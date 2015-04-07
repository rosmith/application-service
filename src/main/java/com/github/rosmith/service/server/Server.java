package com.github.rosmith.service.server;

import com.github.rosmith.service.fetcher.Fetcher;
import com.github.rosmith.service.logic.Logic;
import com.github.rosmith.service.logic.LogicServer;
import com.github.rosmith.service.session.impl.SessionServer;
import com.github.rosmith.service.wrapper.ThreadWrapper;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public class Server extends Thread {

	private final Logger LOG = LoggerFactory.getLogger(Server.class);

	private static Server instance;

	private boolean acceptConnections;

	private Map<String, ThreadWrapper> clients;

	private ServerSocket socket;

	Logic<?, ?, ?> logic;

	private boolean continueListening = true;

	private Thread shuttingOffThreadsListener = new Thread() {
		public void run() {
			while (continueListening) {
				clean();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private Server() {

		acceptConnections = true;

		clients = new HashMap<String, ThreadWrapper>();

		Fetcher.load();

		shuttingOffThreadsListener.start();

		logic = LogicServer.singleton();

		try {

			if (Fetcher.useSSL()) {
				SSLServerSocketFactory sf = (SSLServerSocketFactory) ServerSocketFactory
						.getDefault();
				socket = (SSLServerSocket) sf
						.createServerSocket(Fetcher.port());
			} else {
				ServerSocketFactory sf = (ServerSocketFactory) ServerSocketFactory
						.getDefault();
				socket = (ServerSocket) sf.createServerSocket(Fetcher.port());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (acceptConnections) {
			try {
				LOG.info("Server waits for in coming connections ...");
				SessionServer session = SessionServer.create(this,
						socket.accept());
				LOG.info("Client accepted ...");
				ThreadWrapper thread = new ThreadWrapper(session);
				thread.setName(session.getClass().getSimpleName() + "_"
						+ clients.size());
				clients.put(thread.getName(), thread);
				thread.start();
				LOG.info("Number of available clients: " + size());
			} catch (Exception e) {
				e.printStackTrace();
				shutdown();
			}
		}

		LOG.debug("Server shutting down ...");
	}

	public Logic<?, ?, ?> getLogic() {
		return logic;
	}

	private void kill(String threadName) {
		if (clients.remove(threadName) != null) {
			LOG.info("A client has been removed");
			LOG.info("Number of available clients: " + size());
		}
	}

	public static void execute() {
		instance = new Server();
		instance.start();
	}

	public static Server get() {
		return instance;
	}

	private void clean() {
		for (ThreadWrapper thread : new LinkedList<>(clients.values())) {
			if (thread.isDaemon() || !thread.isAlive()) {
				thread.disconnect();
				kill(thread.getName());
			}
		}
	}

	private int size() {
		return clients.size();
	}

	/**
	 * closes the server
	 */
	public void shutdown() {
		acceptConnections = false;
		continueListening = false;
		socket = null;
		instance = null;
	}

}
