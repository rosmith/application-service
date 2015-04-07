package com.github.rosmith.service.logic;

import com.github.rosmith.service.session.impl.AbstractSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public abstract class Logic<T, D, E> {

	protected AbstractSession<T, D, E> session;
	
	private Logger log = null;

	public Logger getLogger() {
		if(log == null) {
			log = LoggerFactory.getLogger(getClass());					
		}
		return log;
	}

	public void stop() {
		session.disconnect();
	}

}
