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

import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.StyledDSField;

/**
 * @author Michael C. Han
 */
public abstract class StyledDSFieldImpl<T extends DSField>
	extends DSFieldImpl<T> implements StyledDSField<T> {

	public StyledDSFieldImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public Boolean getBold() {
		return _bold;
	}

	@Override
	public String getFont() {
		return _font;
	}

	@Override
	public String getFontColor() {
		return _fontColor;
	}

	@Override
	public String getFontSize() {
		return _fontSize;
	}

	@Override
	public Integer getHeight() {
		return _height;
	}

	@Override
	public Boolean getItalic() {
		return _italic;
	}

	@Override
	public Boolean getUnderline() {
		return _underline;
	}

	@Override
	public Integer getWidth() {
		return _width;
	}

	public void setBold(Boolean bold) {
		_bold = bold;
	}

	public void setFont(String font) {
		_font = font;
	}

	public void setFontColor(String fontColor) {
		_fontColor = fontColor;
	}

	public void setFontSize(String fontSize) {
		_fontSize = fontSize;
	}

	public void setHeight(Integer height) {
		_height = height;
	}

	public void setItalic(Boolean italic) {
		_italic = italic;
	}

	public void setUnderline(Boolean underline) {
		_underline = underline;
	}

	public void setWidth(Integer width) {
		_width = width;
	}

	private Boolean _bold;
	private String _font;
	private String _fontColor;
	private String _fontSize;
	private Integer _height;
	private Boolean _italic;
	private Boolean _underline;
	private Integer _width;

}