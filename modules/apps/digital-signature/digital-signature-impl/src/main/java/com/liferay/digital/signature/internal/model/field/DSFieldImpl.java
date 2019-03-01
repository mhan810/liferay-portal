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

import com.liferay.digital.signature.model.field.AnchorUnits;
import com.liferay.digital.signature.model.field.DSField;

/**
 * @author Michael C. Han
 */
public abstract class DSFieldImpl<T extends DSField> implements DSField<T> {

	public DSFieldImpl(String documentId, String fieldId, Integer pageNumber) {
		_documentId = documentId;
		_fieldId = fieldId;
		_pageNumber = pageNumber;
	}

	@Override
	public Boolean getAnchorIgnoreIfNotPresent() {
		return _anchorIgnoreIfNotPresent;
	}

	@Override
	public String getAnchorString() {
		return _anchorString;
	}

	@Override
	public AnchorUnits getAnchorUnits() {
		return _anchorUnits;
	}

	@Override
	public Float getAnchorXOffset() {
		return _anchorXOffset;
	}

	@Override
	public Float getAnchorYOffset() {
		return _anchorYOffset;
	}

	@Override
	public String getConditionalParentLabel() {
		return _conditionalParentLabel;
	}

	@Override
	public String getConditionalParentValue() {
		return _conditionalParentValue;
	}

	@Override
	public String getDocumentId() {
		return _documentId;
	}

	@Override
	public String getFieldId() {
		return _fieldId;
	}

	@Override
	public String getFieldLabel() {
		return _fieldLabel;
	}

	@Override
	public Integer getFieldOrder() {
		return _fieldOrder;
	}

	@Override
	public Boolean getLocked() {
		return _locked;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public Integer getPageNumber() {
		return _pageNumber;
	}

	@Override
	public String getParticipantId() {
		return _participantId;
	}

	@Override
	public Boolean getRequireAll() {
		return _requireAll;
	}

	@Override
	public Boolean getRequired() {
		return _required;
	}

	@Override
	public Boolean getShared() {
		return _shared;
	}

	@Override
	public Boolean getTemplateLocked() {
		return _templateLocked;
	}

	@Override
	public Boolean getTemplateRequired() {
		return _templateRequired;
	}

	@Override
	public Float getXPosition() {
		return _xPosition;
	}

	@Override
	public Float getYPosition() {
		return _yPosition;
	}

	public void setAnchorIgnoreIfNotPresent(Boolean anchorIgnoreIfNotPresent) {
		_anchorIgnoreIfNotPresent = anchorIgnoreIfNotPresent;
	}

	public void setAnchorString(String anchorString) {
		_anchorString = anchorString;
	}

	public void setAnchorUnits(AnchorUnits anchorUnits) {
		_anchorUnits = anchorUnits;
	}

	public void setAnchorXOffset(Float anchorXOffset) {
		_anchorXOffset = anchorXOffset;
	}

	public void setAnchorYOffset(Float anchorYOffset) {
		_anchorYOffset = anchorYOffset;
	}

	public void setConditionalParentLabel(String conditionalParentLabel) {
		_conditionalParentLabel = conditionalParentLabel;
	}

	public void setConditionalParentValue(String conditionalParentValue) {
		_conditionalParentValue = conditionalParentValue;
	}

	public void setFieldLabel(String fieldLabel) {
		_fieldLabel = fieldLabel;
	}

	public void setFieldOrder(Integer fieldOrder) {
		_fieldOrder = fieldOrder;
	}

	public void setLocked(Boolean locked) {
		_locked = locked;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setParticipantId(String participantId) {
		_participantId = participantId;
	}

	public void setRequireAll(Boolean requireAll) {
		_requireAll = requireAll;
	}

	public void setRequired(Boolean required) {
		_required = required;
	}

	public void setShared(Boolean shared) {
		_shared = shared;
	}

	public void setTemplateLocked(Boolean templateLocked) {
		_templateLocked = templateLocked;
	}

	public void setTemplateRequired(Boolean templateRequired) {
		_templateRequired = templateRequired;
	}

	public void setXPosition(Float xPosition) {
		_xPosition = xPosition;
	}

	public void setYPosition(Float yPosition) {
		_yPosition = yPosition;
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