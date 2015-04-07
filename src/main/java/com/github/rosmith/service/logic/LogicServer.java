package com.github.rosmith.service.logic;

import java.lang.reflect.Constructor;
import java.util.concurrent.Future;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public abstract class LogicServer extends Logic<Future<Object>, Object, Object> {
	
	protected static Logic<Future<Object>, Object, Object> logic;
	
	private static Class<? extends LogicServer> clzz;
	
	public static void setSingletonClass(Class<? extends LogicServer> clzz){
		LogicServer.clzz = clzz;
	}
	
	public static Logic<Future<Object>, Object, Object> singleton(){
		if(logic == null){
			verifyClassIsNotNull();
			try {
				Constructor<? extends LogicServer> constructor = clzz.getDeclaredConstructor();
				constructor.setAccessible(true);
				logic = constructor.newInstance();
			} catch (Exception e) {
				logic = null;
			}
		}
		return logic;
	}
	
	private static void verifyClassIsNotNull() {
		if(clzz == null) {
			clzz = DefaultLogicServer.class;
		}
	}
	
}
