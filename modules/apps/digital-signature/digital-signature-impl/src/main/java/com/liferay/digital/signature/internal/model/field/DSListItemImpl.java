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

package com.liferay.digital.signature.internal.model.field;

import com.liferay.digital.signature.model.field.DSListItem;

/**
 * @author Michael C. Han
 */
public class DSListItemImpl implements DSListItem {

	public DSListItemImpl(String text, String value) {
		this(text, value, false);
	}

	public DSListItemImpl(String text, String value, boolean selected) {
		_text = text;
		_value = value;
		_selected = selected;
	}

	@Override
	public String getText() {
		return _text;
	}

	@Override
	public String getValue() {
		return _value;
	}

	public boolean isSelected() {
		return _selected;
	}

	private final boolean _selected;
	private final String _text;
	private final String _value;

}