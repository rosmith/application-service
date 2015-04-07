package com.github.rosmith.service.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public class Protocol {

	public static final String SHUT_DOWN = "SHUT_DOWN";

	public static final String NULL = "NULL";
	
	private static List<String> protocols = new ArrayList<>();
	
	public static void registerDefaultProtocols() {
		register(SHUT_DOWN);
		register(NULL);
	}

	public static void register(String protocol) {
		if (!protocols.contains(protocol)) {
			protocols.add(protocol);
		}
	}

	public static boolean isRegistered(String protocol) {
		return protocols.contains(protocol);
	}

	public static void verifyIfExists(String protocol) {
		if (!isRegistered(protocol)) {
			throw new RuntimeException("The protocol " + protocol
					+ " is not yet registered.");
		}
	}

}
