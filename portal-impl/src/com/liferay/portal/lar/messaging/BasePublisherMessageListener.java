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

package com.liferay.portal.lar.messaging;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.messaging.BaseMessageStatusMessageListener;
import com.liferay.portal.kernel.messaging.sender.SingleDestinationMessageSender;
import com.liferay.portal.kernel.messaging.sender.SingleDestinationMessageSenderFactory;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Levente Hudák
 */
public abstract class BasePublisherMessageListener
	extends BaseMessageStatusMessageListener {

	public void afterPropertiesSet() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			SingleDestinationMessageSenderFactory.class,
			new SingleDestinationMessageSenderFactoryServiceTrackerCustomizer()
		);

		_serviceTracker.open();
	}

	public void destroy() {
		_serviceTracker.close();
	}

	public void setDestinationName(String destinationName) {
		_destinationName = destinationName;
	}

	protected void initializeSingleDestinationSender(
		SingleDestinationMessageSenderFactory
			singleDestinationMessageSenderFactory) {

		SingleDestinationMessageSender singleDestinationMessageSender =
			singleDestinationMessageSenderFactory.
				createSingleDestinationMessageSender(_destinationName);

		setStatusSender(singleDestinationMessageSender);
	}

	protected void initThreadLocals(
			long userId, Map<String, String[]> parameterMap)
		throws PortalException {

		User user = UserLocalServiceUtil.getUserById(userId);

		CompanyThreadLocal.setCompanyId(user.getCompanyId());

		PrincipalThreadLocal.setName(userId);

		PermissionChecker permissionChecker = null;

		try {
			permissionChecker = PermissionCheckerFactoryUtil.create(user);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}

		PermissionThreadLocal.setPermissionChecker(permissionChecker);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCompanyId(user.getCompanyId());
		serviceContext.setPathMain(PortalUtil.getPathMain());
		serviceContext.setSignedIn(!user.isDefaultUser());
		serviceContext.setUserId(user.getUserId());

		Map<String, Serializable> attributes = new HashMap<>();

		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String param = entry.getKey();
			String[] values = entry.getValue();

			if (ArrayUtil.isNotEmpty(values)) {
				if (values.length == 1) {
					attributes.put(param, values[0]);
				}
				else {
					attributes.put(param, values);
				}
			}
		}

		serviceContext.setAttributes(attributes);

		ServiceContextThreadLocal.pushServiceContext(serviceContext);
	}

	protected void resetThreadLocals() {
		CompanyThreadLocal.setCompanyId(CompanyConstants.SYSTEM);
		PermissionThreadLocal.setPermissionChecker(null);
		PrincipalThreadLocal.setName(null);
		ServiceContextThreadLocal.popServiceContext();
	}

	private String _destinationName;
	private ServiceTracker<SingleDestinationMessageSenderFactory,
					SingleDestinationMessageSenderFactory> _serviceTracker;

	private class SingleDestinationMessageSenderFactoryServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<SingleDestinationMessageSenderFactory,
				SingleDestinationMessageSenderFactory> {

		@Override
		public SingleDestinationMessageSenderFactory addingService(
			ServiceReference<SingleDestinationMessageSenderFactory>
				serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			SingleDestinationMessageSenderFactory
				singleDestinationMessageSenderFactory = registry.getService(
					serviceReference);

			initializeSingleDestinationSender(
				singleDestinationMessageSenderFactory);

			return singleDestinationMessageSenderFactory;
		}

		@Override
		public void modifiedService(
			ServiceReference<SingleDestinationMessageSenderFactory>
				serviceReference,
			SingleDestinationMessageSenderFactory
				singleDestinationMessageSenderFactory) {
		}

		@Override
		public void removedService(
			ServiceReference<SingleDestinationMessageSenderFactory>
				serviceReference,
			SingleDestinationMessageSenderFactory
				singleDestinationMessageSenderFactory) {
		}

	}

}