/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.digital.signature.internal.request;

import com.liferay.digital.signature.request.DSEmailNotification;
import com.liferay.digital.signature.request.DSEmailNotificationSettings;

/**
 * @author Michael C. Han
 */
public class DSEmailNotificationImpl implements DSEmailNotification {

	public DSEmailNotificationImpl(String subject, String message) {
		_subject = subject;
		_message = message;
	}

	@Override
	public DSEmailNotificationSettings getDSEmailNotificationSettings() {
		return _dsEmailNotificationSettings;
	}

	@Override
	public String getMessage() {
		return _message;
	}

	@Override
	public String getSubject() {
		return _subject;
	}

	public void setDSEmailNotificationSettings(
		DSEmailNotificationSettingsImpl dsEmailNotificationSettings) {

		_dsEmailNotificationSettings = dsEmailNotificationSettings;
	}

	private DSEmailNotificationSettings _dsEmailNotificationSettings;
	private final String _message;
	private final String _subject;

}