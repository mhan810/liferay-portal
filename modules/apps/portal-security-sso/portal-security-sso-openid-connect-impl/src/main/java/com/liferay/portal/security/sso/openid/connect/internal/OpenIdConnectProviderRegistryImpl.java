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

package com.liferay.portal.security.sso.openid.connect.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectServiceException;
import com.liferay.portal.security.sso.openid.connect.internal.configuration.OpenIdConnectProviderConfiguration;

import java.net.URL;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.annotations.Component;

/**
 * @author Thuong Dinh
 * @author Edward C. Han
 */
@Component(
	immediate = true,
	property = Constants.SERVICE_PID + "=com.liferay.portal.security.sso.openid.connect.internal.configuration.OpenIdConnectProviderConfiguration",
	service = {
		com.liferay.portal.security.sso.openid.connect.OpenIdConnectProviderRegistry.class,
		ManagedServiceFactory.class, OpenIdConnectProviderRegistry.class
	}
)
public class OpenIdConnectProviderRegistryImpl
	implements OpenIdConnectProviderRegistry, ManagedServiceFactory {

	@Override
	public void deleted(String factoryPid) {
		removeOpenConnectIdProvider(factoryPid);
	}

	@Override
	public OpenIdConnectProviderImpl findOpenIdConnectProvider(String name)
		throws OpenIdConnectServiceException.ProviderException {

		OpenIdConnectProviderImpl openIdConnectProviderImpl = getOpenIdConnectProvider(
			name);

		if (openIdConnectProviderImpl == null) {
			throw new OpenIdConnectServiceException.ProviderException(
				"Unable to get OpenId Connect provider with name " + name);
		}

		return openIdConnectProviderImpl;
	}

	@Override
	public String getName() {
		return "OpenId Connect Provider Factory";
	}

	@Override
	public OpenIdConnectProviderImpl getOpenIdConnectProvider(String name) {
		return _openIdConnectProvidersPerName.get(name);
	}

	@Override
	public Collection<String> getOpenIdConnectProviderNames() {
		if (_openIdConnectProvidersPerName.isEmpty()) {
			return Collections.emptySet();
		}

		return Collections.unmodifiableCollection(
			_openIdConnectProvidersPerName.keySet());
	}

	@Override
	public void updated(String factoryPid, Dictionary<String, ?> properties)
		throws ConfigurationException {

		OpenIdConnectProviderConfiguration openIdConnectProviderConfiguration =
			ConfigurableUtil.createConfigurable(
				OpenIdConnectProviderConfiguration.class, properties);

		synchronized (_openIdConnectProvidersPerFactory) {
			OpenIdConnectProviderImpl openIdConnectProviderImpl =
				createOpenIdConnectProvider(openIdConnectProviderConfiguration);

			removeOpenConnectIdProvider(factoryPid);

			addOpenConnectIdConnectProvider(factoryPid,
			                                openIdConnectProviderImpl);
		}
	}

	protected void addOpenConnectIdConnectProvider(
		String factoryPid, OpenIdConnectProviderImpl openIdConnectProviderImpl) {

		synchronized (_openIdConnectProvidersPerFactory) {
			_openIdConnectProvidersPerFactory.put(
				factoryPid, openIdConnectProviderImpl);

			_openIdConnectProvidersPerName.put(
				openIdConnectProviderImpl.getName(), openIdConnectProviderImpl);
		}
	}

	protected OpenIdConnectProviderImpl createOpenIdConnectProvider(
			OpenIdConnectProviderConfiguration
				openIdConnectProviderConfiguration)
		throws ConfigurationException {

		OpenIdConnectMetadataFactory openIdConnectMetadataFactory = null;

		try {
			if (Validator.isNotNull(
					openIdConnectProviderConfiguration.discoveryEndPoint())) {

				openIdConnectMetadataFactory =
					new OpenIdConnectMetadataFactoryImpl(
						openIdConnectProviderConfiguration.providerName(),
						new URL(
							openIdConnectProviderConfiguration.
								discoveryEndPoint()),
						openIdConnectProviderConfiguration.
							discoveryEndPointCacheInMillis());
			}
			else {
				openIdConnectMetadataFactory =
					new OpenIdConnectMetadataFactoryImpl(
						openIdConnectProviderConfiguration.providerName(),
						openIdConnectProviderConfiguration.issuerURL(),
						openIdConnectProviderConfiguration.subjectTypes(),
						openIdConnectProviderConfiguration.jwksURI(),
						openIdConnectProviderConfiguration.
							authorizationEndPoint(),
						openIdConnectProviderConfiguration.tokenEndPoint(),
						openIdConnectProviderConfiguration.userInfoEndPoint());
			}
		}
		catch (Exception e) {
			throw new ConfigurationException(
				null,
				"Unable to instantiate provider metadata factory for " +
					openIdConnectProviderConfiguration.providerName(),
				e);
		}

		OpenIdConnectProviderImpl
			openIdConnectProviderImpl = new OpenIdConnectProviderImpl(
			openIdConnectProviderConfiguration.providerName(),
			openIdConnectProviderConfiguration.openIdConnectClientId(),
			openIdConnectProviderConfiguration.openIdConnectClientSecret(),
			openIdConnectProviderConfiguration.scopes(),
			openIdConnectMetadataFactory);

		return openIdConnectProviderImpl;
	}

	protected void removeOpenConnectIdProvider(String factoryPid) {
		synchronized (_openIdConnectProvidersPerFactory) {
			OpenIdConnectProviderImpl openIdConnectProviderImpl =
				_openIdConnectProvidersPerFactory.remove(factoryPid);

			if (openIdConnectProviderImpl != null) {
				_openIdConnectProvidersPerName.remove(
					openIdConnectProviderImpl.getName());
			}
		}
	}

	private final Map<String, OpenIdConnectProviderImpl>
		_openIdConnectProvidersPerFactory = new ConcurrentHashMap<>();
	private final Map<String, OpenIdConnectProviderImpl>
		_openIdConnectProvidersPerName = new ConcurrentHashMap<>();

}