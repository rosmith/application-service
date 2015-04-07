package com.github.rosmith.service.handler.impl;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rosmith.service.handler.IHandler;
import com.github.rosmith.service.logic.Logic;
import com.github.rosmith.service.protocol.ProtocolObject;
import com.github.rosmith.service.session.impl.AbstractSession;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public abstract class AbstractHandler<T, E> implements IHandler<T, E> {
	
	protected String protocol;
	
	protected Logic<T, E, ?> logic;
	
	private Logger log = null;
	
	protected AbstractHandler(final String protocol){
		if(protocol == null)
			throw new IllegalArgumentException("Protocol cannot be null");
		this.protocol = protocol;
	}
	
	public <D extends T> Object handleClient(AbstractSession<T, E, D> session, ProtocolObject object) {
		getLogger().info("Received from server...");
		int i = 0;
		for(Object o : object.getObject()) {
			getLogger().debug("Response[{}] from server {}", i++, o);
		}
		Function<ProtocolObject, Object> function = clientHandler();
		return function == null ? null : function.apply(object);
	}
	
	public <D> void handleServer(AbstractSession<T, E, D> session, ProtocolObject object) {
		getLogger().info("Received from client...");
		int i = 0;
		for(Object o : object.getObject()) {
			getLogger().debug("Input[{}] from client {}", i++, o);
		}
		Function<ProtocolObject, Object> function = serverHandler();
		Object result = function.apply(object);
		session.write(new ProtocolObject(getProtocol(), result));
	}
	
	public Function<ProtocolObject, Object> clientHandler() {
		return (p) -> {
			return p.getObject()[0];
		};
	}
	
	public abstract Function<ProtocolObject, Object> serverHandler();
	
	public void sent() {
		getLogger().info("Sending request...");
	}
	
	public <D>  void setLogic(Logic<T, E, D> logic) {
		this.logic = logic;
	}
	
	public String getProtocol(){
		return protocol;
	}
	
	@SuppressWarnings("unchecked")
	public Logic<T, E, ?> getLogic(){
		return logic;
	}
	
	public Logger getLogger() {
		if(log == null) {
			log = LoggerFactory.getLogger(getClass());					
		}
		return log;
	}

}
