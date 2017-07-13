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

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.util.FacetFactory;
import com.liferay.portal.search.facet.asset.AssetEntriesFacet;
import com.liferay.portal.search.facet.asset.AssetEntriesFacetFactory;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

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
		return _assetEntriesFacetPrototype.newInstance(searchContext);
	}

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private AssetEntriesFacet _assetEntriesFacetPrototype;

}