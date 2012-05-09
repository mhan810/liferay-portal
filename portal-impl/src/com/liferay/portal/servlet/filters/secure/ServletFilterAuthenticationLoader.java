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

package com.liferay.portal.servlet.filters.secure;

import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstancePool;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.auth.AuthenticationConfig;
import com.liferay.portal.security.auth.PortalAuthenticator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Tomas Polesovsky
 */
public class ServletFilterAuthenticationLoader {


	public AuthenticationConfig load(HttpServletRequest request) {
		List<AuthenticationRule> rules = loadConfiguration();
		for(AuthenticationRule rule : rules){
			if(rule.match(request)){
				return rule.getAuthenticationConfig();
			}
		}

		return null;
	}


	protected List<AuthenticationRule> loadConfiguration(){
		// TODO: make it extensible for Hooks
		List<AuthenticationRule> result = new ArrayList<AuthenticationRule>();

		boolean finished = false;

		for (int i = 0; i < Integer.MAX_VALUE && !finished; i++) {
			String prefix = "portal.api.authentication.config["+i+"].";

			Properties configProps = PropsUtil.getProperties(prefix, true);
			if(configProps != null && configProps.size() > 0){
				AuthenticationRule rule = loadConfiguration(configProps);
				result.add(rule);
			} else {
				finished = true;
			}
		}

		return result;
	}

	protected AuthenticationRule loadConfiguration(Properties configProps) {
		if(configProps == null) {
			return null;
		}

		AuthenticationRule rule = new AuthenticationRule();
		AuthenticationConfig config = new AuthenticationConfig();
		rule.registerAuthenticationConfig(config);
		config.setSettings(configProps);

		String[] urls = StringUtil.split(configProps.getProperty("urls"));
		for(String url : urls){
			rule.addPattern(url);
		}

		String[] requiredAuthenticators = StringUtil.split(
				configProps.getProperty("authenticators.required"));
		for(String authenticatorAlias : requiredAuthenticators){
			String authenticatorClass = PropsUtil.get(
				"portal.api.authentication.authenticators",
				new Filter(authenticatorAlias));

			PortalAuthenticator authenticatorObj =
				(PortalAuthenticator) InstancePool.get(authenticatorClass);

			config.getRequiredAuthenticators().add(authenticatorObj);
		}

		String[] optionalAuthenticators = StringUtil.split(
			configProps.getProperty("authenticators.optional"));
		for(String authenticatorAlias : optionalAuthenticators){
			String authenticatorClass = PropsUtil.get(
				"portal.api.authentication.authenticators",
				new Filter(authenticatorAlias));

			PortalAuthenticator authenticatorObj =
				(PortalAuthenticator) InstancePool.get(authenticatorClass);

			config.getOptionalAuthenticators().add(authenticatorObj);
		}

		boolean httpsRequired = GetterUtil.getBoolean(
			configProps.getProperty("https.required"));
		config.setSecure(httpsRequired);

		boolean remoteEnabled = GetterUtil.getBoolean(
			configProps.getProperty("remote"));
		config.setRemoteAccess(remoteEnabled);

		return rule;
	}
}
