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

import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
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
	public LarDigestItem doDigest(JournalStructure structure) throws Exception {
		return null;
	}

	@Override
	public void doImportData(LarDigestItem item) throws Exception {
		return;
	}

	@Override
	public JournalStructure getEntity(String classPK) {
		if (Validator.isNull(classPK)) {
			return null;
		}

		try {
			long structureId = Long.valueOf(classPK);

			JournalStructure structure =
				JournalStructureLocalServiceUtil.getStructure(structureId);

			return structure;
		}
		catch (Exception e) {
			return null;
		}
	}

}
