package com.liferay.portal.security;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Igor Spasic
 */
public class SecurityServiceAdvice {

	public Object invokeService(ProceedingJoinPoint pjp) throws Throwable {

		System.out.println("--------> invoking service: " + pjp.getTarget());

		return pjp.proceed();
	}
}