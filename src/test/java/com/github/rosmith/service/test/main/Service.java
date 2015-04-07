package com.github.rosmith.service.test.main;

import com.github.rosmith.service.test.logic.Client;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public class Service {

	private static Service service;

	private static Client logic;

	private Service() {
		logic = new Client();
	}

	/**
	 * Initialises the service.
	 */
	public static void initialise() {
		if (service == null)
			service = new Service();
	}
	
	public static String test(String msg) {
		return logic.test(msg);
	}
	
	private void stop(){
		logic.stop();
		logic = null;
		service = null;
	}
	
	/**
	 * Disconnects the service.
	 */
	public static void disconnect(){
		if(service != null){
			service.stop();
		}
	}

}
