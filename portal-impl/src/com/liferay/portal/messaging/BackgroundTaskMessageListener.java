/*
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

package com.liferay.portal.messaging;

import com.liferay.portal.kernel.executor.BackgroundTaskExecutor;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.service.BackgroundTaskLocalServiceUtil;
import com.liferay.portal.util.ClassLoaderUtil;

/**
 * @author Michael C. Han
 */
public class BackgroundTaskMessageListener extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {

		long backgroundTaskId = (Long)message.get("backgroundTaskId");

		BackgroundTask backgroundTask =
			BackgroundTaskLocalServiceUtil.getBackgroundTask(backgroundTaskId);

		String backgroundTaskExecutorName = backgroundTask.getClassName();
		String servletContextName = backgroundTask.getServletContextName();

		ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();

		if (Validator.isNotNull(servletContextName)) {
			classLoader = ClassLoaderUtil.getPluginClassLoader(
				servletContextName);
		}

		BackgroundTaskExecutor backgroundTaskExecutor =
			(BackgroundTaskExecutor)InstanceFactory.newInstance(
				classLoader, backgroundTaskExecutorName);

		backgroundTaskExecutor.execute(backgroundTask);
	}

}
