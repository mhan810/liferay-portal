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
import com.liferay.digital.signature.model.field.UserEntryDSField;

/**
 * @author Michael C. Han
 */
public abstract class UserEntryDSFieldImpl<T extends DSField>
	extends StyledDSFieldImpl<T> implements UserEntryDSField<T> {

	public UserEntryDSFieldImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public Boolean getConcealValue() {
		return _concealValue;
	}

	@Override
	public Boolean getDisableAutoSize() {
		return _disableAutoSize;
	}

	@Override
	public Integer getMaxLength() {
		return _maxLength;
	}

	@Override
	public String getOriginalValue() {
		return _originalValue;
	}

	@Override
	public Boolean getRequireInitialOnSharedChange() {
		return _requireInitialOnSharedChange;
	}

	@Override
	public String getValidationMessage() {
		return _validationMessage;
	}

	@Override
	public String getValidationRegex() {
		return _validationRegex;
	}

	@Override
	public String getValue() {
		return _value;
	}

	public void setConcealValue(Boolean concealValue) {
		_concealValue = concealValue;
	}

	public void setDisableAutoSize(Boolean disableAutoSize) {
		_disableAutoSize = disableAutoSize;
	}

	public void setMaxLength(Integer maxLength) {
		_maxLength = maxLength;
	}

	public void setOriginalValue(String originalValue) {
		_originalValue = originalValue;
	}

	public void setRequireInitialOnSharedChange(
		Boolean requireInitialOnSharedChange) {

		_requireInitialOnSharedChange = requireInitialOnSharedChange;
	}

	public void setValidationMessage(String validationMessage) {
		_validationMessage = validationMessage;
	}

	public void setValidationRegex(String validationRegex) {
		_validationRegex = validationRegex;
	}

	public void setValue(String value) {
		_value = value;
	}

	private Boolean _concealValue;
	private Boolean _disableAutoSize;
	private Integer _maxLength;
	private String _originalValue;
	private Boolean _requireInitialOnSharedChange;
	private String _validationMessage;
	private String _validationRegex;
	private String _value;

}