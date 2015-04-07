package com.github.rosmith.service.fetcher;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.github.rosmith.service.protocol.Protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public class Fetcher {

	private static final Logger LOG = LoggerFactory.getLogger(Fetcher.class);

	private static Properties properties;

	static String FETCHER_DIR = System.getProperty("user.home")
			+ File.separator + ".app";

	public static void load() {
		if (properties != null)
			return;

		properties = new Properties();
		try {
			properties.load(Fetcher.class.getClassLoader().getResourceAsStream(
					"application-service.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		LOG.debug("Properties: " + properties);
		registerProtocols();

	}

	public static void main(String[] args) {
		load();
		System.out.println(port());
	}

	public static int port() {
		return new Integer((String) properties.get("com.github.rosmith.service.port"));
	}

	public static String host() {
		return (String) properties.get("com.github.rosmith.service.host");
	}
	
	public static String[] handlerPackages() {
		String result = (String) properties.get("com.github.rosmith.service.handlers.package");
		return result != null ? result.split(",") : new String[0];
	}
	
	public static String password() {
		return (String) properties.get("com.github.rosmith.service.security.password");
	}
	
	public static boolean useSSL() {
		return Boolean.valueOf((String)properties.get("com.github.rosmith.service.security.useSSL"));
	}
	
	public static File keyStore() {
		return new File((String) properties.get("com.github.rosmith.service.security.password"));
	}
	
	public static String keyStorePassword() {
		return (String) properties.get("com.github.rosmith.service.security.keyStorePassword");
	}
	
	public static File trustStore() {
		return new File((String) properties.get("com.github.rosmith.service.security.trustStore"));
	}
	
	public static String trustStorePassword() {
		return (String) properties.get("com.github.rosmith.service.security.trustStorePassword");
	}
	
	public static long timeout() {
		String timeout = (String)properties.get("com.github.rosmith.service.protocols.timeout");
		if(timeout == null || timeout.isEmpty()) {
			timeout = "5";
		}
		return Long.valueOf(timeout);
	}
	
	public static void registerProtocols() {
		String result = (String) properties.get("com.github.rosmith.service.protocols");
		if(result == null){
			return;
		}
		Protocol.registerDefaultProtocols();
		for(String s : result.split(",")){
			if(!Protocol.isRegistered(s)){
				Protocol.register(s);
			}
		}
	}

}
