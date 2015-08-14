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

package com.liferay.portal.search.elasticsearch.internal.facet;

import static com.liferay.portal.search.elasticsearch.connection.OperationMode.EMBEDDED;
import static com.liferay.portal.search.elasticsearch.connection.OperationMode.REMOTE;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.elasticsearch.configuration.ElasticsearchConfiguration;
import com.liferay.portal.search.elasticsearch.connection.OperationMode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class FakeElasticsearchConfiguration
	implements ElasticsearchConfiguration {

	@Override
	public String[] additionalConfigurations() {
		return new String[] {
			(String)getDefaultValuedFromAnnotation("additionalConfigurations")
		};
	}

	@Override
	public boolean bootstrapMlockAll() {
		return Boolean.valueOf(
			(String)getDefaultValuedFromAnnotation("bootstrapMlockAll"));
	}

	@Override
	public boolean clientTransportIgnoreClusterName() {
		return Boolean.valueOf(
			(String)getDefaultValuedFromAnnotation(
				"clientTransportIgnoreClusterName"));
	}

	@Override
	public String clientTransportNodesSamplerInterval() {
		return (String)getDefaultValuedFromAnnotation(
			"clientTransportNodesSamplerInterval");
	}

	@Override
	public boolean clientTransportSniff() {
		return Boolean.valueOf(
			(String)getDefaultValuedFromAnnotation("clientTransportSniff"));
	}

	@Override
	public String clusterName() {
		return (String)getDefaultValuedFromAnnotation("clusterName");
	}

	@Override
	public String discoveryZenPingUnicastHostsPort() {
		return (String)getDefaultValuedFromAnnotation(
			"discoveryZenPingUnicastHostsPort");
	}

	@Override
	public String[] httpCORSConfigurations() {
		return new String[] { (String)getDefaultValuedFromAnnotation(
"httpCORSConfigurations")
		};
	}

	@Override
	public boolean httpCORSEnabled() {
		return Boolean.valueOf(
			(String)getDefaultValuedFromAnnotation("httpCORSEnabled"));
	}

	@Override
	public boolean httpEnabled() {
		return Boolean.valueOf(
			(String)getDefaultValuedFromAnnotation("httpEnabled"));
	}

	@Override
	public boolean logExceptionsOnly() {
		return Boolean.valueOf(
			(String)getDefaultValuedFromAnnotation("logExceptionsOnly"));
	}

	@Override
	public String networkBindHost() {
		return (String)getDefaultValuedFromAnnotation("networkBindHost");
	}

	@Override
	public String networkHost() {
		return (String)getDefaultValuedFromAnnotation("networkHost");
	}

	@Override
	public String networkPublishHost() {
		return (String)getDefaultValuedFromAnnotation("networkPublishHost");
	}

	@Override
	public OperationMode operationMode() {
		String defaultValue = (String)getDefaultValuedFromAnnotation(
			"operationMode");

		if (StringUtil.equalsIgnoreCase(REMOTE.name(), defaultValue)) {
			return REMOTE;
		}

		return EMBEDDED;
	}

	@Override
	public int retryOnConflict() {
		return Integer.valueOf(
			(String)getDefaultValuedFromAnnotation("retryOnConflict"));
	}

	@Override
	public String[] transportAddresses() {
		return (String[])getDefaultValuedFromAnnotation("transportAddresses");
	}

	@Override
	public String transportTcpPort() {
		return (String)getDefaultValuedFromAnnotation("transportTcpPort");
	}

	protected Object getDefaultValuedFromAnnotation(String methodName) {
		Object defaultValue = null;

		try {
			Method method = ElasticsearchConfiguration.class.getMethod(
				methodName);

			Annotation annotation = method.getAnnotations()[0];
			Class<? extends Annotation> type = annotation.annotationType();

			defaultValue = type.getMethod("deflt").invoke(annotation);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

}