package com.github.rosmith.service.util;

import java.util.HashMap;

import com.github.rosmith.service.handler.IHandler;
import com.github.rosmith.service.handler.impl.NoProtocolHandler;
import com.github.rosmith.service.protocol.Protocol;

public class HandlerMap<K, V> extends HashMap<String, IHandler<K, V>> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public IHandler<K, V> get(Object key) {
		IHandler<K, V> handler = super.get(key);
		if(handler == null){
			return new NoProtocolHandler<K, V>(Protocol.NULL);
		}
		return handler;
	}

}
