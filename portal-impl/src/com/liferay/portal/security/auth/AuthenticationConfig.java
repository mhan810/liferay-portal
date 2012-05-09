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

package com.liferay.portal.security.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Tomas Polesovsky
 */

public class AuthenticationConfig {

	public List<PortalAuthenticator> getOptionalAuthenticators(){
		return _optionalAuthenticators;
	}

	public List<PortalAuthenticator> getRequiredAuthenticators(){
		return _requiredAuthenticators;
	}

	public Properties getSettings(){
		return this._settings;
	}

	public boolean isRemoteAccess() {
		return _remoteAccess;
	}

	public boolean isSecure(){
		return _secure;
	}

	public void setRemoteAccess(boolean remoteAccess) {
		this._remoteAccess = remoteAccess;
	}

	public void setSecure(boolean secure){
		this._secure = secure;
	}

	public void setSettings(Properties settings){
		this._settings = settings;
	}

	private List<PortalAuthenticator> _optionalAuthenticators
		= new ArrayList<PortalAuthenticator>();
	private boolean _remoteAccess;
	private List<PortalAuthenticator> _requiredAuthenticators
		= new ArrayList<PortalAuthenticator>();
	private boolean _secure;
	private Properties _settings;

}
