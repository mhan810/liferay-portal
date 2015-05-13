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

package com.liferay.portal.security.permission;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.model.User;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceTracker;

/**
 * @author Charles May
 * @author Brian Wing Shun Chan
 */
public class PermissionCheckerFactoryUtil {

	public static PermissionChecker create(User user) throws Exception {
		return getPermissionCheckerFactory().create(user);
	}

	/**
	 * @deprecated As of 6.2.0, replaced by {@link #create(User)}
	 */
	@Deprecated
	public static PermissionChecker create(User user, boolean checkGuest)
		throws Exception {

		return getPermissionCheckerFactory().create(user);
	}

	public static PermissionCheckerFactory getPermissionCheckerFactory() {
		PortalRuntimePermission.checkGetBeanProperty(
			PermissionCheckerFactoryUtil.class);

		return _instance._serviceTracker.getService();
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public void setPermissionCheckerFactory(
		PermissionCheckerFactory permissionCheckerFactory) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		if (_log.isWarnEnabled()) {
			_log.warn(
				"This method has been deprecated, " +
					"please register the factory using OSGi.");
		}
	}

	private PermissionCheckerFactoryUtil() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			PermissionCheckerFactory.class);

		_serviceTracker.open();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PermissionCheckerFactoryUtil.class);

	private static final PermissionCheckerFactoryUtil _instance =
		new PermissionCheckerFactoryUtil();

	private final ServiceTracker<?, PermissionCheckerFactory> _serviceTracker;

}