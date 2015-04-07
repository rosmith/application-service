package com.github.rosmith.service.scanner;

import com.github.rosmith.service.annotation.Handler;
import com.github.rosmith.service.fetcher.Fetcher;
import com.github.rosmith.service.handler.IHandler;
import com.github.rosmith.service.protocol.Protocol;
import com.github.rosmith.service.proxy.ServiceProxy;
import com.github.rosmith.service.proxy.ServiceProxy.DefaultServiceProxy;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.reflect.ClassPath;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public class HandlerScanner {

	private static final String HANDLER_PACKAGE = "com.github.rosmith.service.handler.impl";

	/**
	 * Scans the package "module.application.service.handler" for classes
	 * annotated with "@Handler(protocol=...)". For every class found with this
	 * annotation an instance of this class is created per Java-Reflection and
	 * added to the result list of handlers.
	 * 
	 * @return A list of instantiated and proxied handler.
	 * @throws Exception
	 *             Reflection-Exception
	 */
	public static <T, D> List<IHandler<T, D>> scan() throws Exception {
		return scan(false);
	}

	@SuppressWarnings("unchecked")
	private static <T, D> List<IHandler<T, D>> scan(boolean proxy)
			throws Exception {
		List<IHandler<T, D>> handlers = new ArrayList<IHandler<T, D>>();

		Set<Class<?>> types = scan(HANDLER_PACKAGE, Handler.class);

		for (String handlerPackage : Fetcher.handlerPackages()) {
			for (Class<?> clzz : scan(handlerPackage, Handler.class)) {
				if (!types.contains(clzz)) {
					types.add(clzz);
				}
			}
		}

		for (Class<?> type : types) {
			Handler annotation = type.getAnnotation(Handler.class);
			Constructor<?> c = type.getDeclaredConstructor(String.class);
			c.setAccessible(true);

			Protocol.verifyIfExists(annotation.protocol());

			IHandler<T, D> handler = (IHandler<T, D>) c.newInstance(annotation
					.protocol());
			if (proxy) {
				handler = ServiceProxy.activateProxy(handler, DefaultServiceProxy.class);
			}
			handlers.add(handler);
		}

		return handlers;
	}

	public static Set<Class<?>> scan(String packageName) {
		Set<Class<?>> set = new HashSet<Class<?>>();
		ClassPath classpath = null;
		try {
			classpath = ClassPath.from(HandlerScanner.class.getClassLoader());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (ClassPath.ClassInfo classInfo : classpath
				.getTopLevelClasses(packageName)) {
			try {
				Class<?> clzz = Class.forName(packageName+"."+classInfo.getSimpleName());
				set.add(clzz);
			} catch (ClassNotFoundException e) {
			}
		}
		return set;
	}
	
	public static Set<Class<?>> scan(String packageName, Class<? extends Annotation> annotation) {
		Set<Class<?>> result = scan(packageName);
		result = result.stream().filter(c -> {
			Annotation anno = c.getDeclaredAnnotation(annotation);
			return anno != null && anno.annotationType() != null;
		}).collect(Collectors.toSet());
		return result;
	}

	public static void main(String[] args) {
		scan(HANDLER_PACKAGE);
	}

	/**
	 * Scans the package "module.application.service.handler" for classes
	 * annotated with "@Handler(protocol=...)". For every class found with this
	 * annotation an instance of this class is created per Java-Reflection and
	 * added to the result list of handlers. The difference between this method
	 * and the scan-Method is that this method creates a proxy-object for each
	 * created instance. The reason for doing this is that upon calling any
	 * method of these proxied-instances, a small logic has to be done after
	 * this method has returned and just before the caller receives the result.
	 * 
	 * @return A list of instantiated and proxied handler.
	 * @throws Exception
	 *             Reflection-Exception
	 */
	public static <T, D> List<IHandler<T, D>> proxyScan() throws Exception {
		return scan(true);
	}

}
