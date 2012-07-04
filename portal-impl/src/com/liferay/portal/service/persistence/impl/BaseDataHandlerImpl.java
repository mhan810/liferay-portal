/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.service.persistence.impl;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.DataHandlerContextThreadLocal;
import com.liferay.portal.kernel.lar.PortletDataContextListener;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.lar.XStreamWrapper;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.AuditedModel;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.ResourcedModel;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.persistence.BaseDataHandler;
import com.liferay.portal.service.persistence.lar.BookmarksEntryDataHandler;
import com.liferay.portal.service.persistence.lar.BookmarksFolderDataHandler;
import com.liferay.portal.service.persistence.lar.BookmarksPortletDataHandler;
import com.liferay.portal.service.persistence.lar.ImageDataHandler;
import com.liferay.portal.service.persistence.lar.JournalArticleDataHandler;
import com.liferay.portal.service.persistence.lar.JournalStructureDataHandler;
import com.liferay.portal.service.persistence.lar.JournalTemplateDataHandler;
import com.liferay.portal.service.persistence.lar.PortletDataHandler;
import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mate Thurzo
 */
public class BaseDataHandlerImpl<T extends BaseModel<T>>
	implements BaseDataHandler<T> {

	public void addZipEntry(String path, T object) throws SystemException {
		addZipEntry(path, toXML(object));
	}

	public void addZipEntry(String path, byte[] bytes) throws SystemException {
		if (_portletDataContextListener != null) {
			_portletDataContextListener.onAddZipEntry(path);
		}

		try {
			ZipWriter zipWriter = getZipWriter();

			zipWriter.addEntry(path, bytes);
		}
		catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	public void addZipEntry(String path, InputStream is)
		throws SystemException {

		if (_portletDataContextListener != null) {
			_portletDataContextListener.onAddZipEntry(path);
		}

		try {
			ZipWriter zipWriter = getZipWriter();

			zipWriter.addEntry(path, is);
		}
		catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	public void addZipEntry(String path, Object object) throws SystemException {
		addZipEntry(path, toXML(object));
	}

	public void addZipEntry(String path, String s) throws SystemException {
		if (_portletDataContextListener != null) {
			_portletDataContextListener.onAddZipEntry(path);
		}

		try {
			ZipWriter zipWriter = getZipWriter();

			zipWriter.addEntry(path, s);
		}
		catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	public void addZipEntry(String path, StringBuilder sb)
			throws SystemException {

		if (_portletDataContextListener != null) {
			_portletDataContextListener.onAddZipEntry(path);
		}

		try {
			ZipWriter zipWriter = getZipWriter();

			zipWriter.addEntry(path, sb);
		}
		catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	public void addExpando(T object) throws SystemException {
		if (!(object instanceof ClassedModel)) {
			return;
		}

		ClassedModel classedModel = (ClassedModel)object;

		String expandoPath = getEntityPath(object);
		expandoPath = StringUtil.replace(expandoPath, ".xml", "-expando.xml");

		ExpandoBridge expandoBridge = classedModel.getExpandoBridge();

		addZipEntry(expandoPath, expandoBridge.getAttributes());
	}

	public ServiceContext createServiceContext(
		Element element, ClassedModel classedModel, String namespace) {

		return createServiceContext(element, null, classedModel, namespace);
	}

	public ServiceContext createServiceContext(
		String path, ClassedModel classedModel, String namespace) {

		return createServiceContext(null, path, classedModel, namespace);
	}

	public void digest(T object) throws Exception {
		try {
			doDigest(object);

			String path = getEntityPath(object);

			getDataHandlerContext().addProcessedPath(path);
		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	public Object fromXML(byte[] bytes) {
		if ((bytes == null) || (bytes.length == 0)) {
			return null;
		}

		return getXStreamWrapper().fromXML(new String(bytes));
	}

	public Object fromXML(String xml) {
		if (Validator.isNull(xml)) {
			return null;
		}

		return getXStreamWrapper().fromXML(xml);
	}

	public String getEntityPath(T object) {
		if (object instanceof BaseModel) {
			BaseModel<T> baseModel = (BaseModel<T>)object;

			Map<String, Object> modelAttributes =
				baseModel.getModelAttributes();

			StringBundler sb = new StringBundler();

			sb.append(StringPool.FORWARD_SLASH);
			sb.append("group");
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(modelAttributes.get("groupId"));
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(baseModel.getModelClassName());
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(baseModel.getPrimaryKeyObj() + ".xml");

			return sb.toString();
		}

		return StringPool.BLANK;
	}

	public List<String> getZipEntries() {
		return getZipReader().getEntries();
	}

	public byte[] getZipEntryAsByteArray(String path) {
		if (_portletDataContextListener != null) {
			_portletDataContextListener.onGetZipEntry(path);
		}

		return getZipReader().getEntryAsByteArray(path);
	}

	public File getZipEntryAsFile(String path) {
		if (_portletDataContextListener != null) {
			_portletDataContextListener.onGetZipEntry(path);
		}

		return getZipReader().getEntryAsFile(path);
	}

	public InputStream getZipEntryAsInputStream(String path) {
		if (_portletDataContextListener != null) {
			_portletDataContextListener.onGetZipEntry(path);
		}

		return getZipReader().getEntryAsInputStream(path);
	}

	public Object getZipEntryAsObject(String path) {
		return fromXML(getZipEntryAsString(path));
	}

	public String getZipEntryAsString(String path) {
		if (_portletDataContextListener != null) {
			_portletDataContextListener.onGetZipEntry(path);
		}

		return getZipReader().getEntryAsString(path);
	}

	public List<String> getZipFolderEntries() {
		return getZipFolderEntries(StringPool.SLASH);
	}

	public List<String> getZipFolderEntries(String path) {
		return getZipReader().getFolderEntries(path);
	}

	public void importData(LarDigestItem item) {
		try {
			doImport(item);
		}
		catch (Exception e) {
		}
	}

	public void serialize(String classPK) {

		T object = getEntity(classPK);

		if (object == null) {
			return;
		}

		String path = getEntityPath(object);

		try {
			doSerialize(object);

			addExpando(object);
		}
		catch (Exception e) {
		}

	}

	protected ServiceContext createServiceContext(
			Element element, String path, ClassedModel classedModel,
			String namespace) {

		Class<?> clazz = classedModel.getModelClass();
		long classPK = getClassPK(classedModel);

		LarPersistenceContext context =
			LarPersistenceContextThreadLocal.getLarPersistenceContext();

		ServiceContext serviceContext = new ServiceContext();

		// Theme display

		serviceContext.setCompanyId(context.getCompanyId());
		serviceContext.setScopeGroupId(getScopeGroupId());

		// Dates

		if (classedModel instanceof AuditedModel) {
			AuditedModel auditedModel = (AuditedModel)classedModel;

			serviceContext.setCreateDate(auditedModel.getCreateDate());
			serviceContext.setModifiedDate(auditedModel.getModifiedDate());
		}

		// Permissions

		if (!MapUtil.getBoolean(
				context.getParameters(), PortletDataHandlerKeys.PERMISSIONS)) {

			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
		}

		// Asset

		boolean portletMetadataAll = context.getBooleanParameter(
			namespace, PortletDataHandlerKeys.PORTLET_METADATA_ALL);

		if (isResourceMain(classedModel)) {
			if (portletMetadataAll ||
				context.getBooleanParameter(namespace, "categories")) {

				// toDo: add getAssetCategoryIds to context
				//long[] assetCategoryIds = getAssetCategoryIds(clazz, classPK);

				//serviceContext.setAssetCategoryIds(assetCategoryIds);
			}

			if (portletMetadataAll ||
				context.getBooleanParameter(namespace, "tags")) {

				// toDo: add getAssetTagNames to context
				//String[] assetTagNames = getAssetTagNames(clazz, classPK);

				//serviceContext.setAssetTagNames(assetTagNames);
			}
		}

		// Expando

		String expandoPath = null;

		if (element != null) {
			expandoPath = element.attributeValue("expando-path");
		}
		else {
			expandoPath = getExpandoPath(path);
		}

		if (Validator.isNotNull(expandoPath)) {
			try {
				Map<String, Serializable> expandoBridgeAttributes =
					(Map<String, Serializable>)getZipEntryAsObject(expandoPath);

				serviceContext.setExpandoBridgeAttributes(
					expandoBridgeAttributes);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}
			}
		}

		return serviceContext;
	}

	protected long getClassPK(ClassedModel classedModel) {
		if (classedModel instanceof ResourcedModel) {
			ResourcedModel resourcedModel = (ResourcedModel)classedModel;

			return resourcedModel.getResourcePrimKey();
		}
		else {
			return (Long)classedModel.getPrimaryKeyObj();
		}
	}

	protected T getEntity(String classPK) {
		return null;
	}

	public String toXML(Object object) {
		return getXStreamWrapper().toXML(object);
	}

	public void setXstreamWrapper(XStreamWrapper xstreamWrapper) {
		_xStreamWrapper = xstreamWrapper;
	}

	public XStreamWrapper getXStreamWrapper() {
		if (_xStreamWrapper == null) {
			Object o = PortalBeanLocatorUtil.locate("xStreamWrapper");

			if (o != null) {
				_xStreamWrapper = ((XStreamWrapper)o);
			}
		}

		return _xStreamWrapper;
	}

	public ZipReader getZipReader() {
		DataHandlerContext context =
			DataHandlerContextThreadLocal.getDataHandlerContext();

		if (context != null) {
			return context.getZipReader();
		}

		return null;
	}

	public ZipWriter getZipWriter() {
		DataHandlerContext context =
			DataHandlerContextThreadLocal.getDataHandlerContext();

		if (context != null) {
			return context.getZipWriter();
		}

		return null;
	}

	protected void doDigest(T object) throws Exception {
		return;
	}

	protected void doImport(LarDigestItem item) throws Exception{
		return;
	}

	protected void doSerialize(T object) throws Exception {
		String path = getEntityPath(object);

		addZipEntry(path, object);
	}

	protected String getExpandoPath(String path) {
		if (!isValidPath(path)) {
			throw new IllegalArgumentException(
				path + " is located outside of the lar");
		}

		int pos = path.lastIndexOf(".xml");

		if (pos == -1) {
			throw new IllegalArgumentException(
				path + " does not end with .xml");
		}

		return path.substring(0, pos).concat("-expando").concat(
			path.substring(pos));
}

	protected DataHandlerContext getDataHandlerContext() {
		return DataHandlerContextThreadLocal.getDataHandlerContext();
	}

	protected String getLayoutPath(long layoutId) {
		return getRootPath() + ROOT_PATH_LAYOUTS + layoutId;
	}

	protected String getPortletPath(String portletId) {
		return getRootPath() + ROOT_PATH_PORTLETS + portletId;
	}

	protected String getRootPath() {
		return ROOT_PATH_GROUPS + getScopeGroupId();
	}

	protected long getScopeGroupId() {
		DataHandlerContext context =
			getDataHandlerContext();

		if (context != null) {
			return context.getGroupId();
		}

		return 0;
	}

	protected boolean isResourceMain(ClassedModel classedModel) {
		if (classedModel instanceof ResourcedModel) {
			ResourcedModel resourcedModel = (ResourcedModel)classedModel;

			return resourcedModel.isResourceMain();
		}

		return true;
	}

	protected boolean isValidPath(String path) {
		if (path.contains(StringPool.DOUBLE_PERIOD)) {
			return false;
		}

		return true;
	}

	private Log _log = LogFactoryUtil.getLog(BaseDataHandlerImpl.class);
	private PortletDataContextListener _portletDataContextListener;
	private XStreamWrapper _xStreamWrapper;

	@BeanReference(type = JournalArticleDataHandler.class)
	protected JournalArticleDataHandler journalArticleDataHandler;
	@BeanReference(type = JournalStructureDataHandler.class)
	protected JournalStructureDataHandler journalStructureDataHandler;
	@BeanReference(type = JournalTemplateDataHandler.class)
	protected JournalTemplateDataHandler journalTemplateDataHandler;
	@BeanReference(type = ImageDataHandler.class)
	protected ImageDataHandler imageDataHandler;
	@BeanReference(type = BookmarksEntryDataHandler.class)
	protected BookmarksEntryDataHandler bookmarksEntryDataHandler;
	@BeanReference(type = BookmarksFolderDataHandler.class)
	protected BookmarksFolderDataHandler bookmarksFolderDataHandler;
	@BeanReference(type = BookmarksPortletDataHandler.class)
	protected BookmarksPortletDataHandler bookmarksPortletDataHandler;

}
