package com.github.rosmith.service.test.logic;

import com.github.rosmith.service.logic.LogicClient;
import com.github.rosmith.service.protocol.ProtocolObject;
import com.github.rosmith.service.test.protocol.ProtocolTest;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public class Client extends LogicClient {

	private static final Logger LOG = LoggerFactory.getLogger(Client.class);

	public String test(String msg) {

		ProtocolObject protocol = new ProtocolObject(
				ProtocolTest.TEST,
				new Object[] { msg });

		LOG.debug("SESSION: {}", session);

		Future<Object> promise = session.write(protocol);

		String value = null;
		try {
			value = (String) promise.get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.debug("Received: {}", value);
		
		return value;
	}

	@Override
	public Logger getLogger() {
		return LOG;
	}

}
