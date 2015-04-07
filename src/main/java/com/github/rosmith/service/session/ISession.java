package com.github.rosmith.service.session;

import com.github.rosmith.service.handler.IHandler;
import com.github.rosmith.service.protocol.ProtocolObject;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public interface ISession<T, D, E> {
	
	public void addHandler(IHandler<T, D> handler)  throws Exception;
	
	public void removeHandler(IHandler<T, D> handler);
	
	public E write(ProtocolObject object);

}
