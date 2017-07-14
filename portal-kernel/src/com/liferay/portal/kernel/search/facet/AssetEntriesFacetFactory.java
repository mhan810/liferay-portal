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

package com.liferay.portal.kernel.search.facet;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.util.FacetFactory;
import com.liferay.portal.kernel.search.facet.util.FacetFactoryUtil;

/**
 * @author Raymond Augé
 * @deprecated As of 7.0.0, replaced by
 *      com.liferay.portal.search.facet.asset.AssetEntriesFacetFactory
 */
@Deprecated
public class AssetEntriesFacetFactory implements FacetFactory {

	@Override
	public String getFacetClassName() {
		return _ASSET_ENTRIES_FACET_CLASS_NAME;
	}

	@Override
	public Facet newInstance(SearchContext searchContext) {
		Facet facet = null;

		try {
			facet = FacetFactoryUtil.create(
				_ASSET_ENTRIES_FACET_CLASS_NAME, searchContext);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}

		if (facet == null) {
			throw new IllegalStateException(
				"No facet defined for: " + _ASSET_ENTRIES_FACET_CLASS_NAME);
		}

		return facet;
	}

	private static final String _ASSET_ENTRIES_FACET_CLASS_NAME =
		"com.liferay.portal.search.facet.asset.AssetEntriesFacet";

}