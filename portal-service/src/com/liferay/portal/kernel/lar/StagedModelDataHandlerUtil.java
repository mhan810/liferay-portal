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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.StagedModel;

/**
 * @author Brian Wing Shun Chan
 * @author Mate Thurzo
 */
public class StagedModelDataHandlerUtil {

	public static <T extends StagedModel> void exportStagedModel(
			PortletDataContext portletDataContext, T stagedModel)
		throws PortletDataException {

		StagedModelDataHandler<T> stagedModelDataHandler =
			_getStagedModelDataHandler(stagedModel);

		stagedModelDataHandler.exportStagedModel(
			portletDataContext, stagedModel);
	}

	public static void importStagedModel(
			PortletDataContext portletDataContext, Element element)
		throws PortletDataException {

		String path = element.attributeValue("path");

		StagedModel stagedModel =
			(StagedModel)portletDataContext.getZipEntryAsObject(element, path);

		importStagedModel(portletDataContext, stagedModel);
	}

	public static <T extends StagedModel> void importStagedModel(
			PortletDataContext portletDataContext, T stagedModel)
		throws PortletDataException {

		StagedModelDataHandler<T> stagedModelDataHandler =
			_getStagedModelDataHandler(stagedModel);

		stagedModelDataHandler.importStagedModel(
			portletDataContext, stagedModel);
	}

	public static <T extends StagedModel> String prepareStagedModel(
		PortletDataContext portletDataContext,
		Class<? extends StagedModel> stagedModelClass) {

		StagedModelDataHandler<T> stagedModelDataHandler =
			_getStagedModelDataHandler(stagedModelClass.getName());

		return stagedModelDataHandler.prepareStagedModel(portletDataContext);
	}

	private static <T extends StagedModel> StagedModelDataHandler<T>
		_getStagedModelDataHandler(T stagedModel) {

		ClassedModel classedModel = stagedModel;

		return _getStagedModelDataHandler(classedModel.getModelClassName());
	}

	private static <T extends StagedModel> StagedModelDataHandler<T>
		_getStagedModelDataHandler(String stagedModelClassName) {

		return (StagedModelDataHandler<T>)
			StagedModelDataHandlerRegistryUtil.getStagedModelDataHandler(
				stagedModelClassName);
	}

}