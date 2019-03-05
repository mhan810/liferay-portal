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

package com.liferay.digital.signature.internal.model.field.builder;

import com.liferay.digital.signature.internal.model.field.StyledDSFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.builder.StyledDSFieldBuilder;

/**
 * @author Michael C. Han
 */
public abstract class StyledDSFieldBuilderImpl<T extends DSField>
	extends DSFieldBuilderImpl<T> implements StyledDSFieldBuilder<T> {

	public StyledDSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	public Boolean getBold() {
		return _bold;
	}

	public String getFont() {
		return _font;
	}

	public String getFontColor() {
		return _fontColor;
	}

	public String getFontSize() {
		return _fontSize;
	}

	public Integer getHeight() {
		return _height;
	}

	public Boolean getItalic() {
		return _italic;
	}

	public Boolean getUnderline() {
		return _underline;
	}

	public Integer getWidth() {
		return _width;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setBold(Boolean bold) {
		_bold = bold;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setFont(String font) {
		_font = font;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setFontColor(String fontColor) {
		_fontColor = fontColor;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setFontSize(String fontSize) {
		_fontSize = fontSize;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setHeight(Integer height) {
		_height = height;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setItalic(Boolean italic) {
		_italic = italic;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setUnderline(Boolean underline) {
		_underline = underline;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setWidth(Integer width) {
		_width = width;

		return (S)this;
	}

	protected void populateFields(StyledDSFieldImpl<T> styledDSFieldImpl) {
		styledDSFieldImpl.setBold(_bold);
		styledDSFieldImpl.setFont(_font);
		styledDSFieldImpl.setFontColor(_fontColor);
		styledDSFieldImpl.setFontSize(_fontSize);
		styledDSFieldImpl.setHeight(_height);
		styledDSFieldImpl.setItalic(_italic);
		styledDSFieldImpl.setUnderline(_underline);
		styledDSFieldImpl.setWidth(_width);

		super.populateFields(styledDSFieldImpl);
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