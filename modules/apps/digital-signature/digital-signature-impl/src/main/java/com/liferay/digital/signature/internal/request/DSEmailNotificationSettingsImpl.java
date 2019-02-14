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

import com.liferay.digital.signature.request.DSEmailNotificationSettings;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public class DSEmailNotificationSettingsImpl
	implements DSEmailNotificationSettings {

	public void addBccEmailAddresses(Collection<String> bccEmailAddresses) {
		_bccEmailAddresses.addAll(bccEmailAddresses);
	}

	@Override
	public Set<String> getBccEmailAddresses() {
		return _bccEmailAddresses;
	}

	@Override
	public String getReplyEmailAddressNameOverride() {
		return _replyEmailAddressNameOverride;
	}

	@Override
	public String getReplyEmailAddressOverride() {
		return _replyEmailAddressOverride;
	}

	public void setReplyEmailAddressNameOverride(
		String replyEmailAddressNameOverride) {

		_replyEmailAddressNameOverride = replyEmailAddressNameOverride;
	}

	public void setReplyEmailAddressOverride(String replyEmailAddressOverride) {
		_replyEmailAddressOverride = replyEmailAddressOverride;
	}

	private final Set<String> _bccEmailAddresses = new HashSet<>();
	private String _replyEmailAddressNameOverride;
	private String _replyEmailAddressOverride;

}