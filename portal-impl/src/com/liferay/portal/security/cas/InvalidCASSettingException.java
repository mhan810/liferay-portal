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

package com.liferay.portal.security.cas;

import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

/**
 * @author Edward C. Han
 */
public class InvalidCASSettingException extends PortalException {

	public InvalidCASSettingException(List<String> errors) {
		this.errors = errors;
	}

	public InvalidCASSettingException(String msg, List<String> errors) {
		super(msg);

		this.errors = errors;
	}

	public InvalidCASSettingException(
			String msg, Throwable cause, List<String> errors) {

		super(msg, cause);

		this.errors = errors;
	}

	public InvalidCASSettingException(Throwable cause, List<String> errors) {
		super(cause);

		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}

	private List<String> errors;

}