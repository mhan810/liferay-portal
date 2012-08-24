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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.lar.digest.LarDigestEntry;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.ResourcedModel;
import com.liferay.portal.model.Role;

/**
 * @author Mate Thurzo
 */
public class ExportImportAdvice {

	protected long getClassPK(LarDigestEntry entry) {
		return Long.valueOf(entry.getClassPK());
	}

	protected String getEntityObjectInterfaceName(Object entity) {
		if ((entity == null) || !(entity instanceof BaseModel)) {
			return StringPool.BLANK;
		}

		Class entitySuperClass = entity.getClass().getSuperclass();

		Class[] superClassInterfaces = entitySuperClass.getInterfaces();

		if ((superClassInterfaces != null) &&
			(superClassInterfaces.length > 0)) {

			return superClassInterfaces[0].getName();
		}

		return StringPool.BLANK;
	}

	protected String getRolePath(Role role) {
		return getRolePath(role.getName());
	}

	protected String getRolePath(String roleName) {
		StringBundler sb = new StringBundler();

		sb.append(StringPool.FORWARD_SLASH);
		sb.append("roles");
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(roleName + ".xml");

		return sb.toString();
	}

	protected boolean isResourceMain(Object object) {
		if (object instanceof ResourcedModel) {
			ResourcedModel resourcedModel = (ResourcedModel)object;

			return resourcedModel.isResourceMain();
		}

		return true;
	}

}