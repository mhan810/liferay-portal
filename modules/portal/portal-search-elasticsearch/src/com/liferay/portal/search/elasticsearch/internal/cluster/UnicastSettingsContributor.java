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

package com.liferay.portal.search.elasticsearch.internal.cluster;

import com.liferay.portal.search.elasticsearch.settings.SettingsContributor;

import org.elasticsearch.common.settings.ImmutableSettings;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 */
@Component(immediate = true, service = SettingsContributor.class)
public class UnicastSettingsContributor implements SettingsContributor {

	@Override
	public void populate(ImmutableSettings.Builder builder) {
		if (!_unicastContext.isUnicastWanted()) {
			return;
		}

		builder.put("node.local", false);
		builder.put("discovery.zen.ping.multicast.enabled", false);

		builder.putArray(
			"discovery.zen.ping.unicast.hosts", _unicastContext.getHosts());
	}

	@Reference(unbind = "-")
	public void setUnicastContext(UnicastContext unicastContext) {
		_unicastContext = unicastContext;
	}

	private UnicastContext _unicastContext;

}