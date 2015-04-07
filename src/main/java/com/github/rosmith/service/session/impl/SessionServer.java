package com.github.rosmith.service.session.impl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.crypto.SealedObject;

import com.github.rosmith.service.handler.IHandler;
import com.github.rosmith.service.logic.Logic;
import com.github.rosmith.service.protocol.ProtocolObject;
import com.github.rosmith.service.scanner.HandlerScanner;
import com.github.rosmith.service.server.Server;

public class SessionServer extends
		AbstractSession<Future<Object>, Object, Object> implements Runnable {

	private Logic<Future<Object>, Object, Object> logic;

	private String threadName;

	private Socket socket;

	@SuppressWarnings("unchecked")
	private SessionServer(Server server, Socket socket) {

		super();

		this.socket = socket;

		this.logic = (Logic<Future<Object>, Object, Object>) server.getLogic();

		List<IHandler<Future<Object>, Object>> handlers = new ArrayList<IHandler<Future<Object>, Object>>();
		try {
			handlers = HandlerScanner.<Future<Object>, Object> scan();
		} catch (Exception e1) {
			e1.printStackTrace();
			getLogger().error("Exception on adding handlers: " + e1);
		}
		try {
			for (IHandler<Future<Object>, Object> handler : handlers) {
				handler.setLogic(logic);
				addHandler(handler);
			}
		} catch (Exception e1) {
			getLogger().error("An error occured while instantiating handlers",
					e1);
		}
		System.out.println(handlers);
		initStreams();
	}

	private void initStreams() {
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	public Object write(ProtocolObject object) {
		getLogger().debug("Sending protocol '{}'", object.getProtocol());
		try {
			oos.writeObject(new SealedObject(object, encryptCipher));
			oos.flush();
		} catch (SocketException e) {
			getLogger().error("An error occured while writing in the stream", e);
			disconnect();
		} catch (Exception e) {
			getLogger().error("An error occured while writing in the stream", e);
		}
		handlers.get(object.getProtocol()).sent();
		return null;
	}

	public static SessionServer create(Server server, Socket socket) {
		return new SessionServer(server, socket);
	}

	public void disconnect() {
		try {
			close();
		} catch (Exception e) {
			getLogger().error("Error on killing client thread.", e);
		}
	}

	@Override
	public void run() {
		Object input = null;
		while (!stop) {
			try {
				if ((input = ois.readObject()) != null) {
					SealedObject sealed = (SealedObject) input;
					ProtocolObject value = (ProtocolObject) sealed
							.getObject(decryptCipher);
					handlers.get(value.getProtocol()).handleServer(this, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().error(e.getMessage(), e);
				stop = true;
			}
		}
		getLogger().info("Server thread shuts down...");
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

}
