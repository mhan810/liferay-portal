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

package com.liferay.portal.kernel.nio.intraband;

import com.liferay.portal.kernel.executor.PortalExecutorManager;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.concurrent.Executor;

/**
 * @author Shuyang Zhou
 */
public abstract class BaseAsyncDatagramReceiveHandler
	implements DatagramReceiveHandler {

	public BaseAsyncDatagramReceiveHandler() {
		Class<? extends BaseAsyncDatagramReceiveHandler> clazz = getClass();

		_className = clazz.getName();

		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			PortalExecutorManager.class,
			new PortalExecutorManagerServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	@Override
	public void receive(
		RegistrationReference registrationReference, Datagram datagram) {

		if (_executor == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("" +
					"No executor to dispatch job: " + registrationReference +
					" " + datagram);
			}

			return;
		}

		_executor.execute(new DispatchJob(registrationReference, datagram));
	}

	protected abstract void doReceive(
			RegistrationReference registrationReference, Datagram datagram)
		throws Exception;

	private static final Log _log = LogFactoryUtil.getLog(
		BaseAsyncDatagramReceiveHandler.class);

	private final String _className;
	private Executor _executor;
	private final ServiceTracker<PortalExecutorManager, PortalExecutorManager>
		_serviceTracker;

	private class DispatchJob implements Runnable {

		public DispatchJob(
			RegistrationReference registrationReference, Datagram datagram) {

			_registrationReference = registrationReference;
			_datagram = datagram;
		}

		@Override
		public void run() {
			try {
				doReceive(_registrationReference, _datagram);
			}
			catch (Exception e) {
				_log.error("Unable to dispatch", e);
			}
		}

		private final Datagram _datagram;
		private final RegistrationReference _registrationReference;

	}

	private class PortalExecutorManagerServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<PortalExecutorManager, PortalExecutorManager> {

		@Override
		public PortalExecutorManager addingService(
			ServiceReference<PortalExecutorManager> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			PortalExecutorManager portalExecutorManager = registry.getService(
				serviceReference);

			_executor = portalExecutorManager.getPortalExecutor(_className);

			return portalExecutorManager;
		}

		@Override
		public void modifiedService(
			ServiceReference<PortalExecutorManager> serviceReference,
			PortalExecutorManager service) {
		}

		@Override
		public void removedService(
			ServiceReference<PortalExecutorManager> serviceReference,
			PortalExecutorManager service) {
		}

	}

}