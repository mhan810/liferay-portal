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

package com.liferay.calendar.internal.search.contributor.model;

import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarBookingLocalService;
import com.liferay.calendar.workflow.CalendarBookingWorkflowConstants;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.search.contributor.model.IndexerWriterMode;
import com.liferay.portal.search.contributor.model.ModelIndexerWriterContributor;
import com.liferay.portal.search.indexer.IndexerDocumentBuilder;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {
		"indexer.class.name=com.liferay.calendar.model.CalendarBooking"
	},
	service = ModelIndexerWriterContributor.class
)
public class CalendarBookingModelIndexerWriterContributor
	implements ModelIndexerWriterContributor<CalendarBooking> {

	@Override
	public void customize(
		final IndexableActionableDynamicQuery indexableActionableDynamicQuery,
		final IndexerDocumentBuilder<CalendarBooking> indexerDocumentBuilder,
		long companyId) {

		indexableActionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				Property statusProperty = PropertyFactoryUtil.forName("status");

				int[] statuses = {
					CalendarBookingWorkflowConstants.STATUS_APPROVED,
					CalendarBookingWorkflowConstants.STATUS_MAYBE
				};

				dynamicQuery.add(statusProperty.in(statuses));
			});

		indexableActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<CalendarBooking>() {

				@Override
				public void performAction(CalendarBooking calendarBooking)
					throws PortalException {

					try {
						Document document = indexerDocumentBuilder.getDocument(
							calendarBooking);

						indexableActionableDynamicQuery.addDocuments(document);
					}
					catch (PortalException pe) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to index calendar booking " +
									calendarBooking.getCalendarBookingId(),
								pe);
						}
					}
				}

			});
	}

	@Override
	public Optional<CalendarBooking> getBaseModel(long classPK) {
		CalendarBooking calendarBooking =
			calendarBookingLocalService.fetchCalendarBooking(classPK);

		return Optional.ofNullable(calendarBooking);
	}

	@Override
	public long getCompanyId(CalendarBooking calendarBooking) {
		return calendarBooking.getCompanyId();
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return calendarBookingLocalService.getIndexableActionableDynamicQuery();
	}

	@Override
	public IndexerWriterMode getIndexerWriterMode(
		CalendarBooking calendarBooking) {

		int status = calendarBooking.getStatus();

		if ((status == CalendarBookingWorkflowConstants.STATUS_APPROVED) ||
			(status == CalendarBookingWorkflowConstants.STATUS_MAYBE)) {

			return IndexerWriterMode.UPDATE;
		}

		return IndexerWriterMode.DELETE;
	}

	@Reference
	protected CalendarBookingLocalService calendarBookingLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		CalendarBookingModelIndexerWriterContributor.class);

}