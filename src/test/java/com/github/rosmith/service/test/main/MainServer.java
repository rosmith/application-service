package com.github.rosmith.service.test.main;

import com.github.rosmith.service.logic.LogicServer;
import com.github.rosmith.service.server.Server;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public class MainServer {

	public static void main(String[] args){
		Server.execute();
		System.out.println(LogicServer.singleton());
	}

}
