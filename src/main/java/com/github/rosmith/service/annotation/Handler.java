package com.github.rosmith.service.annotation;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.*;

/**
 * @author Ronald Smith, Djomkam Yotedje
 */
@Retention(RUNTIME)
public @interface Handler {
	
	String protocol();

}
