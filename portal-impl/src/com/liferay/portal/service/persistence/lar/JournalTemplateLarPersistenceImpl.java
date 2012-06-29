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

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.LarPersistenceContext;
import com.liferay.portal.kernel.lar.LarPersistenceContextThreadLocal;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.model.Image;
import com.liferay.portal.service.persistence.ImageUtil;
import com.liferay.portal.service.persistence.impl.BaseLarPersistenceImpl;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.journal.model.JournalTemplate;

/**
 * @author Daniel Kocsis
 */
public class JournalTemplateLarPersistenceImpl
	extends BaseLarPersistenceImpl<JournalTemplate>
	implements JournalTemplateLarPersistence {

	@Override
	public void digest(JournalTemplate template) throws Exception {
		return;
		/*LarPersistenceContext larPersistenceContext =
			LarPersistenceContextThreadLocal.getLarPersistenceContext();

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
				JournalPortletLarPersistence._NAMESPACE, "embedded-assets")) {

			String content =
				journalArticleLarPersistence.exportReferencedContent(
					portletDataContext, template.getXsl(),
					JournalTemplate.class);

			template.setXsl(content);
		}*/

	/*	portletDataContext.addClassedModel(
			templateElement, path, template,
			JournalPortletLarPersistence._NAMESPACE);*/
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
