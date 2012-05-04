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

/**
 * Result of the authentication. {@link PortalAuthenticator}s should set at
 * least:<ul>
 *     <li>{@link #setState(AuthenticationResult.State)} indicating the
 *     result state</li>
 *     <li>{@link #setUserId(long)} when user is authenticated - state is
 *     {@link State#SUCCESS}</li>
 * </ul>
 * @author Tomas Polesovsky
 */
public class AuthenticationResult {

	public String getPassword() {
		return _password;
	}

	public State getState(){
		return _state;
	}

	public long getUserId(){
		return _userId;
	}

	public void setPassword(String password) {
		this._password = password;
	}

	public void setState(State state){
		this._state = state;
	}
	public void setUserId(long userId) {
		this._userId = userId;
	}

	public enum State {
		SUCCESS, IN_PROGRESS, INVALID_CREDENTIALS
	}

	private String _password;

	private State _state = State.INVALID_CREDENTIALS;
	private long _userId = 0;
}
