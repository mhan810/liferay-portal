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
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.ResourcedModel;

import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class ExportImportAdvice {

	protected long getClassPK(LarDigestItem item) {
		return Long.valueOf(item.getClassPK());
	}

	protected String getEntityObjectInterfaceName(Object entity) {
		if (entity == null || !(entity instanceof BaseModel)) {
			return StringPool.BLANK;
		}

		Class entitySuperClass = entity.getClass().getSuperclass();

		Class[] superClassInterfaces = entitySuperClass.getInterfaces();

		if (superClassInterfaces != null && superClassInterfaces.length > 0) {
			return superClassInterfaces[0].getName();
		}

		return StringPool.BLANK;
	}

	protected String getPermissionPath(Object entity) {
		if (entity instanceof BaseModel) {
			BaseModel baseModel = (BaseModel)entity;

			Map<String, Object> modelAttributes =
				baseModel.getModelAttributes();

			StringBundler sb = new StringBundler();

			sb.append(StringPool.FORWARD_SLASH);
			sb.append("group");
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(modelAttributes.get("groupId"));
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(baseModel.getModelClassName());
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(baseModel.getPrimaryKeyObj() + "-permissions.xml");

			return sb.toString();
		}

		return StringPool.BLANK;
	}

	protected boolean isResourceMain(Object object) {
		if (object instanceof ResourcedModel) {
			ResourcedModel resourcedModel = (ResourcedModel)object;

			return resourcedModel.isResourceMain();
		}

		return true;
	}

}
