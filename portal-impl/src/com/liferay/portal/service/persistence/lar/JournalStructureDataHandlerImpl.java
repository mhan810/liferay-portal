/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.service.persistence.lar;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.journal.NoSuchStructureException;
import com.liferay.portlet.journal.model.JournalStructure;
import com.liferay.portlet.journal.service.JournalStructureLocalServiceUtil;

/**
 * @author Daniel Kocsis
 */
public class JournalStructureDataHandlerImpl
	extends BaseDataHandlerImpl<JournalStructure>
	implements JournalStructureDataHandler {

	@Override
	protected void doDigest(JournalStructure structure) throws Exception {

		String path = getStructurePath(structure);

		if (isPathProcessed(path)) {
			return;
		}

		String parentStructureId = structure.getParentStructureId();

		if (Validator.isNotNull(parentStructureId)) {
			try {
				JournalStructure parentStructure =
					JournalStructureLocalServiceUtil.getStructure(
						structure.getGroupId(), parentStructureId, true);

				doDigest(parentStructure);
			}
			catch (NoSuchStructureException nsse) {
			}
		}

		/*portletDataContext.addClassedModel(
			path, structure, JournalPortletDataHandler._NAMESPACE);*/
	}

	private String getStructurePath(JournalStructure structure) {

		StringBundler sb = new StringBundler(4);

		sb.append(getPortletPath(PortletKeys.JOURNAL));
		sb.append("/structures/");
		sb.append(structure.getUuid());
		sb.append(".xml");

		return sb.toString();
	}
}
