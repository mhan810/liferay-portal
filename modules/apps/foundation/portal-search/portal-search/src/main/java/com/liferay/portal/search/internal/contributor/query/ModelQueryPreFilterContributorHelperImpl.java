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

import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.query.ModelQueryPreFilterContributorHelper;

import org.osgi.service.component.annotations.Component;

/**
 * @author Bryan Engler
 */
@Component(
	immediate = true, service = ModelQueryPreFilterContributorHelper.class
)
public class ModelQueryPreFilterContributorHelperImpl
	implements ModelQueryPreFilterContributorHelper {

	@Override
	public void addClassTypeIdsFilter(
		BooleanFilter fullQueryBooleanFilter, SearchContext searchContext) {

		long[] classTypeIds = searchContext.getClassTypeIds();

		if (ArrayUtil.isEmpty(classTypeIds)) {
			return;
		}

		TermsFilter termsFilter = new TermsFilter(Field.CLASS_TYPE_ID);

		termsFilter.addValues(ArrayUtil.toStringArray(classTypeIds));

		fullQueryBooleanFilter.add(termsFilter, BooleanClauseOccur.MUST);
	}

	public void addStagingFilter(
		BooleanFilter booleanFilter, SearchContext searchContext) {

		if (!searchContext.isIncludeLiveGroups() &&
			searchContext.isIncludeStagingGroups()) {

			booleanFilter.addRequiredTerm(Field.STAGING_GROUP, true);
		}
		else if (searchContext.isIncludeLiveGroups() &&
				 !searchContext.isIncludeStagingGroups()) {

			booleanFilter.addRequiredTerm(Field.STAGING_GROUP, false);
		}
	}

	@Override
	public void addWorkflowStatusesFilter(
		BooleanFilter booleanFilter, SearchContext searchContext) {

		int[] statuses = GetterUtil.getIntegerValues(
			searchContext.getAttribute(Field.STATUS), null);

		if (ArrayUtil.isEmpty(statuses)) {
			int status = GetterUtil.getInteger(
				searchContext.getAttribute(Field.STATUS),
				WorkflowConstants.STATUS_APPROVED);

			statuses = new int[] {status};
		}

		if (!ArrayUtil.contains(statuses, WorkflowConstants.STATUS_ANY)) {
			TermsFilter statusesTermsFilter = new TermsFilter(Field.STATUS);

			statusesTermsFilter.addValues(ArrayUtil.toStringArray(statuses));

			booleanFilter.add(statusesTermsFilter, BooleanClauseOccur.MUST);
		}
		else {
			booleanFilter.addTerm(
				Field.STATUS, String.valueOf(WorkflowConstants.STATUS_IN_TRASH),
				BooleanClauseOccur.MUST_NOT);
		}
	}

}