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

package com.liferay.message.boards.internal.search;

import com.liferay.message.boards.model.MBMessage;
import com.liferay.portal.kernel.comment.Comment;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentContributor;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.RelatedEntryIndexer;
import com.liferay.portal.kernel.search.RelatedEntryIndexerRegistry;

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
		"indexer.class.name=com.liferay.message.boards.model.MBMessage"
	},
	service = DocumentContributor.class
)
public class MBMessageRelatedEntryBaseModelDocumentContributor
	implements DocumentContributor {

	@Override
	public void contribute(Document document, BaseModel baseModel) {
		if (!(baseModel instanceof MBMessage)) {
			return;
		}

		MBMessage mbMessage = (MBMessage)baseModel;

		if (mbMessage.isDiscussion()) {
			List<RelatedEntryIndexer> relatedEntryIndexers =
				_relatedEntryIndexerRegistry.getRelatedEntryIndexers(
					mbMessage.getClassName());

			if ((relatedEntryIndexers != null) &&
				!relatedEntryIndexers.isEmpty()) {

				Comment comment = commentManager.fetchComment(
					mbMessage.getMessageId());

				if (comment != null) {
					document.addKeyword(Field.RELATED_ENTRY, true);
				}
			}
		}
	}

	@Reference
	protected CommentManager commentManager;

	@Reference
	private RelatedEntryIndexerRegistry _relatedEntryIndexerRegistry;

}