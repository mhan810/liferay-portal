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

package com.liferay.portal.custom.attributes.expando.internal;

import com.liferay.data.engine.rest.dto.v1_0.DataDefinition;
import com.liferay.data.engine.rest.dto.v1_0.DataDefinitionField;
import com.liferay.data.engine.rest.resource.v1_0.DataDefinitionResource;
import com.liferay.dynamic.data.mapping.kernel.NoSuchStructureException;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.util.PropsValues;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author Michael C. Han
 */
public class DataEngineExpandoBridgeImpl implements ExpandoBridge {


	public DataEngineExpandoBridgeImpl(long companyId, String className) {
		this(companyId, className, 0);
	}

	public DataEngineExpandoBridgeImpl(
		long companyId, String className, long classPK) {

		_companyId = companyId;
		_className = className;
		_classPK = classPK;

		Group group = _groupLocalService.fetchCompanyGroup(companyId);

		if (group == null) {
			throw new IllegalArgumentException("Invalid company: " + companyId);
		}

		_companyGroupId = group.getGroupId();
	}


	@Override
	public void addAttribute(String name) throws PortalException {
		boolean secure =
			PropsValues.PERMISSIONS_CUSTOM_ATTRIBUTE_WRITE_CHECK_BY_DEFAULT;

		if (ExportImportThreadLocal.isImportInProcess()) {
			secure = false;
		}

		addAttribute(name, ExpandoColumnConstants.STRING, null, secure);
	}

	@Override
	public void addAttribute(String name, boolean secure)
		throws PortalException {

		addAttribute(name, ExpandoColumnConstants.STRING, null, secure);
	}

	@Override
	public void addAttribute(String name, int type) throws PortalException {
		boolean secure =
			PropsValues.PERMISSIONS_CUSTOM_ATTRIBUTE_WRITE_CHECK_BY_DEFAULT;

		if (ExportImportThreadLocal.isImportInProcess()) {
			secure = false;
		}

		addAttribute(name, type, null, secure);
	}

	@Override
	public void addAttribute(String name, int type, boolean secure)
		throws PortalException {

		addAttribute(name, type, null, secure);
	}

	@Override
	public void addAttribute(
			String name, int type, Serializable defaultValue)
		throws PortalException {

		boolean secure =
			PropsValues.PERMISSIONS_CUSTOM_ATTRIBUTE_WRITE_CHECK_BY_DEFAULT;

		if (ExportImportThreadLocal.isImportInProcess()) {
			secure = false;
		}

		addAttribute(name, type, defaultValue, secure);
	}

	@Override
	public void addAttribute(
			String name, int type, Serializable defaultValue, boolean secure)
		throws PortalException {
		
		boolean isNew = false;

		DataDefinition dataDefinition = null;

		try {
			dataDefinition = _dataDefinitionResource.getSiteDataDefinition(
				_companyGroupId, _className);
		}
		catch (Exception e) {
			if (e instanceof NoSuchStructureException) {
				dataDefinition = new DataDefinition();

				isNew = true;
			}
		}

		DataDefinitionField[] dataDefinitionFields =
			dataDefinition.getDataDefinitionFields();

		List<DataDefinitionField> dataDefinitionFieldsList = new ArrayList<>();

		if (ArrayUtil.isEmpty(dataDefinitionFields)) {
			dataDefinitionFieldsList.addAll(
				Arrays.asList(dataDefinitionFields));
		}

		DataDefinitionField dataDefinitionField = createDataDefinitionField(
			name, type, defaultValue);

		dataDefinitionFieldsList.add(dataDefinitionField);

		dataDefinition.setDataDefinitionFields(
			dataDefinitionFieldsList.toArray(new DataDefinitionField[0]));


		if (isNew) {
			_dataDefinitionResource.postSiteDataDefinition(
				_companyGroupId, dataDefinition);
		}
		else {
			_dataDefinitionResource.putDataDefinition(
				dataDefinition.getId(), dataDefinition);
		}

	}

