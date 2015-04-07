package com.github.rosmith.service.logic;

import java.util.concurrent.Future;

import com.github.rosmith.service.fetcher.Fetcher;
import com.github.rosmith.service.session.impl.SessionClient;

public abstract class LogicClient extends Logic<Future<Object>, Object, Future<Object>> {
	
	public LogicClient(){
		Fetcher.load();
		session = SessionClient.create();
		session.setLogic(this);
		getLogger().debug("Properties: " + Fetcher.port());
		getLogger().debug("Properties: " + Fetcher.host());
	}

}