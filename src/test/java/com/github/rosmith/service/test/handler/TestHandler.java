package com.github.rosmith.service.test.handler;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rosmith.service.annotation.Handler;
import com.github.rosmith.service.handler.impl.AbstractDefaultHandler;
import com.github.rosmith.service.protocol.ProtocolObject;
import com.github.rosmith.service.test.protocol.ProtocolTest;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
@Handler(protocol=ProtocolTest.TEST)
public class TestHandler extends AbstractDefaultHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestHandler.class);
	
	private TestHandler(String protocol){
		super(protocol);
	}
	
	@Override
	public Logger getLogger() {
		return LOG;
	}

	@Override
	public Function<ProtocolObject, Object> serverHandler() {
		return (p) -> {
			return "Message received!";
		};
	}
	
}
