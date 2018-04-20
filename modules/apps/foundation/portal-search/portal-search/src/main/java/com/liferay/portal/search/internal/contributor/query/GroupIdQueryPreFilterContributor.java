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

package com.liferay.portal.search.internal.contributor.query;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.spi.model.query.contributor.QueryPreFilterContributor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = QueryPreFilterContributor.class)
public class GroupIdQueryPreFilterContributor
	implements QueryPreFilterContributor {

	@Override
	public void contribute(
		BooleanFilter fullQueryBooleanFilter, SearchContext searchContext) {

		long[] groupIds = searchContext.getGroupIds();

		if (ArrayUtil.isEmpty(groupIds) ||
			((groupIds.length == 1) && (groupIds[0] == 0))) {

			return;
		}

		long ownerUserId = searchContext.getOwnerUserId();

		if (ownerUserId > 0) {
			fullQueryBooleanFilter.addRequiredTerm(Field.USER_ID, ownerUserId);
		}

		TermsFilter groupIdsTermsFilter = new TermsFilter(Field.GROUP_ID);
		TermsFilter scopeGroupIdsTermsFilter = new TermsFilter(
			Field.SCOPE_GROUP_ID);

		for (long groupId : groupIds) {
			if (groupId <= 0) {
				continue;
			}

			try {
				Group group = groupLocalService.getGroup(groupId);

				if (!groupLocalService.isLiveGroupActive(group)) {
					continue;
				}

				if (group.isLayout()) {
					groupIdsTermsFilter.addValue(
						String.valueOf(group.getParentGroupId()));
				}
				else {
					groupIdsTermsFilter.addValue(String.valueOf(groupId));
				}

				if (group.isLayout() || searchContext.isScopeStrict()) {
					scopeGroupIdsTermsFilter.addValue(String.valueOf(groupId));
				}
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}
			}
		}

		if (!groupIdsTermsFilter.isEmpty()) {
			fullQueryBooleanFilter.add(
				groupIdsTermsFilter, BooleanClauseOccur.MUST);
		}

		if (!scopeGroupIdsTermsFilter.isEmpty()) {
			fullQueryBooleanFilter.add(
				scopeGroupIdsTermsFilter, BooleanClauseOccur.MUST);
		}
	}

	@Reference
	protected GroupLocalService groupLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		GroupIdQueryPreFilterContributor.class);

}