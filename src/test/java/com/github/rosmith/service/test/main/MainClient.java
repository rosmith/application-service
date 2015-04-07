package com.github.rosmith.service.test.main;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
public class MainClient {
	
	@Before
	public void setUp() {
		Service.initialise();
	}

	@Test
	public void test() {

		String response = Service.test("Hello server!");
		
		Assert.assertEquals(response, "Message received!");
		
		Service.disconnect();
	}
	
	@After
	public void cleanUp() {
		Service.disconnect();
	}

}
