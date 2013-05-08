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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerProxy;
import com.liferay.portal.model.StagedModel;

/**
 * @author Mate Thurzo
 */
public class StagedModelDataHandlerProxyImpl
	implements StagedModelDataHandlerProxy {

	public void exportStagedModel(
			com.liferay.portal.kernel.lar.PortletDataContext portletDataContext,
			StagedModel stagedModel)
		throws com.liferay.portal.kernel.lar.PortletDataException {

		_stagedModelDataHandler.exportStagedModel(
			portletDataContext, stagedModel);
	}

	public String[] getClassNames() {
		return _stagedModelDataHandler.getClassNames();
	}

	public StagedModelDataHandler getStagedModelDataHandler() {
		return _stagedModelDataHandler;
	}

	public void importStagedModel(
			com.liferay.portal.kernel.lar.PortletDataContext portletDataContext,
			StagedModel stagedModel)
		throws com.liferay.portal.kernel.lar.PortletDataException {

		_stagedModelDataHandler.importStagedModel(
			portletDataContext, stagedModel);
	}

	public void setStagedModelDataHandler(
		StagedModelDataHandler stagedModelDataHandler) {

		_stagedModelDataHandler = stagedModelDataHandler;
	}

	private StagedModelDataHandler _stagedModelDataHandler;

}