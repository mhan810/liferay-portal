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

import javax.xml.stream.XMLEventReader;
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
		this(null);

		XMLOutputFactory xmlOutputFactory =
			StAXWriterUtil.getXMLOutputFactory();

		OutputStream outputStream = new FileOutputStream(_digestFile);

		_xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(outputStream);

		_xmlStreamWriter = new IndentingXMLStreamWriter(_xmlStreamWriter);

		_xmlStreamWriter.writeStartDocument();

		_xmlStreamWriter.writeStartElement("root");
	}

	public LarDigestImpl(File digest) throws Exception {
		_digestFile = digest;

		if (_digestFile == null) {
			_digestFile = getDigestFile();
		}
	}

	public void addMetaData(Map<String, String> metadata) throws Exception {
		_xmlStreamWriter.writeStartElement(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		try {
			for (String key : metadata.keySet()) {
				_xmlStreamWriter.writeStartElement(
					LarDigesterConstants.NODE_METADATA_LABEL);
				_xmlStreamWriter.writeAttribute(key, metadata.get(key));
				_xmlStreamWriter.writeEndElement();
			}
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("Metadata is not available to digest: " +
					e.getMessage());
			}
		}
		finally {
			_xmlStreamWriter.writeEndElement();
		}
	}

	public void addPermissions(Map<String, List<String>> permissions)
		throws Exception {

		_xmlStreamWriter.writeStartElement(
			LarDigesterConstants.NODE_PERMISSIONS_LABEL);

		try {
			for (String key : permissions.keySet()) {
				_xmlStreamWriter.writeStartElement(
					LarDigesterConstants.NODE_PERMISSION_LABEL);
				_xmlStreamWriter.writeAttribute(
					LarDigesterConstants.ATTRIBUTE_NAME_ROLE, key);

				List<String> actions = permissions.get(key);

				if (actions == null) {
					_xmlStreamWriter.writeEndElement();
					break;
				}

				for(String action : actions) {
					_xmlStreamWriter.writeStartElement(
						LarDigesterConstants.NODE_ACTION_KEY_LABEL);
					_xmlStreamWriter.writeCharacters(action);
					_xmlStreamWriter.writeEndElement();
				}

				_xmlStreamWriter.writeEndElement();
			}
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("Permissions not available to digest: " +
					e.getMessage());
			}
		}

		_xmlStreamWriter.writeEndElement();
	}

	public void close() throws Exception {
		if (_xmlStreamWriter != null) {
			_xmlStreamWriter.writeEndElement();
			_xmlStreamWriter.writeEndDocument();
			_xmlStreamWriter.flush();
			_xmlStreamWriter.close();
		}

		if (_xmlStreamReader != null) {
			_xmlStreamReader.close();
		}
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


	public Map<String, String> getMetaData() {
		Element root = _document.getRootElement();

		return getMetaData(root);
	}

	public Iterator<LarDigestItem> iterator() {
		XMLInputFactory xmlInputFactory = StAXReaderUtil.getXMLInputFactory();

		try {
			InputStream inputStream = new FileInputStream(getDigestFile());

			_xmlStreamReader = xmlInputFactory.createXMLStreamReader(
				inputStream);

			return new LarDigestIterator(_xmlStreamReader);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("Cannot get the digest iterator: " + e.getMessage());
			}
			return null;
		}
	}

	public void write(LarDigestItem digestItem) throws Exception {
		_xmlStreamWriter.writeStartElement(
			LarDigesterConstants.NODE_DIGEST_ITEM_LABEL);

		try {
			addMetaData(digestItem.getMetadata());

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

	protected void addXmlNode(String name, String value)
		throws XMLStreamException {

		_xmlStreamWriter.writeStartElement(name);
		_xmlStreamWriter.writeCharacters(value);
		_xmlStreamWriter.writeEndElement();
	}

	protected LarDigestItem fetchDigestItem(Element root) {
		LarDigestItem digestItem = new LarDigestItemImpl();

		Element element = root.element(LarDigesterConstants.NODE_ACTION_LABEL);
		digestItem.setAction(GetterUtil.getInteger(element.getText()));

		element = root.element(LarDigesterConstants.NODE_CLASS_PK_LABEL);
		digestItem.setClassPK(element.getText());

		element = root.element(LarDigesterConstants.NODE_PATH_LABEL);
		digestItem.setPath(element.getText());

		element = root.element(LarDigesterConstants.NODE_TYPE_LABEL);
		digestItem.setType(element.getText());

		digestItem.setPermissions(getPermissions(root));

		digestItem.setMetadata(getMetaData(root));

		return digestItem;
	}

	public Map<String, String> getMetaData(Element element) {
		Element metadataSetElement = element.element(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		if (metadataSetElement == null) {
			return null;
		}

		Map metadata = new HashMap<String, String>();

		List<Element> metadataElements = metadataSetElement.elements(
			LarDigesterConstants.NODE_METADATA_LABEL);

		if (metadataElements == null || metadataElements.isEmpty()) {
			return null;
		}

		for (Element metadataEl : metadataElements) {
			Attribute metadataAt = metadataEl.attribute(0);
			metadata.put(metadataAt.getName(), metadataAt.getValue());
		}

		return metadata;
	}

	protected Map getPermissions(Element element) {
		Element permissionsElement = element.element(
			LarDigesterConstants.NODE_PERMISSIONS_LABEL);

		if (permissionsElement == null) {
			return null;
		}

		Map permissions = new HashMap<String, List<String>>();

		List<Element> permissionElements = permissionsElement.elements(
			LarDigesterConstants.NODE_PERMISSION_LABEL);

		for (Element permissionEl : permissionElements) {
			Attribute role = permissionEl.attribute(
				LarDigesterConstants.ATTRIBUTE_NAME_ROLE);

			List<Element> actionNameElements = permissionEl.elements(
				LarDigesterConstants.NODE_ACTION_KEY_LABEL);

			List actionNames = new ArrayList<String>();

			for(Element actionName : actionNameElements) {
				actionNames.add(actionName.getText());
			}

			permissions.put(role.getValue(), actionNames);
		}

		return permissions;
	}

	private static Log _log = LogFactoryUtil.getLog(LarDigest.class);

	private File _digestFile;
	private Document _document;

	private XMLStreamReader _xmlStreamReader;
	private XMLStreamWriter _xmlStreamWriter;

}