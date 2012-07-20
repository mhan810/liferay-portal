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
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.journal.model.JournalTemplate;

/**
 * @author Daniel Kocsis
 */
public class JournalTemplateDataHandlerImpl
	extends BaseDataHandlerImpl<JournalTemplate>
	implements JournalTemplateDataHandler {

	@Override
	public void digest(JournalTemplate template) throws Exception {
		return;
		/*DataHandlerContext larPersistenceContext =
			DataHandlerContextThreadLocal.getDataHandlerContext();

		String path = getTemplatePath(template);

		if (isPathProcessed(path)) {
			return;
		}

		// Clone this template to make sure changes to its content are never
		// persisted

		template = (JournalTemplate)template.clone();

		if (template.isSmallImage()) {
			String smallImagePath = getTemplateSmallImagePath(template);

			Image smallImage = ImageUtil.fetchByPrimaryKey(
				template.getSmallImageId());

			template.setSmallImageType(smallImage.getType());
		}

		if (larPersistenceContext.getBooleanParameter(
				JournalPortletDataHandler._NAMESPACE, "embedded-assets")) {

			String content =
				journalArticleLarPersistence.exportReferencedContent(
					portletDataContext, template.getXsl(),
					JournalTemplate.class);

			template.setXsl(content);
		}*/

	/*	portletDataContext.addClassedModel(
			templateElement, path, template,
			JournalPortletDataHandler._NAMESPACE);*/
	}

	@Override
	public JournalTemplate getEntity(String classPK) {
		// TODO implement getEntity
		return null;
	}

	@Override
	public void doDigest(JournalTemplate object) throws Exception {
		// TODO implement doDigest
	}

	@Override
	public void doImport(LarDigestItem item) throws Exception {
		// toDo: implement method
	}

	private String getTemplatePath(JournalTemplate template) {

		StringBundler sb = new StringBundler(4);

		sb.append(getPortletPath(PortletKeys.JOURNAL));
		sb.append("/templates/");
		sb.append(template.getUuid());
		sb.append(".xml");

		return sb.toString();
	}

	private String getTemplateSmallImagePath(JournalTemplate template)
		throws Exception {

		StringBundler sb = new StringBundler(5);

		sb.append(getPortletPath(PortletKeys.JOURNAL));
		sb.append("/templates/thumbnail-");
		sb.append(template.getUuid());
		sb.append(StringPool.PERIOD);
		sb.append(template.getSmallImageType());

		return sb.toString();
	}

}
