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

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InitialThreadLocal;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

/**
 * @author Pavel Savinov
 */
public class CommitImmediatelyThreadLocal {

	public static boolean isCommitImmediately() {
		return _commitImmediatelyThreadLocal.get();
	}

	public static void setCommitImmediately(boolean commitImmediately) {
		_commitImmediatelyThreadLocal.set(commitImmediately);
	}

	private static final boolean _INDEX_COMMIT_IMMEDIATELY =
		GetterUtil.getBoolean(
			PropsUtil.get(PropsKeys.INDEX_COMMIT_IMMEDIATELY));

	private static final ThreadLocal<Boolean> _commitImmediatelyThreadLocal =
		new InitialThreadLocal<>(
			CommitImmediatelyThreadLocal.class +
				"._commitImmediatelyThreadLocal",
			_INDEX_COMMIT_IMMEDIATELY);

}