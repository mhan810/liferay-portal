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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.digest.LarDigest;
import com.liferay.portal.kernel.lar.digest.LarDigestEntry;
import com.liferay.portal.kernel.lar.digest.LarDigestEntryDependency;
import com.liferay.portal.kernel.lar.digest.LarDigestModule;
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
import com.liferay.portal.lar.AssetAction;
import com.liferay.portal.lar.DataHandlersUtil;
import com.liferay.portal.lar.ImageDataHandler;
import com.liferay.portal.lar.LayoutPrototypeDataHandler;
import com.liferay.portal.lar.LayoutSetDataHandler;
import com.liferay.portal.lar.XStreamWrapper;
import com.liferay.portal.model.AuditedModel;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.ResourcedModel;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.asset.lar.AssetCategoryDataHandler;
import com.liferay.portlet.asset.lar.AssetLinkDataHandler;
import com.liferay.portlet.asset.lar.AssetVocabularyDataHandler;
import com.liferay.portlet.bookmarks.lar.BookmarksEntryDataHandler;
import com.liferay.portlet.bookmarks.lar.BookmarksFolderDataHandler;
import com.liferay.portlet.bookmarks.lar.BookmarksPortletDataHandler;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.journal.lar.JournalArticleDataHandler;
import com.liferay.portlet.journal.lar.JournalStructureDataHandler;
import com.liferay.portlet.journal.lar.JournalTemplateDataHandler;

import java.io.IOException;
import java.io.Serializable;

import java.lang.reflect.ParameterizedType;

import java.util.Date;
import java.util.Map;

/**
 * @author Mate Thurzo
 * @author Daniel Kocsis
 */
