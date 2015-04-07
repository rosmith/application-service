package com.github.rosmith.service.handler.impl;

import java.util.function.Function;

import com.github.rosmith.service.annotation.Handler;
import com.github.rosmith.service.protocol.Protocol;
import com.github.rosmith.service.protocol.ProtocolObject;
import com.github.rosmith.service.session.impl.AbstractSession;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
@Handler(protocol=Protocol.NULL)
public class NoProtocolHandler<T, V> extends AbstractHandler<T, V> {
	
	public NoProtocolHandler(String protocol){
		super(protocol);
	}
	
	public <D extends T> Object handleClient(AbstractSession<T, V, D> session, ProtocolObject object) {
		getLogger().error("The protocol '{}' does not exist.", object.getProtocol());
		return null;
	}

	public <D> void handleServer(AbstractSession<T, V, D> session, ProtocolObject object) {
		getLogger().error("The protocol '{}' does not exist.", object.getProtocol());
		session.write(object);
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
