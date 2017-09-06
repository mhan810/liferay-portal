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

package com.liferay.portal.search.buffer;

import com.liferay.portal.kernel.util.AutoResetThreadLocal;

/**
 * @author Gergely Mathe
 */
public class MassUpdateThreadLocal {

	public static boolean isMassUpdateMode() {
		return _massUpdateMode.get();
	}

	public static void setMassUpdateMode(boolean massUpdateMode) {
		_massUpdateMode.set(massUpdateMode);
	}

	private static final ThreadLocal<Boolean> _massUpdateMode =
		new AutoResetThreadLocal<>(
			MassUpdateThreadLocal.class + "._massUpdateMode",
			() -> Boolean.FALSE);

}