package com.github.rosmith.service.session.impl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rosmith.service.fetcher.Fetcher;
import com.github.rosmith.service.handler.IHandler;
import com.github.rosmith.service.logic.Logic;
import com.github.rosmith.service.protocol.ProtocolObject;
import com.github.rosmith.service.session.ISession;
import com.github.rosmith.service.util.HandlerMap;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public abstract class AbstractSession<T, D, E> implements ISession<T, D, E> {
	
	protected Map<String, IHandler<T, D>> handlers;
	
	protected ObjectOutputStream oos;

	protected ObjectInputStream ois;
	
	private Logger log = null;

	protected boolean stop;
	
	/**
	 * @since 1.4
	 */
	protected Cipher decryptCipher;
	
	/**
	 * @since 1.4
	 */
	protected Cipher encryptCipher;
	
	protected AbstractSession(){
		handlers = new HandlerMap<T, D>();
		Fetcher.load();
		
		try{
			
			DESKeySpec desKeySpec = new DESKeySpec(Fetcher.password().getBytes());
		    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			
			encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
		} catch(Exception e) {
			getLogger().error("Error on initialising cipher", e);
		}
	}

	public void addHandler(IHandler<T, D> handler) throws Exception {
		handlers.put(handler.getProtocol(), handler);
	}

	public void removeHandler(IHandler<T, D> handler) {
		handlers.remove(handler.getProtocol());
	}
	
	public void setLogic(Logic<T, D, E> logic){
		for(String protocol : handlers.keySet()){
			handlers.get(protocol).setLogic(logic);
		}
	}
	
	public void read() {}
	
	public abstract E write(ProtocolObject object) ;
	
	public void stopJob(){
		stop = true;
	}
	
	public abstract void disconnect();

	protected void close(){
        try{
            if(oos!=null){
                oos.close();
            }
            if(ois!=null){
                ois.close();
            }
        }catch(Exception io){
            getLogger().error("Error on closing stream.", io);
        }
    }
	
	public Logger getLogger() {
		if(log == null) {
			log = LoggerFactory.getLogger(getClass());					
		}
		return log;
	}

}
