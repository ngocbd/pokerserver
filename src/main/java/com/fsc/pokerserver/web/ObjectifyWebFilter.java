package com.fsc.pokerserver.web;
import javax.servlet.annotation.WebFilter;

import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;

/**
 * Filter required by Objectify to clean up any thread-local transaction contexts and pending
 * asynchronous operations that remain at the end of a request.
 */
@WebFilter(urlPatterns = {"/*"})
public class ObjectifyWebFilter extends ObjectifyFilter {
	static {
		ObjectifyService.init();
		ObjectifyService.register(User.class);
	}
	
	
}