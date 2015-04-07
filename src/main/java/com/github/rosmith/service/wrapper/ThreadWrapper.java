package com.github.rosmith.service.wrapper;

import java.lang.Thread.State;
import java.lang.Thread.UncaughtExceptionHandler;

import com.github.rosmith.service.session.impl.SessionServer;

public class ThreadWrapper {
	
	private Thread wrapped;
	
	private SessionServer runnable;
	
	public ThreadWrapper(SessionServer t) {
		runnable = t;
		wrapped = new Thread(runnable);
		runnable.setThreadName(getName());
	}

	public synchronized void start() {
		wrapped.start();
	}
	
	public void setName(String name) {
		wrapped.setName(name);
	}
	
	public String getName() {
		return wrapped.getName();
	}
	
	public void disconnect() {
		runnable.disconnect();
	}

	public boolean isDaemon() {
		return wrapped.isDaemon();
	}
	
	public boolean isAlive() {
		return wrapped.isAlive();
	}

	public ClassLoader getContextClassLoader() {
		return wrapped.getContextClassLoader();
	}


	public long getId() {
		return wrapped.getId();
	}


	public StackTraceElement[] getStackTrace() {
		return wrapped.getStackTrace();
	}


	public State getState() {
		return wrapped.getState();
	}

	public void interrupt() {
		wrapped.interrupt();
	}

	public boolean isInterrupted() {
		return wrapped.isInterrupted();
	}

	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return wrapped.getUncaughtExceptionHandler();
	}

	public void run() {
		wrapped.run();
	}

	public void setContextClassLoader(ClassLoader cl) {
		wrapped.setContextClassLoader(cl);
	}

	public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
		wrapped.setUncaughtExceptionHandler(eh);
	}

	public int hashCode() {
		return wrapped.hashCode();
	}
	

	public String toString() {
		return wrapped.toString();
	}
	
}
