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
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.lar.digest.LarDigesterConstants;
import com.liferay.portal.model.Lock;
import com.liferay.portal.service.LockLocalServiceUtil;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;

/**
 * @author Mate Thurzo
 */
public class LockDataHandlerImpl
	extends BaseDataHandlerImpl<Lock>
	implements LockDataHandler {

	@Override
	public LarDigestItem doDigest(Lock lock) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		String path = getEntityPath(lock);

		if (context.isPathProcessed(path)) {
			return null;
		}

		LarDigest digest = context.getLarDigest();

		LarDigestItem digestItem = new LarDigestItemImpl();

		digestItem.setAction(LarDigesterConstants.ACTION_ADD);
		digestItem.setPath(path);
		digestItem.setType(Lock.class.getName());
		digestItem.setClassPK(StringUtil.valueOf(lock.getLockId()));

		return digestItem;
		return digestItem;
	}

	@Override
	public void doImport(LarDigestItem item) throws Exception {
		// toDo: implement
	}

	@Override
	public Lock getEntity(String classPK) {
		if (Validator.isNull(classPK)) {
			return null;
		}

		try {
			long lockId = Long.valueOf(classPK);

			Lock lock = LockLocalServiceUtil.getLock(lockId);

			return lock;
		}
		catch (Exception e) {
			return null;
		}
	}

}
