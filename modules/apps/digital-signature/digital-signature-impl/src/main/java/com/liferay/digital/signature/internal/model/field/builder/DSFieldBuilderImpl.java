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

import com.liferay.digital.signature.internal.model.field.DSFieldImpl;
import com.liferay.digital.signature.model.field.AnchorUnits;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.builder.DSFieldBuilder;

/**
 * @author Michael C. Han
 */
public abstract class DSFieldBuilderImpl<T extends DSField>
	implements DSFieldBuilder<T> {

	public DSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		_documentId = documentId;
		_fieldId = fieldId;
		_pageNumber = pageNumber;
	}

	public Boolean getAnchorIgnoreIfNotPresent() {
		return _anchorIgnoreIfNotPresent;
	}

	public String getAnchorString() {
		return _anchorString;
	}

	public AnchorUnits getAnchorUnits() {
		return _anchorUnits;
	}

	public Float getAnchorXOffset() {
		return _anchorXOffset;
	}

	public Float getAnchorYOffset() {
		return _anchorYOffset;
	}

	public String getConditionalParentLabel() {
		return _conditionalParentLabel;
	}

	public String getConditionalParentValue() {
		return _conditionalParentValue;
	}

	public String getDocumentId() {
		return _documentId;
	}

	public String getFieldId() {
		return _fieldId;
	}

	public String getFieldLabel() {
		return _fieldLabel;
	}

	public Integer getFieldOrder() {
		return _fieldOrder;
	}

	public Boolean getLocked() {
		return _locked;
	}

	public String getName() {
		return _name;
	}

	public Integer getPageNumber() {
		return _pageNumber;
	}

	public String getParticipantId() {
		return _participantId;
	}

	public Boolean getRequired() {
		return _required;
	}

	public Boolean getShared() {
		return _shared;
	}

	public Boolean getTemplateLocked() {
		return _templateLocked;
	}

	public Boolean getTemplateRequired() {
		return _templateRequired;
	}

	public Float getXPosition() {
		return _xPosition;
	}

	public Float getYPosition() {
		return _yPosition;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setAnchorIgnoreIfNotPresent(Boolean anchorIgnoreIfNotPresent) {
		_anchorIgnoreIfNotPresent = anchorIgnoreIfNotPresent;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setAnchorString(String anchorString) {
		_anchorString = anchorString;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setAnchorUnits(AnchorUnits anchorUnits) {
		_anchorUnits = anchorUnits;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setAnchorXOffset(Float anchorXOffset) {
		_anchorXOffset = anchorXOffset;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setAnchorYOffset(Float anchorYOffset) {
		_anchorYOffset = anchorYOffset;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setConditionalParentLabel(String conditionalParentLabel) {
		_conditionalParentLabel = conditionalParentLabel;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setConditionalParentValue(String conditionalParentValue) {
		_conditionalParentValue = conditionalParentValue;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setFieldLabel(String fieldLabel) {
		_fieldLabel = fieldLabel;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setFieldOrder(Integer fieldOrder) {
		_fieldOrder = fieldOrder;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setLocked(Boolean locked) {
		_locked = locked;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setName(String name) {
		_name = name;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setParticipantId(String participantId) {
		_participantId = participantId;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setRequireAll(Boolean requireAll) {
		_requireAll = requireAll;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setRequired(Boolean required) {
		_required = required;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setShared(Boolean shared) {
		_shared = shared;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setTemplateLocked(Boolean templateLocked) {
		_templateLocked = templateLocked;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setTemplateRequired(Boolean templateRequired) {
		_templateRequired = templateRequired;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setXPosition(Float xPosition) {
		_xPosition = xPosition;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S setYPosition(Float yPosition) {
		_yPosition = yPosition;

		return (S)this;
	}

	protected void populateFields(DSFieldImpl<T> dsFieldImpl) {
		dsFieldImpl.setAnchorIgnoreIfNotPresent(_anchorIgnoreIfNotPresent);
		dsFieldImpl.setAnchorString(_anchorString);
		dsFieldImpl.setAnchorUnits(_anchorUnits);
		dsFieldImpl.setAnchorXOffset(_anchorXOffset);
		dsFieldImpl.setAnchorYOffset(_anchorYOffset);
		dsFieldImpl.setConditionalParentLabel(_conditionalParentLabel);
		dsFieldImpl.setConditionalParentValue(_conditionalParentValue);
		dsFieldImpl.setFieldLabel(_fieldLabel);
		dsFieldImpl.setFieldOrder(_fieldOrder);
		dsFieldImpl.setLocked(_locked);
		dsFieldImpl.setParticipantId(_participantId);
		dsFieldImpl.setRequireAll(_requireAll);
		dsFieldImpl.setRequired(_required);
		dsFieldImpl.setShared(_shared);
		dsFieldImpl.setTemplateLocked(_templateLocked);
		dsFieldImpl.setTemplateRequired(_templateRequired);
		dsFieldImpl.setXPosition(_xPosition);
		dsFieldImpl.setYPosition(_yPosition);
	}

	private Boolean _anchorIgnoreIfNotPresent;
	private String _anchorString;
	private AnchorUnits _anchorUnits;
	private Float _anchorXOffset;
	private Float _anchorYOffset;
	private String _conditionalParentLabel;
	private String _conditionalParentValue;
	private final String _documentId;
	private final String _fieldId;
	private String _fieldLabel;
	private Integer _fieldOrder;
	private Boolean _locked;
	private String _name;
	private final Integer _pageNumber;
	private String _participantId;
	private Boolean _requireAll;
	private Boolean _required;
	private Boolean _shared;
	private Boolean _templateLocked;
	private Boolean _templateRequired;
	private Float _xPosition;
	private Float _yPosition;

}