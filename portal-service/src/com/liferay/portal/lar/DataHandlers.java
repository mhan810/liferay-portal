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

package com.liferay.portal.lar;

import com.liferay.portal.service.persistence.BaseDataHandler;

import java.util.Map;

/**
 * @author Mate Thurzo
 */
public interface DataHandlers {

	public void addDataHandlerMapping(String key, String dataHandlerClass);

	public String getDataHandlerClass(String key);

	public BaseDataHandler getDataHandlerInstance(String key);

	public Map<String, String> getDataHandlerMapping();

	public void read(ClassLoader classLoader, String source) throws Exception;

}