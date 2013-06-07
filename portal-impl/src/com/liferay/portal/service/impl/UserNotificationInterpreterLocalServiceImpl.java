/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.notifications.UserNotificationFeedEntry;
import com.liferay.portal.kernel.notifications.UserNotificationInterpreter;
import com.liferay.portal.model.UserNotificationEvent;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.base.UserNotificationInterpreterLocalServiceBaseImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jonathan Lee
 * @author Brian Wing Shun Chan
 */
public class UserNotificationInterpreterLocalServiceImpl
	extends UserNotificationInterpreterLocalServiceBaseImpl {

	/**
	 * Adds the use notification interpreter to the list of available
	 * interpreters.
	 *
	 * @param userNotificationInterpreter the user notification interpreter
	 */
	@Override
	public void addUserNotificationInterpreter(
		UserNotificationInterpreter userNotificationInterpreter) {

		List<UserNotificationInterpreter> userNotificationInterpreters =
			_userNotificationInterpreters.get(
				userNotificationInterpreter.getSelector());

		if (userNotificationInterpreters == null) {
			userNotificationInterpreters =
				new ArrayList<UserNotificationInterpreter>();
		}

		userNotificationInterpreters.add(userNotificationInterpreter);

		_userNotificationInterpreters.put(
			userNotificationInterpreter.getSelector(),
			userNotificationInterpreters);
	}

	/**
	 * Removes the user notification interpreter from the list of available
	 * interpreters.
	 *
	 * @param userNotificationInterpreter the user notification interpreter
	 */
	@Override
	public void deleteUserNotificationInterpreter(
		UserNotificationInterpreter userNotificationInterpreter) {

		List<UserNotificationInterpreter> userNotificationInterpreters =
			_userNotificationInterpreters.get(
				userNotificationInterpreter.getSelector());

		if (userNotificationInterpreters == null) {
			return;
		}

		userNotificationInterpreters.remove(userNotificationInterpreter);
	}

	@Override
	public Map<String, List<UserNotificationInterpreter>>
		getUserNotificationInterpreters() {

		return _userNotificationInterpreters;
	}

	/**
	 * Creates a human readable user notification feed entry for the user
	 * notification using an available compatible user notification interpreter.
	 *
	 * <p>
	 * This method finds the appropriate interpreter for the user notification
	 * by going through the available interpreters and asking them if they can
	 * handle the user notidication based on the portlet.
	 * </p>
	 *
	 * @param  userNotificationEvent the user notification event to be
	 *         translated to human readable form
	 * @return the user notification feed that is a human readable form of the
	 *         user notification record or <code>null</code> if a compatible
	 *         interpreter is not found
	 */
	@Override
	public UserNotificationFeedEntry interpret(
		String selector, UserNotificationEvent userNotificationEvent,
		ServiceContext serviceContext) {

		HttpServletRequest request = serviceContext.getRequest();

		if (request == null) {
			return null;
		}

		List<UserNotificationInterpreter> userNotificationInterpreters =
			_userNotificationInterpreters.get(selector);

		if (userNotificationInterpreters == null) {
			return null;
		}

		String portletId = userNotificationEvent.getType();

		for (int i = 0; i < userNotificationInterpreters.size(); i++) {
			UserNotificationInterpreterImpl userNotificationInterpreter =
				(UserNotificationInterpreterImpl)
					userNotificationInterpreters.get(i);

			if (portletId.equals(userNotificationInterpreter.getPortletId())) {
				UserNotificationFeedEntry userNotificationFeedEntry =
					userNotificationInterpreter.interpret(
						userNotificationEvent, serviceContext);

				if (userNotificationFeedEntry != null) {
					userNotificationFeedEntry.setPortletId(
						userNotificationInterpreter.getPortletId());

					return userNotificationFeedEntry;
				}
			}
		}

		return null;
	}

	private Map<String, List<UserNotificationInterpreter>>
		_userNotificationInterpreters =
			new HashMap<String, List<UserNotificationInterpreter>>();

}