/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.calendar.service;

import com.liferay.portal.kernel.calendar.model.CalendarResource;

import java.util.List;

/**
 * @author Josef Sustacek
 */
public interface ResourceService {

	/**
	 * Fetch calendar resource of given ID.
	 *
	 * @param calendarResourceId
	 * @return resource of given ID or null, when not existing
	 */
	public CalendarResource getCalendarResource(String calendarResourceId);

	/**
	 * Fetch all calendar resources known to this service instance.
	 * @return
	 */
	public List<CalendarResource> getCalendarResources();

	/**
	 * Fetch all calendar resources known to this service instance, in given
	 * range.
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public List<CalendarResource> getCalendarResources(int start, int end);

	/**
	 * Returns count of calendar resources known to this service instance.
	 * @return
	 */
	public int getCalendarResourcesCount();

	/**
	 * Search for resources based on given keywords.
	 *
	 * @param keywords the keywords that should be matched in the returned
	 *                 resources; matched in <code>name</code> or
	 *                 <code>description</code>
	 * @param andOperator use <code>true</code>, when all terms (words) in
	 *                       <code>keywords</code> have to be matched (AND); use
	 *                       <code>false</code>, when at least one of terms has
	 *                       to be matched (OR)
	 * @param start
	 * @param end
	 * @return
	 */
	public List<CalendarResource> searchByKeywords(
		String keywords, boolean andOperator, int start, int end);

	/**
	 * Search for resources matching given name and description.
	 *
	 * @param name the term that should be matched in name of the resource
	 * @param description the term that should be matched in description of
	 *                       the resource
	 * @param start
	 * @param end
	 * @return
	 */
	public List<CalendarResource> search(
		String name, String description, int start, int end);

	/**
	 * Returns count of matching results for given name and description terms.
	 *
	 * @param name
	 * @param description
	 * @return
	 */
	public int searchCount(String name, String description);

}
