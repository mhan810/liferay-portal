package com.liferay.portal.search.elasticsearch.util;

import java.util.concurrent.Callable;

public class RetryAssertExecutor {
	
	public RetryAssertExecutor(long timeout) {
		_timeout = timeout;
	}
	
	public <T> T execute(Callable<T> callable) 
		throws Exception {
		
		long deadline = System.currentTimeMillis() + _timeout;
		
		while (true) {
			try {
				return callable.call();
			} 
			catch (AssertionError ae) { 
				if (System.currentTimeMillis() > deadline) {
					throw ae;
				}
			}
		}
	}
	
	private final long _timeout;
	
}