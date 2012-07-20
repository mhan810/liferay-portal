/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.service.persistence.lar;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * @author Mate Thurzo
 */
public class DigestErrorRegistryAdvice implements ThrowsAdvice {

	public void afterThrowing(
		Method m, Object[] args, Object target, Exception e) {

		_log.error("---Called---");
	}

	private Log _log = LogFactoryUtil.getLog(DigestErrorRegistryAdvice.class);

}
