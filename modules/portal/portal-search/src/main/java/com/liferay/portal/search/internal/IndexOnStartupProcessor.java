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

package com.liferay.portal.search.internal;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.UserConstants;
import com.liferay.portal.util.PortalInstances;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 */
public class IndexOnStartupProcessor {

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY, unbind = "unregister"
	)
	public void register(Indexer<?> indexer) {
		if (!GetterUtil.getBoolean(_props.get(PropsKeys.INDEX_ON_STARTUP))) {
			return;
		}

		String className = indexer.getClassName();

		if (Validator.isNull(className)) {
			return;
		}

		try {
			_indexWriterHelper.reindex(
				UserConstants.USER_ID_DEFAULT, "reindexOnActivate",
				PortalInstances.getCompanyIds(), className, null);
		}
		catch (SearchException se) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to reindex on activation", se);
			}
		}
	}

	public void unregister(Indexer<?> indexer) {
	}

	@Reference(unbind = "-")
	protected void setIndexWriterHelper(IndexWriterHelper indexWriterHelper) {
		_indexWriterHelper = indexWriterHelper;
	}

	@Reference(unbind = "-")
	protected void setProps(Props props) {
		_props = props;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IndexOnStartupProcessor.class);

	private IndexWriterHelper _indexWriterHelper;
	private Props _props;

}