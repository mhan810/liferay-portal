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

package com.liferay.dynamic.data.mapping.form.field.type.internal.grid;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueRequestParameterRetriever;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pedro Queiroz
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=grid",
	service = DDMFormFieldValueRequestParameterRetriever.class
)
public class GridDDMFormFieldValueRequestParameterRetriever
	implements DDMFormFieldValueRequestParameterRetriever {

	@Override
	public String get(
		HttpServletRequest httpServletRequest, String ddmFormFieldParameterName,
		String defaultDDMFormFieldParameterValue) {

		JSONObject jsonObject = jsonFactory.createJSONObject();

		try {
			jsonObject = jsonFactory.createJSONObject(
				httpServletRequest.getParameter(ddmFormFieldParameterName));
		}
		catch (JSONException jsonException) {
			Map<String, String[]> parameterMap =
				httpServletRequest.getParameterMap();

			if (!parameterMap.containsKey(ddmFormFieldParameterName)) {
				return jsonObject.toString();
			}

			String[] parameterValues = parameterMap.get(
				ddmFormFieldParameterName);

			for (String parameterValue : parameterValues) {
				if (!parameterValue.isEmpty()) {
					String[] parameterValueParts = parameterValue.split(";");

					jsonObject.put(
						parameterValueParts[0], parameterValueParts[1]);
				}
			}
		}

		return jsonObject.toString();
	}

	@Reference
	protected JSONFactory jsonFactory;

}