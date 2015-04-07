package com.github.rosmith.service.handler.impl;

import java.util.concurrent.Future;

import com.github.rosmith.service.protocol.ProtocolObject;
import com.github.rosmith.service.session.impl.AbstractSession;

public abstract class AbstractDefaultHandler extends AbstractHandler<Future<Object>, Object> {

	protected AbstractDefaultHandler(String protocol) {
		super(protocol);
	}
	
	public final <D extends Future<Object>> Object handleClient(AbstractSession<Future<Object>, Object, D> session, ProtocolObject object) {
		return super.handleClient(session, object);
	}
	
	public final <D> void handleServer(AbstractSession<Future<Object>, Object, D> session, ProtocolObject object) {
		super.handleServer(session, object);
	}

}
