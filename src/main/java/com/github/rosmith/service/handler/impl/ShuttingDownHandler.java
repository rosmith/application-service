package com.github.rosmith.service.handler.impl;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.github.rosmith.service.annotation.Handler;
import com.github.rosmith.service.protocol.Protocol;
import com.github.rosmith.service.protocol.ProtocolObject;
import com.github.rosmith.service.session.impl.AbstractSession;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
@Handler(protocol=Protocol.SHUT_DOWN)
public class ShuttingDownHandler extends AbstractHandler<Future<Object>, Void> {
	
	private ShuttingDownHandler(String protocol){
		super(protocol);
	}
	
	public <D extends Future<Object>> Object handleClient(AbstractSession<Future<Object>, Void, D> session, ProtocolObject object) {
		session.stopJob();
		return null;
	}

	public <D> void handleServer(AbstractSession<Future<Object>, Void, D> session, ProtocolObject object) {
		getLogger().info("handleServer from ShutdownHandler");
		session.write(object);
		session.stopJob();
		session.disconnect();
	}

	@Override
	public Function<ProtocolObject, Object> clientHandler() {
		return null;
	}

	@Override
	public Function<ProtocolObject, Object> serverHandler() {
		return null;
	}
	
}
