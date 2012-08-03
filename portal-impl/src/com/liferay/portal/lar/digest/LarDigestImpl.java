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

package com.liferay.portal.lar.digest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.*;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.xml.StAXReaderUtil;
import com.liferay.portal.xml.StAXWriterUtil;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author Daniel Kocsis
 * @author Mate Thurzo
 */
public class LarDigestImpl implements LarDigest {

	public LarDigestImpl() throws Exception {
		_digestFile = getDigestFile();

		XMLOutputFactory xmlOutputFactory =
			StAXWriterUtil.getXMLOutputFactory();

		OutputStream outputStream = new FileOutputStream(_digestFile);

		_xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(outputStream);

		_xmlStreamWriter = new IndentingXMLStreamWriter(_xmlStreamWriter);

		_xmlStreamWriter.writeStartDocument();

		_xmlStreamWriter.writeStartElement("root");
	}

	public LarDigestImpl(String xmlContent) throws Exception {
		_digestFile = getDigestFile();

		FileUtil.write(_digestFile, xmlContent);
	}

	public void addItem(LarDigestItem item) {
		if (item == null) {
			return;
		}

		_itemList.add(item);
	}

	public void addMetadata(LarDigestMetadata metadata) {
		_metadata.add(metadata);
	}

	public void close() throws Exception {
		if (_xmlStreamWriter != null) {
			_xmlStreamWriter.writeEndElement();
			_xmlStreamWriter.writeEndDocument();
			_xmlStreamWriter.flush();
			_xmlStreamWriter.close();
		}

		if (_digestFile != null) {
			FileUtil.delete(_digestFile);
		}
	}

	public LarDigestItem findDigestItem(
		int action, String path, String type, String classPK) {

		List<LarDigestItem> result =
			findDigestItems(action, path, type, classPK);

		if (result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	public List<LarDigestItem> findDigestItems(
		int action, String path, String type, String classPK) {

		List result = new ArrayList<LarDigestItem>();

		try {
			Element rootElement = getDocument().getRootElement();

			StringBundler sb = new StringBundler("./item");

			if (action > 0) {
				sb = sb.append(StringPool.OPEN_BRACKET);
				sb = sb.append(LarDigesterConstants.NODE_ACTION_LABEL);
				sb = sb.append(StringPool.EQUAL);
				sb = sb.append("'" + action + "'");
				sb = sb.append(StringPool.CLOSE_BRACKET);
			}

			if (Validator.isNotNull(path)) {
				sb = sb.append(StringPool.OPEN_BRACKET);
				sb = sb.append(LarDigesterConstants.NODE_PATH_LABEL);
				sb = sb.append(StringPool.EQUAL);
				sb = sb.append("'" + path + "'");
				sb = sb.append(StringPool.CLOSE_BRACKET);
			}

			if (Validator.isNotNull(type)) {
				sb = sb.append(StringPool.OPEN_BRACKET);
				sb = sb.append(LarDigesterConstants.NODE_TYPE_LABEL);
				sb = sb.append(StringPool.EQUAL);
				sb = sb.append("'" + type + "'");
				sb = sb.append(StringPool.CLOSE_BRACKET);
			}

			if (Validator.isNotNull(classPK)) {
				sb = sb.append(StringPool.OPEN_BRACKET);
				sb = sb.append(LarDigesterConstants.NODE_CLASS_PK_LABEL);
				sb = sb.append(StringPool.EQUAL);
				sb = sb.append("'" + classPK + "'");
				sb = sb.append(StringPool.CLOSE_BRACKET);
			}

			for (Node node : rootElement.selectNodes(sb.toString())) {
				Element digestElement = (Element)node;

				LarDigestItem digestItem = fetchDigestItem(digestElement);

				result.add(digestItem);
			}
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("Cannot find item in the digest: " +
					e.getMessage());
			}
		}

		return result;
	}

	public List<LarDigestItem> getAllItems() {
		if(_itemList.isEmpty()) {
			Element root = getDocument().getRootElement();

			for(Element itemEl : root.elements(
					LarDigesterConstants.NODE_DIGEST_ITEM_LABEL)) {

				_itemList.add(fetchDigestItem(itemEl));
			}
		}

		return _itemList;
	}

	public File getDigestFile() {
		if (_digestFile == null) {
			String path =
				SystemProperties.get(SystemProperties.TMP_DIR) +
					StringPool.SLASH + "digest_" + PortalUUIDUtil.generate() +
					".xml";

			return new File(path);
		}

		return _digestFile;
	}

	public String getDigestString() {
		if (_digestFile != null) {
			try {
				return new String(FileUtil.getBytes(_digestFile));
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug("Cannot get the digest content as a String: " +
							e.getMessage());
				}

				return null;
			}
		}

		return null;
	}

