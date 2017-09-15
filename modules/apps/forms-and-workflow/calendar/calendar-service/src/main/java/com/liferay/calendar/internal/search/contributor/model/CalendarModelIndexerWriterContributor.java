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

import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarBookingLocalService;
import com.liferay.calendar.service.CalendarLocalService;
import com.liferay.calendar.workflow.CalendarBookingWorkflowConstants;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.search.contributor.model.ModelIndexerWriterContributor;
import com.liferay.portal.search.contributor.model.ModelSearchConfigurator;
import com.liferay.portal.search.contributor.model.ModelSearchSettings;
import com.liferay.portal.search.indexer.IndexerDocumentBuilder;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {"indexer.class.name=com.liferay.calendar.model.Calendar"},
	service = ModelIndexerWriterContributor.class
)
public class CalendarModelIndexerWriterContributor
	implements ModelIndexerWriterContributor<Calendar> {

	@Override
	public void customize(
		IndexableActionableDynamicQuery indexableActionableDynamicQuery,
		IndexerDocumentBuilder<Calendar> indexerDocumentBuilder,
		long companyId) {

		indexableActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<Calendar>() {

				@Override
				public void performAction(Calendar calendar) {
					try {
						Document document = indexerDocumentBuilder.getDocument(
							calendar);

						indexableActionableDynamicQuery.addDocuments(document);
					}
					catch (PortalException pe) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to index calendar " +
									calendar.getCalendarId(),
								pe);
						}
					}
				}

			});
	}

	@Override
	public Optional<Calendar> getBaseModel(long classPK) {
		Calendar calendar = calendarLocalService.fetchCalendar(classPK);

		return Optional.ofNullable(calendar);
	}

	@Override
	public long getCompanyId(Calendar calendar) {
		return calendar.getCompanyId();
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return calendarLocalService.getIndexableActionableDynamicQuery();
	}

	@Override
	public void modelIndexed(Calendar calendar) {
		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			calendarBookingLocalService.getIndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				Property calendarIdPropery = PropertyFactoryUtil.forName(
					"calendarId");

				dynamicQuery.add(
					calendarIdPropery.eq(calendar.getCalendarId()));

				Property statusProperty = PropertyFactoryUtil.forName("status");

				int[] statuses = {
					CalendarBookingWorkflowConstants.STATUS_APPROVED,
					CalendarBookingWorkflowConstants.STATUS_MAYBE
				};

				dynamicQuery.add(statusProperty.in(statuses));
			});

		final ModelSearchSettings calendarBookingModelSearchSettings =
			calendarBookingModelSearchConfigurator.getModelSearchSettings();

		indexableActionableDynamicQuery.setCompanyId(calendar.getCompanyId());
		indexableActionableDynamicQuery.setSearchEngineId(
			calendarBookingModelSearchSettings.getSearchEngineId());

		indexableActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<CalendarBooking>() {

				@Override
				public void performAction(CalendarBooking calendarBooking) {
					try {
						Document document =
							calendarBookingIndexerDocumentBuilder.getDocument(
								calendarBooking);

						indexableActionableDynamicQuery.addDocuments(document);
					}
					catch (PortalException pe) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to index calendar " +
									calendar.getCalendarId(),
								pe);
						}
					}
				}

			});

		try {
			indexableActionableDynamicQuery.performActions();
		}
		catch (PortalException pe) {
			if (_log.isWarnEnabled()) {
				StringBundler sb = new StringBundler(4);

				sb.append("Error reindexing all ");
				sb.append(calendarBookingModelSearchSettings.getClassName());
				sb.append(" for company: ");
				sb.append(calendar.getCompanyId());

				_log.warn(sb.toString(), pe);
			}
		}
	}

	@Reference(
		target = "(indexer.class.name=com.liferay.calendar.model.CalendarBooking)"
	)
	protected IndexerDocumentBuilder<CalendarBooking>
		calendarBookingIndexerDocumentBuilder;

	@Reference
	protected CalendarBookingLocalService calendarBookingLocalService;

	@Reference(
		target = "(indexer.class.name=com.liferay.calendar.model.CalendarBooking)"
	)
	protected ModelSearchConfigurator<CalendarBooking>
		calendarBookingModelSearchConfigurator;

	@Reference
	protected CalendarLocalService calendarLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		CalendarModelIndexerWriterContributor.class);

}