	protected DataDefinitionField createDataDefinitionField(
		String name, int type, Serializable defaultValue) {

		DataDefinitionField dataDefinitionField = new DataDefinitionField();

		dataDefinitionField.setName(name);

		switch (type) {
			case ExpandoColumnConstants.BOOLEAN:
				dataDefinitionField.setFieldType("checkbox");
			case ExpandoColumnConstants.BOOLEAN_ARRAY:
				dataDefinitionField.setFieldType("checkbox_multiple");
			case ExpandoColumnConstants.DATE:
				dataDefinitionField.setFieldType("date");
			case ExpandoColumnConstants.DATE_ARRAY:
				dataDefinitionField.setFieldType("string");
			case ExpandoColumnConstants.DOUBLE:
				dataDefinitionField.setFieldType("numeric");
			case ExpandoColumnConstants.DOUBLE_ARRAY:
				dataDefinitionField.setFieldType("string");
			case ExpandoColumnConstants.FLOAT:
				dataDefinitionField.setFieldType("numeric");
			case ExpandoColumnConstants.FLOAT_ARRAY:
				dataDefinitionField.setFieldType("string");
			case ExpandoColumnConstants.INTEGER:
				dataDefinitionField.setFieldType("numeric");
			case ExpandoColumnConstants.INTEGER_ARRAY:
				dataDefinitionField.setFieldType("string");
			case ExpandoColumnConstants.LONG:
				dataDefinitionField.setFieldType("numeric");
			case ExpandoColumnConstants.LONG_ARRAY:
				dataDefinitionField.setFieldType("string");
			case ExpandoColumnConstants.NUMBER:
				dataDefinitionField.setFieldType("numeric");
			case ExpandoColumnConstants.NUMBER_ARRAY:
				dataDefinitionField.setFieldType("string");
			case ExpandoColumnConstants.SHORT:
				dataDefinitionField.setFieldType("numeric");
			case ExpandoColumnConstants.SHORT_ARRAY:
				dataDefinitionField.setFieldType("string");
			case ExpandoColumnConstants.STRING:
				dataDefinitionField.setFieldType("string");
			case ExpandoColumnConstants.STRING_ARRAY:
				dataDefinitionField.setFieldType("string");
			case ExpandoColumnConstants.STRING_LOCALIZED:
				dataDefinitionField.setFieldType("string");
			case ExpandoColumnConstants.STRING_ARRAY_LOCALIZED:
				dataDefinitionField.setFieldType("string");
		}

		dataDefinitionField.setIndexable(false);
		dataDefinitionField.setReadOnly(false);
		dataDefinitionField.setRequired(false);
		dataDefinitionField.setVisible(true);
		return dataDefinitionField;
	}

	@Override
	public Serializable getAttribute(String name) {
		return null;
	}

	@Override
	public Serializable getAttribute(String name, boolean secure) {
		return null;
	}

	@Override
	public Serializable getAttributeDefault(String name) {
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return null;
	}

	@Override
	public UnicodeProperties getAttributeProperties(
		String name) {
		return null;
	}

	@Override
	public Map<String, Serializable> getAttributes() {
		return null;
	}

	@Override
	public Map<String, Serializable> getAttributes(boolean secure) {
		return null;
	}

	@Override
	public Map<String, Serializable> getAttributes(
		Collection<String> names) {
		return null;
	}

	@Override
	public Map<String, Serializable> getAttributes(
		Collection<String> names, boolean secure) {
		return null;
	}

	@Override
	public int getAttributeType(String name) {
		return 0;
	}

	@Override
	public String getClassName() {
		return _className;
	}

	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public boolean hasAttribute(String name) {
		return false;
	}

	@Override
	public boolean isIndexEnabled() {
		return _indexEnabled;
	}

	@Override
	public void setAttribute(String name, Serializable value) {

	}

	@Override
	public void setAttribute(String name, Serializable value, boolean secure) {

	}

	@Override
	public void setAttributeDefault(String name, Serializable defaultValue) {

	}

	@Override
	public void setAttributeProperties(
		String name, UnicodeProperties properties) {

	}

	@Override
	public void setAttributeProperties(
		String name, UnicodeProperties properties, boolean secure) {

	}

	@Override
	public void setAttributes(
		Map<String, Serializable> attributes) {

	}

	@Override
	public void setAttributes(
		Map<String, Serializable> attributes, boolean secure) {

	}

	@Override
	public void setAttributes(
		ServiceContext serviceContext) {

	}

	@Override
	public void setAttributes(
		ServiceContext serviceContext, boolean secure) {

	}

	@Override
	public void setClassName(String className) {
		_className = className;
	}

	@Override
	public void setClassPK(long classPK) {
		_classPK = classPK;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@Override
	public void setIndexEnabled(boolean indexEnabled) {
		_indexEnabled = indexEnabled;
	}


	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private DataDefinitionResource _dataDefinitionResource;
	
	private String _className;
	private long _classPK;
	private long _companyId;
	private boolean _indexEnabled;
	private final long _companyGroupId;

}
