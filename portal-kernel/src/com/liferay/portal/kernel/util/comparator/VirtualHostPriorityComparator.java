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

package com.liferay.portal.kernel.util.comparator;

import com.liferay.portal.kernel.model.VirtualHost;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author Raymond Augé
 */
public class VirtualHostPriorityComparator
	extends OrderByComparator<VirtualHost> {

	public static final String ORDER_BY_ASC = "VirtualHost.priority ASC";

	public static final String ORDER_BY_DESC = "VirtualHost.priority DESC";

	public static final String[] ORDER_BY_FIELDS = {"priority"};

	public VirtualHostPriorityComparator() {
		this(false);
	}

	public VirtualHostPriorityComparator(boolean ascending) {
		_ascending = ascending;

		_virtualHost = null;
		_lessThan = false;
	}

	public VirtualHostPriorityComparator(
		VirtualHost virtualHost, boolean lessThan) {

		_virtualHost = virtualHost;
		_lessThan = lessThan;

		_ascending = true;
	}

	@Override
	public int compare(VirtualHost virtualHost1, VirtualHost virtualHost2) {
		int value = 0;

		int priority1 = -1;

		if (virtualHost1 != null) {
			priority1 = virtualHost1.getPriority();
		}

		int priority2 = -1;

		if (virtualHost2 != null) {
			priority2 = virtualHost2.getPriority();
		}

		if (priority1 > priority2) {
			value = 1;
		}
		else if (priority1 < priority2) {
			value = -1;
		}
		else {
			if (_virtualHost != null) {
				if (_virtualHost.equals(virtualHost1)) {
					if (_lessThan) {
						value = 1;
					}
					else {
						value = -1;
					}
				}
				else if (_virtualHost.equals(virtualHost2)) {
					if (_lessThan) {
						value = -1;
					}
					else {
						value = 1;
					}
				}
			}
		}

		if (_ascending) {
			return value;
		}

		return -value;
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		}

		return ORDER_BY_DESC;
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private final boolean _ascending;
	private final boolean _lessThan;
	private final VirtualHost _virtualHost;

}