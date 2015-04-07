package com.github.rosmith.service.session.impl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.SealedObject;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.github.rosmith.service.fetcher.Fetcher;
import com.github.rosmith.service.handler.IHandler;
import com.github.rosmith.service.protocol.Protocol;
import com.github.rosmith.service.protocol.ProtocolObject;
import com.github.rosmith.service.scanner.HandlerScanner;
import com.github.rosmith.service.wrapper.FutureWrapper;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public class SessionClient extends
		AbstractSession<Future<Object>, Object, Future<Object>> {

	private final ExecutorService SERVICE = Executors.newCachedThreadPool();
	
	private Socket socket;

	private SessionClient() {

		super();

		List<IHandler<Future<Object>, Object>> handlers = new ArrayList<IHandler<Future<Object>, Object>>();
		try {
			handlers = HandlerScanner.<Future<Object>, Object> scan();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			getLogger().info("Number of handlers: " + handlers.size());
			getLogger().debug("Handlers list: {}", handlers);
			for (IHandler<Future<Object>, Object> handler : handlers) {
				addHandler(handler);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		initStreams();
	}

	private void initStreams() {
		try {
			if (Fetcher.useSSL()) {
				SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory
						.getDefault();
				socket = (SSLSocket) sf.createSocket(Fetcher.host(),
						Fetcher.port());
			} else {
				SocketFactory sf = (SocketFactory) SocketFactory.getDefault();
				socket = (Socket) sf.createSocket(Fetcher.host(),
						Fetcher.port());
			}
			
			getLogger().info("Client is connected");

			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
		} catch (Exception e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	public static SessionClient create() {
		return new SessionClient();
	}

	private Object helpWrite(ProtocolObject object) {
		try {
			oos.writeObject(new SealedObject(object, encryptCipher));
			oos.flush();
		} catch (Exception e) {
			getLogger()
					.error("An error occured while writing in the stream", e);
		}
		handlers.get(object.getProtocol()).sent();
		Object input = null, result = null;
		try {
			if ((input = ois.readObject()) != null) {
				SealedObject sealed = (SealedObject) input;
				ProtocolObject value = (ProtocolObject) sealed
						.getObject(decryptCipher);
				result = handlers.get(value.getProtocol()).handleClient(this,
						value);
			}
		} catch (Exception io) {
			getLogger().error("Client suddenly shuts down due to error...", io);
			return null;
		}
		return result;
	}

	public Future<Object> write(ProtocolObject object) {
		Future<Object> toBeWrapped = SERVICE.submit(() -> {
			return helpWrite(object);
		});
		Future<Object> wrapper = new FutureWrapper(toBeWrapped);
		return wrapper;
	}

	@Override
	public void disconnect() {
		Future<Object> object = write(new ProtocolObject(Protocol.SHUT_DOWN));
		try {
			object.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		SERVICE.shutdown();
		getLogger().info("Threads successfully terminated");
	}

}
