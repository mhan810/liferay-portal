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

import com.liferay.digital.signature.internal.model.field.UserEntryDSFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.builder.UserEntryDSFieldBuilder;

/**
 * @author Michael C. Han
 */
public abstract class UserEntryDSFieldBuilderImpl<T extends DSField>
	extends StyledDSFieldBuilderImpl<T> implements UserEntryDSFieldBuilder<T> {

	public UserEntryDSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	public Boolean getConcealValue() {
		return _concealValue;
	}

	public Boolean getDisableAutoSize() {
		return _disableAutoSize;
	}

	public Integer getMaxLength() {
		return _maxLength;
	}

	public String getOriginalValue() {
		return _originalValue;
	}

	public Boolean getRequireInitialOnSharedChange() {
		return _requireInitialOnSharedChange;
	}

	public String getValidationMessage() {
		return _validationMessage;
	}

	public String getValidationRegex() {
		return _validationRegex;
	}

	public String getValue() {
		return _value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setConcealValue(Boolean concealValue) {
		_concealValue = concealValue;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setDisableAutoSize(Boolean disableAutoSize) {
		_disableAutoSize = disableAutoSize;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setMaxLength(Integer maxLength) {
		_maxLength = maxLength;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setOriginalValue(String originalValue) {
		_originalValue = originalValue;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setRequireInitialOnSharedChange(
		Boolean requireInitialOnSharedChange) {

		_requireInitialOnSharedChange = requireInitialOnSharedChange;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setValidationMessage(String validationMessage) {
		_validationMessage = validationMessage;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setValidationRegex(String validationRegex) {
		_validationRegex = validationRegex;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setValue(String value) {
		_value = value;

		return (S)this;
	}

	protected void populateFields(
		UserEntryDSFieldImpl<T> userEntryDSFieldImpl) {

		userEntryDSFieldImpl.setConcealValue(_concealValue);
		userEntryDSFieldImpl.setDisableAutoSize(_disableAutoSize);
		userEntryDSFieldImpl.setMaxLength(_maxLength);
		userEntryDSFieldImpl.setOriginalValue(_originalValue);
		userEntryDSFieldImpl.setRequireInitialOnSharedChange(
			_requireInitialOnSharedChange);
		userEntryDSFieldImpl.setValidationMessage(_validationMessage);
		userEntryDSFieldImpl.setValidationRegex(_validationRegex);
		userEntryDSFieldImpl.setValue(_value);

		super.populateFields(userEntryDSFieldImpl);
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