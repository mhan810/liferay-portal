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

package com.liferay.portal.search.internal.facet.asset;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseFactory;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.util.FacetFactory;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.facet.asset.AssetEntriesFacet;
import com.liferay.portal.search.facet.asset.AssetEntriesFacetBooleanFilterBuilder;
import com.liferay.portal.search.facet.asset.AssetEntriesFacetFactory;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	service = {AssetEntriesFacetFactory.class, FacetFactory.class}
)
public class AssetEntriesFacetFactoryImpl
	implements AssetEntriesFacetFactory, FacetFactory {

	@Override
	public String getFacetClassName() {
		return AssetEntriesFacet.class.getName();
	}

	@Override
	public Facet newInstance(SearchContext searchContext) {
		return new AssetEntriesFacetImpl(
			searchContext, _assetEntriesFacetBooleanFilterBuilders,
			_booleanClauseFactory,
			_defaultAssetEntriesFacetBooleanFilterBuilder, _indexerRegistry,
			_jsonFactory, _searchEngineHelper);
	}

	protected void removeAssetEntriesFacetBooleanFilterBuilder(
		AssetEntriesFacetBooleanFilterBuilder
			assetEntriesFacetBooleanFilterBuilder,
		Map<String, Object> properties) {

		String entryClassName = (String)properties.get("entry.class.name");

		if (Validator.isNull(entryClassName)) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"No entry.class.name property specified for : " +
						assetEntriesFacetBooleanFilterBuilder);
			}

			return;
		}

		_assetEntriesFacetBooleanFilterBuilders.put(
			entryClassName, assetEntriesFacetBooleanFilterBuilder);
	}


	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(!(entry.class.name=default))",
		unbind = "removeAssetEntriesFacetBooleanFilterBuilder"
	)
	protected void setAssetEntriesFacetBooleanFilterBuilder(
		AssetEntriesFacetBooleanFilterBuilder
			assetEntriesFacetBooleanFilterBuilder,
		Map<String, Object> properties) {

		String entryClassName = (String)properties.get("entry.class.name");

		if (Validator.isNull(entryClassName)) {
			throw new IllegalStateException(
				"No entry.class.name property specified for : " +
					assetEntriesFacetBooleanFilterBuilder);
		}

		_assetEntriesFacetBooleanFilterBuilders.put(
			entryClassName, assetEntriesFacetBooleanFilterBuilder);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AssetEntriesFacetFactoryImpl.class);

	private Map<String, AssetEntriesFacetBooleanFilterBuilder>
		_assetEntriesFacetBooleanFilterBuilders = new ConcurrentHashMap<>();

	@Reference
	private BooleanClauseFactory _booleanClauseFactory;

	@Reference(
		cardinality = ReferenceCardinality.MANDATORY,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(entry.class.name=default)"
	)
	private volatile AssetEntriesFacetBooleanFilterBuilder
		_defaultAssetEntriesFacetBooleanFilterBuilder;

	@Reference
	private IndexerRegistry _indexerRegistry;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private SearchEngineHelper _searchEngineHelper;


}