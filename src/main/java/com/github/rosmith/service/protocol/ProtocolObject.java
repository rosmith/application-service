package com.github.rosmith.service.protocol;

import java.io.Serializable;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public class ProtocolObject implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String protocol;
	
	private Object[] values;
	
	public ProtocolObject(String protocol, Object...objects){
		Protocol.verifyIfExists(protocol);
		this.protocol = protocol;
		this.values = objects;
	}
	
	public String getProtocol(){
		return protocol;
	}
	
	public Object getObject()[]{
		return values;
	}

}
