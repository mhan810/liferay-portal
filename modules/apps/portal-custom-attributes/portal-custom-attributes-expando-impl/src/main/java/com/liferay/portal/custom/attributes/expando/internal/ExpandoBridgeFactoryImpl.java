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

package com.liferay.portal.custom.attributes.expando.internal;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactory;
import com.liferay.portal.custom.attributes.CustomAttributesDataEngineAware;
import com.liferay.portlet.expando.model.impl.ExpandoBridgeImpl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Raymond Augé
 */
@Component(service = ExpandoBridgeFactory.class)
public class ExpandoBridgeFactoryImpl implements ExpandoBridgeFactory {

	@Override
	public ExpandoBridge getExpandoBridge(long companyId, String className) {
		if (!_dataEngineClassNames.containsKey(className)) {
			return new ExpandoBridgeImpl(companyId, className);
		}

		return new DataEngineExpandoBridgeImpl(companyId, className);
	}

	@Override
	public ExpandoBridge getExpandoBridge(
		long companyId, String className, long classPK) {

		if (!_dataEngineClassNames.containsKey(className)) {
			return new ExpandoBridgeImpl(companyId, className, classPK);
		}

		return new DataEngineExpandoBridgeImpl(companyId, className, classPK);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		unbind = "unsetCustomAttributesDataEngineAware")
	protected void setCustomAttributesDataEngineAware(
		CustomAttributesDataEngineAware customAttributesDataEngineAware) {

		_dataEngineClassNames.put(
			customAttributesDataEngineAware.getClassName(),
			customAttributesDataEngineAware.getClassName());
	}

	protected void unsetCustomAttributesDataEngineAware(
		CustomAttributesDataEngineAware customAttributesDataEngineAware) {

		_dataEngineClassNames.remove(
			customAttributesDataEngineAware.getClassName());
	}


	private Map<String, String> _dataEngineClassNames = new HashMap<>();
}