	public Document getDocument() {
		if (_document == null) {
			try {
				_document = SAXReaderUtil.read(getDigestString());
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug("Cannot get the XML document of the digest: " +
						e.getMessage());
				}
			}
		}

		return _document;
	}

	public List<LarDigestMetadata> getMetaData() {
		Element root = getDocument().getRootElement();

		return getMetaData(root);
	}

	public String getMetadataValue(String name) {
		for (LarDigestMetadata metadata : getMetaData()) {
			String metadataName = metadata.getName();

			if(metadataName.equals(name)) {
				return metadata.getValue();
			}
		}

		return null;
	}

	public void write() throws Exception {
		addMetaData(_metadata);

		for (LarDigestItem item : _itemList) {
			write(item);
		}
	}

	public void write(LarDigestItem digestItem) throws Exception {
		_xmlStreamWriter.writeStartElement(
			LarDigesterConstants.NODE_DIGEST_ITEM_LABEL);

		try {
			addMetaData(digestItem.getMetadata());

			addDependencies(digestItem.getDependencies());

			addXmlNode(
				LarDigesterConstants.NODE_PATH_LABEL, digestItem.getPath());

			addXmlNode(
				LarDigesterConstants.NODE_ACTION_LABEL,
				StringUtil.valueOf(digestItem.getAction()));

			addXmlNode(
				LarDigesterConstants.NODE_TYPE_LABEL, digestItem.getType());

			addXmlNode(
				LarDigesterConstants.NODE_CLASS_PK_LABEL,
				digestItem.getClassPK());

			addPermissions(digestItem.getPermissions());
		}
		finally {
			try {
				_xmlStreamWriter.writeEndElement();
				_xmlStreamWriter.flush();
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug("Cannot add new item into the digest: " +
						e.getMessage());
				}
			}
		}
	}

	protected void addDependencies(List<LarDigestDependency> dependencies)
		throws Exception{

		_xmlStreamWriter.writeStartElement("dependencies");

		for (LarDigestDependency dependency : dependencies) {
			_xmlStreamWriter.writeStartElement("dependency");
			_xmlStreamWriter.writeStartElement("className");
			_xmlStreamWriter.writeCharacters(dependency.getClassName());
			_xmlStreamWriter.writeEndElement();
			_xmlStreamWriter.writeStartElement("uuid");
			_xmlStreamWriter.writeCharacters(dependency.getUuid());
			_xmlStreamWriter.writeEndElement();
			_xmlStreamWriter.writeEndElement();
		}

		_xmlStreamWriter.writeEndElement();
	}

	protected void addMetaData(List<LarDigestMetadata> metadata)
		throws Exception {

		_xmlStreamWriter.writeStartElement(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		for (LarDigestMetadata metadataItem : metadata) {
			_xmlStreamWriter.writeStartElement(
				LarDigesterConstants.NODE_METADATA_LABEL);
			_xmlStreamWriter.writeAttribute(
				metadataItem.getName(), metadataItem.getValue());
			_xmlStreamWriter.writeEndElement();
		}

		_xmlStreamWriter.writeEndElement();
	}

	protected void addPermissions(List<LarDigestPermission> permissions)
		throws Exception {

		_xmlStreamWriter.writeStartElement(
			LarDigesterConstants.NODE_PERMISSIONS_LABEL);

		for (LarDigestPermission permission : permissions) {
			_xmlStreamWriter.writeStartElement(
				LarDigesterConstants.NODE_PERMISSION_LABEL);
			_xmlStreamWriter.writeAttribute(
				LarDigesterConstants.ATTRIBUTE_NAME_ROLE,
				permission.getRoleName());

			for (String action : permission.getActionIds()) {
				if (Validator.isNull(action)) {
					continue;
				}

				_xmlStreamWriter.writeStartElement(
					LarDigesterConstants.NODE_ACTION_KEY_LABEL);
				_xmlStreamWriter.writeCharacters(action);
				_xmlStreamWriter.writeEndElement();
			}

			_xmlStreamWriter.writeEndElement();
		}

		_xmlStreamWriter.writeEndElement();
	}

	protected void addXmlNode(String name, String value)
		throws XMLStreamException {

		_xmlStreamWriter.writeStartElement(name);
		_xmlStreamWriter.writeCharacters(value);
		_xmlStreamWriter.writeEndElement();
	}

	protected LarDigestItem fetchDigestItem(Element root) {
		LarDigestItem digestItem = new LarDigestItemImpl();

		for(LarDigestMetadata metadata : getMetaData(root)) {
			digestItem.addMetadata(metadata);
		}

		for(LarDigestDependency dependency : getDependencies(root)) {
			digestItem.addDependency(dependency);
		}

		Element element = root.element(LarDigesterConstants.NODE_ACTION_LABEL);
		digestItem.setAction(GetterUtil.getInteger(element.getText()));

		element = root.element(LarDigesterConstants.NODE_CLASS_PK_LABEL);
		digestItem.setClassPK(element.getText());

		element = root.element(LarDigesterConstants.NODE_PATH_LABEL);
		digestItem.setPath(element.getText());

		element = root.element(LarDigesterConstants.NODE_TYPE_LABEL);
		digestItem.setType(element.getText());

		for(LarDigestDependency dependency : getDependencies(root)) {
			digestItem.addDependency(dependency);
		}

		return digestItem;
	}

	protected List<LarDigestDependency> getDependencies(Element root) {
		List<LarDigestDependency> result = new ArrayList<LarDigestDependency>();

		Element dependencyElement = root.element("dependencies");

		List<Element> dependencyElements =
			dependencyElement.elements("dependency");

		for (Element dependencyEl : dependencyElements) {
			Element classNameEl= dependencyEl.element("className");
			Element uuidEl= dependencyEl.element("uuid");

			result.add(new LarDigestDependencyImpl(
				classNameEl.getText(), uuidEl.getText()));
		}

		return result;
	}

	public List<LarDigestMetadata> getMetaData(Element root) {
		Element metadataSetElement = root.element(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		List<LarDigestMetadata> result = new ArrayList<LarDigestMetadata>();

		List<Element> metadataElements = metadataSetElement.elements(
			LarDigesterConstants.NODE_METADATA_LABEL);

		for (Element metadataEl : metadataElements) {
			Attribute metadataAt = metadataEl.attribute(0);

			result.add(new LarDigestMetadataImpl(
				metadataAt.getName(), metadataAt.getValue()));
		}

		return result;
	}


	protected List<LarDigestPermission> getPermissions(Element root) {
		Element permissionsElement = root.element(
			LarDigesterConstants.NODE_PERMISSIONS_LABEL);

		List<LarDigestPermission> result = new ArrayList<LarDigestPermission>();

		List<Element> permissionElements = permissionsElement.elements(
			LarDigesterConstants.NODE_PERMISSION_LABEL);

		for (Element permissionEl : permissionElements) {
			LarDigestPermission permission = new LarDigestPermissionImpl();

			Attribute role = permissionEl.attribute(
				LarDigesterConstants.ATTRIBUTE_NAME_ROLE);

			permission.setRoleName(role.getText());

			List<Element> actionNameElements = permissionEl.elements(
				LarDigesterConstants.NODE_ACTION_KEY_LABEL);

			for (Element actionId : actionNameElements) {
				permission.addActionId(actionId.getText());
			}

			result.add(permission);
		}

		return result;
	}

	private static Log _log = LogFactoryUtil.getLog(LarDigest.class);

	private File _digestFile;
	private Document _document;

	private List<LarDigestItem> _itemList = new ArrayList<LarDigestItem>();
	private List<LarDigestMetadata> _metadata =
		new ArrayList<LarDigestMetadata>();

	private XMLStreamWriter _xmlStreamWriter;

}