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

package com.liferay.asset.kernel.service;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;

/**
 * Provides the remote service utility for AssetVocabulary. This utility wraps
 * <code>com.liferay.portlet.asset.service.impl.AssetVocabularyServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see AssetVocabularyService
 * @generated
 */
public class AssetVocabularyServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portlet.asset.service.impl.AssetVocabularyServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.asset.kernel.model.AssetVocabulary addVocabulary(
			long groupId, String title,
			java.util.Map<java.util.Locale, String> titleMap,
			java.util.Map<java.util.Locale, String> descriptionMap,
			String settings, int visibilityType,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addVocabulary(
			groupId, title, titleMap, descriptionMap, settings, visibilityType,
			serviceContext);
	}

	public static com.liferay.asset.kernel.model.AssetVocabulary addVocabulary(
			long groupId, String title,
			java.util.Map<java.util.Locale, String> titleMap,
			java.util.Map<java.util.Locale, String> descriptionMap,
			String settings,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addVocabulary(
			groupId, title, titleMap, descriptionMap, settings, serviceContext);
	}

	public static com.liferay.asset.kernel.model.AssetVocabulary addVocabulary(
			long groupId, String title,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addVocabulary(groupId, title, serviceContext);
	}

	public static com.liferay.asset.kernel.model.AssetVocabulary addVocabulary(
			long groupId, String name, String title,
			java.util.Map<java.util.Locale, String> titleMap,
			java.util.Map<java.util.Locale, String> descriptionMap,
			String settings,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addVocabulary(
			groupId, name, title, titleMap, descriptionMap, settings,
			serviceContext);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
			deleteVocabularies(
				long[] vocabularyIds,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteVocabularies(vocabularyIds, serviceContext);
	}

	public static void deleteVocabulary(long vocabularyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteVocabulary(vocabularyId);
	}

	public static com.liferay.asset.kernel.model.AssetVocabulary
			fetchVocabulary(long vocabularyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().fetchVocabulary(vocabularyId);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
		getGroupsVocabularies(long[] groupIds) {

		return getService().getGroupsVocabularies(groupIds);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
		getGroupsVocabularies(long[] groupIds, String className) {

		return getService().getGroupsVocabularies(groupIds, className);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
		getGroupsVocabularies(
			long[] groupIds, String className, long classTypePK) {

		return getService().getGroupsVocabularies(
			groupIds, className, classTypePK);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
			getGroupVocabularies(long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getGroupVocabularies(groupId);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
			getGroupVocabularies(long groupId, boolean createDefaultVocabulary)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getGroupVocabularies(
			groupId, createDefaultVocabulary);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
			getGroupVocabularies(
				long groupId, boolean createDefaultVocabulary, int start,
				int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.asset.kernel.model.AssetVocabulary>
						orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getGroupVocabularies(
			groupId, createDefaultVocabulary, start, end, orderByComparator);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
		getGroupVocabularies(long groupId, int visibilityType) {

		return getService().getGroupVocabularies(groupId, visibilityType);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
		getGroupVocabularies(
			long groupId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.asset.kernel.model.AssetVocabulary>
					orderByComparator) {

		return getService().getGroupVocabularies(
			groupId, start, end, orderByComparator);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
		getGroupVocabularies(
			long groupId, String name, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.asset.kernel.model.AssetVocabulary>
					orderByComparator) {

		return getService().getGroupVocabularies(
			groupId, name, start, end, orderByComparator);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
		getGroupVocabularies(long[] groupIds) {

		return getService().getGroupVocabularies(groupIds);
	}

	public static java.util.List<com.liferay.asset.kernel.model.AssetVocabulary>
		getGroupVocabularies(long[] groupIds, int[] visibilityTypes) {

		return getService().getGroupVocabularies(groupIds, visibilityTypes);
	}

	public static int getGroupVocabulariesCount(long groupId) {
		return getService().getGroupVocabulariesCount(groupId);
	}

	public static int getGroupVocabulariesCount(long groupId, String name) {
		return getService().getGroupVocabulariesCount(groupId, name);
	}

	public static int getGroupVocabulariesCount(long[] groupIds) {
		return getService().getGroupVocabulariesCount(groupIds);
	}

	public static com.liferay.asset.kernel.model.AssetVocabularyDisplay
			getGroupVocabulariesDisplay(
				long groupId, String name, int start, int end,
				boolean addDefaultVocabulary,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.asset.kernel.model.AssetVocabulary>
						orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getGroupVocabulariesDisplay(
			groupId, name, start, end, addDefaultVocabulary, orderByComparator);
	}

	public static com.liferay.asset.kernel.model.AssetVocabularyDisplay
			getGroupVocabulariesDisplay(
				long groupId, String name, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.asset.kernel.model.AssetVocabulary>
						orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getGroupVocabulariesDisplay(
			groupId, name, start, end, orderByComparator);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.asset.kernel.model.AssetVocabulary getVocabulary(
			long vocabularyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getVocabulary(vocabularyId);
	}

	public static com.liferay.asset.kernel.model.AssetVocabularyDisplay
			searchVocabulariesDisplay(
				long groupId, String title, boolean addDefaultVocabulary,
				int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().searchVocabulariesDisplay(
			groupId, title, addDefaultVocabulary, start, end);
	}

	public static com.liferay.asset.kernel.model.AssetVocabularyDisplay
			searchVocabulariesDisplay(
				long groupId, String title, boolean addDefaultVocabulary,
				int start, int end, com.liferay.portal.kernel.search.Sort sort)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().searchVocabulariesDisplay(
			groupId, title, addDefaultVocabulary, start, end, sort);
	}

	public static com.liferay.asset.kernel.model.AssetVocabulary
			updateVocabulary(
				long vocabularyId,
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String settings)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateVocabulary(
			vocabularyId, titleMap, descriptionMap, settings);
	}

	public static com.liferay.asset.kernel.model.AssetVocabulary
			updateVocabulary(
				long vocabularyId,
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String settings, int visibilityType)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateVocabulary(
			vocabularyId, titleMap, descriptionMap, settings, visibilityType);
	}

	public static com.liferay.asset.kernel.model.AssetVocabulary
			updateVocabulary(
				long vocabularyId, String title,
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String settings,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateVocabulary(
			vocabularyId, title, titleMap, descriptionMap, settings,
			serviceContext);
	}

	public static com.liferay.asset.kernel.model.AssetVocabulary
			updateVocabulary(
				long vocabularyId, String name, String title,
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String settings,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateVocabulary(
			vocabularyId, name, title, titleMap, descriptionMap, settings,
			serviceContext);
	}

	public static AssetVocabularyService getService() {
		if (_service == null) {
			_service = (AssetVocabularyService)PortalBeanLocatorUtil.locate(
				AssetVocabularyService.class.getName());
		}

		return _service;
	}

	private static AssetVocabularyService _service;

}