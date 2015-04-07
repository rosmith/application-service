package com.github.rosmith.service.handler;

import com.github.rosmith.service.logic.Logic;
import com.github.rosmith.service.protocol.ProtocolObject;
import com.github.rosmith.service.session.impl.AbstractSession;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public interface IHandler<T, D> {
	
	public <E extends T> Object handleClient(AbstractSession<T, D, E> session, ProtocolObject object);
	
	public <X> void handleServer(AbstractSession<T, D, X> session, ProtocolObject object);
	
	public <Y> void setLogic(Logic<T, D, Y> logic);
	
	public <Y> Logic<T, D, Y> getLogic();
	
	public void sent();
	
	public String getProtocol();

}
