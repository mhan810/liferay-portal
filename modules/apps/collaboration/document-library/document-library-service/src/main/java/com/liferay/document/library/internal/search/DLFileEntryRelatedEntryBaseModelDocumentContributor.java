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

package com.liferay.document.library.internal.search;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentContributor;
import com.liferay.portal.kernel.search.DocumentHelper;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.RelatedEntryIndexer;
import com.liferay.portal.kernel.search.RelatedEntryIndexerRegistry;
import com.liferay.portal.kernel.util.Portal;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eric Yan
 */
@Component(
	immediate = true,
	property = {
		"base.model.document.contributor=true",
		"indexer.class.name=com.liferay.document.library.kernel.model.DLFileEntry"
	},
	service = DocumentContributor.class
)
public class DLFileEntryRelatedEntryBaseModelDocumentContributor
	implements DocumentContributor {

	@Override
	public void contribute(Document document, BaseModel baseModel) {
		if (!(baseModel instanceof DLFileEntry)) {
			return;
		}

		DLFileEntry dlFileEntry = (DLFileEntry)baseModel;

		if (!dlFileEntry.isInHiddenFolder()) {
			return;
		}

		List<RelatedEntryIndexer> relatedEntryIndexers =
			_relatedEntryIndexerRegistry.getRelatedEntryIndexers(
				dlFileEntry.getClassName());

		if (relatedEntryIndexers != null) {
			DocumentHelper documentHelper = new DocumentHelper(document);

			documentHelper.setAttachmentOwnerKey(
				_portal.getClassNameId(dlFileEntry.getClassName()),
				dlFileEntry.getClassPK());

			document.addKeyword(Field.RELATED_ENTRY, true);
		}
	}

	@Reference
	private Portal _portal;

	@Reference
	private RelatedEntryIndexerRegistry _relatedEntryIndexerRegistry;

}