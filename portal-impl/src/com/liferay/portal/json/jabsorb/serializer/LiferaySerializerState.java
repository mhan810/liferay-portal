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

package com.liferay.portal.json.jabsorb.serializer;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import org.jabsorb.serializer.ProcessedObject;
import org.jabsorb.serializer.SerializerState;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Tomas Polesovsky
 */
public class LiferaySerializerState extends SerializerState {

	@Override
	public ProcessedObject store(Object obj) {

		if(obj instanceof JSONObject &&
			((JSONObject) obj).has("javaClass")){

			try {
				String javaClass = ((JSONObject) obj).getString("javaClass");

				if(javaClass.contains("com.liferay") &&
						javaClass.contains("Util")) {

					throw new RuntimeException("Not instantiating " + javaClass);
				}
			} catch(JSONException ex){
				_log.error(ex);
			}
		}

		return super.store(obj);
	}

	private static Log _log = LogFactoryUtil.getLog(LiferaySerializerState.class);
}
