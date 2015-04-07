package com.github.rosmith.service.wrapper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.github.rosmith.service.fetcher.Fetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FutureWrapper implements Future<Object> {
	
	private static final Logger LOG = LoggerFactory.getLogger(FutureWrapper.class);
	
	private Future<Object> wrapped;
	
	public FutureWrapper(Future<Object> future) {
		this.wrapped = future;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return wrapped.cancel(mayInterruptIfRunning);
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		try {
			return get(Fetcher.timeout(), TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			LOG.error("No response from the server.", e);
		}
		return null;
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return wrapped.get(timeout, unit);
	}

	@Override
	public boolean isCancelled() {
		return wrapped.isCancelled();
	}

	@Override
	public boolean isDone() {
		return wrapped.isDone();
	}

}