public abstract class StagedDataHandlerImpl<T extends BaseModel<T>>
	implements StagedDataHandler<T> {

	public void addZipEntry(ZipWriter writer, String path, Object object)
		throws SystemException {

		addZipEntry(writer, path, toXML(object));
	}

	public void addZipEntry(ZipWriter writer, String path, String s)
		throws SystemException {

		if (_portletDataContextListener != null) {
			_portletDataContextListener.onAddZipEntry(path);
		}

		try {
			writer.addEntry(path, s);
		}
		catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	public void addZipEntry(ZipWriter writer, String path, T object)
		throws SystemException {

		addZipEntry(writer, path, toXML(object));
	}

	public ServiceContext createServiceContext(
		String path, ClassedModel classedModel, String namespace,
		DataHandlerContext context) {

		return createServiceContext(
			null, path, classedModel, namespace, context);
	}

	public void export(
			T object, DataHandlerContext context,
			LarDigestModule parentPortletModule)
		throws Exception {

		return;
	}

	public String getNamespace() {
		return StringPool.BLANK;
	}

	public String getPermissionResourceName() {
		ParameterizedType type =
			(ParameterizedType)getClass().getGenericSuperclass();

		return type.getClass().getName();
	}

	public XStreamWrapper getXstreamWrapper() {
		if (_xStreamWrapper == null) {
			Object o = PortalBeanLocatorUtil.locate("xStreamWrapper");

			if (o != null) {
				_xStreamWrapper = ((XStreamWrapper)o);
			}
		}

		return _xStreamWrapper;
	}

	public byte[] getZipEntryAsByteArray(ZipReader reader, String path) {
		if (_portletDataContextListener != null) {
			_portletDataContextListener.onGetZipEntry(path);
		}

		return reader.getEntryAsByteArray(path);
	}

	public Object getZipEntryAsObject(ZipReader reader, String path) {
		return fromXML(getZipEntryAsString(reader, path));
	}

	public String getZipEntryAsString(ZipReader reader, String path) {
		if (_portletDataContextListener != null) {
			_portletDataContextListener.onGetZipEntry(path);
		}

		return reader.getEntryAsString(path);
	}

	public void importData(LarDigestEntry entry, DataHandlerContext context)
		throws Exception {

		return;
	}

	public void setXstreamWrapper(XStreamWrapper xStreamWrapper) {
		_xStreamWrapper = xStreamWrapper;
	}

	protected void addExpando(T object, DataHandlerContext context)
		throws SystemException {

		if (!(object instanceof ClassedModel)) {
			return;
		}

		ClassedModel classedModel = (ClassedModel)object;

		String expandoPath = ExportImportPathUtil.getEntityPath(object);
		expandoPath = StringUtil.replace(expandoPath, ".xml", "-expando.xml");

		ExpandoBridge expandoBridge = classedModel.getExpandoBridge();

		addZipEntry(
			context.getZipWriter(), expandoPath, expandoBridge.getAttributes());
	}

	protected ServiceContext createServiceContext(
		Element element, String path, ClassedModel classedModel,
		String namespace, DataHandlerContext context) {

		Class<?> clazz = classedModel.getModelClass();
		long classPK = getClassPK(classedModel);

		ServiceContext serviceContext = new ServiceContext();

		// Theme display

		serviceContext.setCompanyId(context.getCompanyId());
		serviceContext.setScopeGroupId(context.getScopeGroupId());

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
					(Map<String, Serializable>)getZipEntryAsObject(
							context.getZipReader(), expandoPath);

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

	protected Object fromXML(String xml) {
		if (Validator.isNull(xml)) {
			return null;
		}

		return getXstreamWrapper().fromXML(xml);
	}

	protected AssetAction getAssetAction(
		DataHandlerContext context, Object object) {

		if (!(object instanceof StagedModel)) {
			return AssetAction.ADD;
		}

		StagedModel stagedModel = (StagedModel)object;

		Date createDate = stagedModel.getCreateDate();
		Date modifiedDate = stagedModel.getModifiedDate();

		if (!context.isWithinDateRange(stagedModel.getModifiedDate())) {
			return AssetAction.IGNORE;
		}

		//todo - tbd need to add algorithm for
		// determineing AssetAction.ARCHIVE

		if (modifiedDate.after(createDate)) {
			return AssetAction.UPDATE;
		}

		return AssetAction.ADD;
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

	protected String getPortletPath(String portletId) {
		StringBundler sb = new StringBundler();

		sb.append(ROOT_PATH_PORTLETS);
		sb.append(portletId);
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(portletId + ".xml");

		return sb.toString();
	}

	protected String getRootPath(long groupId) {
		return ROOT_PATH_GROUPS + groupId;
	}

	protected String getSourcePortletPath(long groupid, String portletId) {
		return getSourceRootPath(groupid) + ROOT_PATH_PORTLETS + portletId;
	}

	protected String getSourceRootPath(long groupId) {
		return ROOT_PATH_GROUPS + groupId;
	}

	protected void importDependencies(
			LarDigestEntry entry, DataHandlerContext context)
		throws Exception {

		LarDigest digest = context.getLarDigest();

		for (LarDigestEntryDependency dependency : entry.getDependencies()) {

			LarDigestEntry dependentEntry = digest.findDigestItem(
				null, null, dependency.getClassName(), dependency.getUuid(),
				dependency.getUuid());

			StagedDataHandler dataHandler =
				DataHandlersUtil.getDataHandlerInstance(
					dependentEntry.getClassName());

			dataHandler.importData(dependentEntry, context);
		}
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

	protected void serialize(T object, DataHandlerContext context) {
		if (object == null) {
			return;
		}

		try {
			String path = ExportImportPathUtil.getEntityPath(object);

			addZipEntry(context.getZipWriter(), path, object);

			addExpando(object, context);
		}
		catch (Exception e) {
		}
	}

	protected String toXML(Object object) {
		return getXstreamWrapper().toXML(object);
	}

	private Log _log = LogFactoryUtil.getLog(StagedDataHandlerImpl.class);
	private PortletDataContextListener _portletDataContextListener;
	private XStreamWrapper _xStreamWrapper;

	@BeanReference(type = AssetCategoryDataHandler.class)
	protected AssetCategoryDataHandler assetCategoryDataHandler;
	@BeanReference(type = AssetLinkDataHandler.class)
	protected AssetLinkDataHandler assetLinkDataHandler;
	@BeanReference(type = AssetVocabularyDataHandler.class)
	protected AssetVocabularyDataHandler assetVocabularyDataHandler;
	@BeanReference(type = BookmarksEntryDataHandler.class)
	protected BookmarksEntryDataHandler bookmarksEntryDataHandler;
	@BeanReference(type = BookmarksFolderDataHandler.class)
	protected BookmarksFolderDataHandler bookmarksFolderDataHandler;
	@BeanReference(type = BookmarksPortletDataHandler.class)
	protected BookmarksPortletDataHandler bookmarksPortletDataHandler;
	@BeanReference(type = ImageDataHandler.class)
	protected ImageDataHandler imageDataHandler;
	@BeanReference(type = JournalArticleDataHandler.class)
	protected JournalArticleDataHandler journalArticleDataHandler;
	@BeanReference(type = JournalStructureDataHandler.class)
	protected JournalStructureDataHandler journalStructureDataHandler;
	@BeanReference(type = JournalTemplateDataHandler.class)
	protected JournalTemplateDataHandler journalTemplateDataHandler;
	@BeanReference(type = LayoutPrototypeDataHandler.class)
	protected LayoutPrototypeDataHandler layoutPrototypeDataHandler;
	@BeanReference(type = LayoutSetDataHandler.class)
	protected LayoutSetDataHandler layoutSetDataHandler;

}