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

package com.liferay.portal.search.internal.query;

import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.MatchQuery;
import com.liferay.portal.kernel.search.generic.QueryTermImpl;
import com.liferay.portal.kernel.search.generic.WildcardQueryImpl;
import com.liferay.portal.kernel.search.query.FieldQueryFactory;
import com.liferay.portal.kernel.search.query.QueryPreProcessConfiguration;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = FieldQueryFactory.class)
public class FieldQueryFactoryImpl implements FieldQueryFactory {

	@Override
	public Query createQuery(
		String field, String value, boolean like, boolean splitKeywords) {

		if (splitKeywords) {
			String[] splitValues = StringUtil.split(value, CharPool.SPACE);

			if (splitValues.length == 1) {
				return createQuery(field, splitValues[0], like, false);
			}

			BooleanQueryImpl booleanQuery = new BooleanQueryImpl();

			for (String splitValue : splitValues) {
				Query query = createQuery(field, splitValue, like, false);

				booleanQuery.add(query, BooleanClauseOccur.SHOULD);
			}

			return booleanQuery;
		}

		boolean isSubstringSearchAlways = false;

		if (_queryPreProcessConfiguration != null) {
			isSubstringSearchAlways =
				_queryPreProcessConfiguration.isSubstringSearchAlways(field);
		}

		Query query = null;

		if (like || isSubstringSearchAlways) {
			value = StringUtil.replace(
				value, StringPool.PERCENT, StringPool.BLANK);

			if (value.startsWith(StringPool.QUOTE)) {
				value = value.substring(1);

				if (value.contains(StringPool.QUOTE)) {
					int pos = value.indexOf(StringPool.QUOTE);

					value = value.substring(0, pos);
				}
			}

			if (isSubstringSearchAlways) {
				if (value.length() == 0) {
					value = StringPool.STAR;
				}
				else {
					value = StringUtil.toLowerCase(value);

					value = StringPool.STAR + value + StringPool.STAR;
				}
			}

			query = new WildcardQueryImpl(new QueryTermImpl(field, value));
		}
		else {
			MatchQuery matchQuery = new MatchQuery(field, value);

			if (value.contains(StringPool.SPACE)) {
				matchQuery.setType(MatchQuery.Type.PHRASE);
			}
		}

		return query;
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void setQueryPreProcessConfiguration(
		QueryPreProcessConfiguration queryPreProcessConfiguration) {

		_queryPreProcessConfiguration = queryPreProcessConfiguration;
	}

	protected void unsetQueryPreProcessConfiguration(
		QueryPreProcessConfiguration queryPreProcessConfiguration) {

		_queryPreProcessConfiguration = null;
	}

	private QueryPreProcessConfiguration _queryPreProcessConfiguration;

}