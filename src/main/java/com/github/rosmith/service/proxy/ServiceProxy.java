package com.github.rosmith.service.proxy;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public abstract class ServiceProxy implements InvocationHandler {
	
	private Logger log = null;
	
	private Object object;
	
	public ServiceProxy(Object obj) {
		this.object = obj;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T activateProxy(T handler, Class<? extends ServiceProxy> proxyClass) throws InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException {
		Constructor<? extends ServiceProxy> constructor = proxyClass.getDeclaredConstructor(Object.class);
		ServiceProxy proxy = constructor.newInstance(handler);
		return (T)Proxy.newProxyInstance(handler.getClass().getClassLoader(), new Class[]{handler.getClass()}, proxy);
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		beforeInvoke(proxy, method, args);
		Object result = null;
		if(shouldInvoke(proxy, method, args)) {
			result = method.invoke(object, args);
		}
		afterInvoke(proxy, method, args);
		return result;
	}
	
	public Logger getLogger() {
		if(log == null) {
			log = LoggerFactory.getLogger(getClass());					
		}
		return log;
	}
	
	public boolean shouldInvoke(Object proxy, Method method, Object[] args) {
		return true;
	}
	
	public abstract void beforeInvoke(Object proxy, Method method, Object[] args);
	
	public abstract void afterInvoke(Object proxy, Method method, Object[] args);
	
	public static class DefaultServiceProxy extends ServiceProxy {

		public DefaultServiceProxy(Object obj) {
			super(obj);
		}

		@Override
		public void beforeInvoke(Object proxy, Method method, Object[] args) {
			
		}

		@Override
		public void afterInvoke(Object proxy, Method method, Object[] args) {
			
		}
		
	}

}
