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

package com.liferay.portal.kernel.lar.messaging;

import com.liferay.portal.kernel.lar.PortletDataHandler;

/**
 * @author Mate Thurzo
 */
public interface ExportImportMessageSender {

	public boolean isDebugEnabled();

	public boolean isErrorEnabled();

	public boolean isInfoEnabled();

	public void send();

	public void send(
		String action, PortletDataHandler portletDataHandler, long timestamp);

}