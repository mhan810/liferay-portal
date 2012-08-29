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

package com.liferay.portal.kernel.lar.digest;

import com.liferay.portal.kernel.lar.LarDigesterConstants;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.lar.AssetAction;
import com.liferay.portal.xml.StAXWriterUtil;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
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

	public void addMetadata(LarDigestMetadata metadata) {
		if (_metadata.contains(metadata)) {
			return;
		}

		_metadata.add(metadata);
	}

	public void addModule(LarDigestModule module) {
		if ((module == null) || _moduleList.contains(module)) {
			return;
		}

		_moduleList.add(module);
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

	public LarDigestEntry findDigestItem(
		AssetAction action, String path, String className, String classPK,
		String uuid) {

		List<LarDigestEntry> result = findDigestItems(
			action, path, className, classPK, uuid);

		if (result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	public List<LarDigestEntry> findDigestItems(
		AssetAction action, String path, String className, String classPK,
		String uuid) {

		if (Validator.isNull(getDigestString())) {
			return doFindDigestEntriesInObject(
				action, path, className, classPK, uuid);
		}

		return doFindDigestEntriesInXml(action, path, className, classPK, uuid);
	}

	public LarDigestModule findDigestModule(String moduleName) {
		if (Validator.isNull(getDigestString())) {
			return doFindDigestModuleInObject(moduleName);
		}

		return doFindDigestModuleInXml(moduleName);
	}

	public List<LarDigestModule> getAllModules() {
		if (_moduleList.isEmpty()) {
			Element root = getDocument().getRootElement();

			for (Element moduleEl : root.elements(
					LarDigesterConstants.NODE_MODULE_LABEL)) {

				_moduleList.add(new LarDigestModuleImpl(moduleEl));
			}
		}

		return _moduleList;
	}

	public List<LarDigestModule> getAllPortletModules() {
		List<LarDigestModule> result = new ArrayList<LarDigestModule>();

		for (LarDigestModule module : getAllModules()) {
			try {
				Integer.valueOf(module.getName());
			}
			catch (NumberFormatException nfe) {
				continue;
			}

			result.add(module);
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

	public List<LarDigestMetadata> getMetaData() {
		Element root = getDocument().getRootElement();

		Element metadataRootEl = root.element(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		List<Element> metadatasEl = metadataRootEl.elements(
			LarDigesterConstants.NODE_METADATA_LABEL);

		List<LarDigestMetadata> result = new ArrayList<LarDigestMetadata>();

		for (Element metadataEl : metadatasEl) {
			result.add(new LarDigestMetadataImpl(metadataEl));
		}

		return result;
	}

	public String getMetadataValue(String name) {
		for (LarDigestMetadata metadata : getMetaData()) {
			String metadataName = metadata.getName();

			if (metadataName.equals(name)) {
				return metadata.getValue();
			}
		}

		return null;
	}

	public void write() throws Exception {
		_xmlStreamWriter.writeStartElement(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		for (LarDigestMetadata metadataItem : _metadata) {
			metadataItem.serialize(_xmlStreamWriter);
		}

		_xmlStreamWriter.writeEndElement();

		for (LarDigestModule module : _moduleList) {
			module.serialize(_xmlStreamWriter);
		}
	}

	protected List<LarDigestEntry> doFindDigestEntriesInObject(
		AssetAction action, String path, String className, String classPK,
		String uuid) {

		List<LarDigestEntry> result = new ArrayList<LarDigestEntry>();

		Set<LarDigestEntry> entries = null;

		for (LarDigestModule module : _moduleList) {

			if (Validator.isNotNull(className)) {
				entries = module.getEntriesByClassName(className);
			}
			else {
				Map<String, Set<LarDigestEntry>> moduleEntries =
					module.getEntries();
				entries = new HashSet<LarDigestEntry>();

				for (String key : moduleEntries.keySet()) {
					entries.addAll(moduleEntries.get(key));
				}
			}

			for (LarDigestEntry entry : entries) {
				if ((action != null) && (action == entry.getAction()) &&
					Validator.isNotNull(path) && path.equals(entry.getPath()) &&
					Validator.isNotNull(classPK) &&
					classPK.equals(entry.getClassPK()) &&
					Validator.isNotNull(uuid) && uuid.equals(entry.getUuid())) {

					result.add(entry);
				}
			}
		}

		return result;
	}

	protected List<LarDigestEntry> doFindDigestEntriesInXml(
		AssetAction action, String path, String type, String classPK,
		String uuid) {

		List result = new ArrayList<LarDigestEntry>();

		try {
			Element rootElement = getDocument().getRootElement();

			StringBundler sb = new StringBundler("//entry");

			if (action != null) {
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
				sb = sb.append(LarDigesterConstants.NODE_CLASS_NAME_LABEL);
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

			if (Validator.isNotNull(uuid)) {
				sb = sb.append(StringPool.OPEN_BRACKET);
				sb = sb.append(LarDigesterConstants.NODE_UUID_LABEL);
				sb = sb.append(StringPool.EQUAL);
				sb = sb.append("'" + uuid + "'");
				sb = sb.append(StringPool.CLOSE_BRACKET);
			}

			for (Node node : rootElement.selectNodes(sb.toString())) {
				Element digestElement = (Element)node;

				LarDigestEntry digestEntry = new LarDigestEntryImpl(
					digestElement);

				result.add(digestEntry);
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

	protected LarDigestModule doFindDigestModuleInObject(String moduleName) {

		if (Validator.isNull(moduleName)) {
			return null;
		}

		for (LarDigestModule module : _moduleList) {
			if (module.getName().equals(moduleName)) {
				return module;
			}
		}

		return null;
	}

	protected LarDigestModule doFindDigestModuleInXml(String moduleName) {

		List<LarDigestModule> result = new ArrayList<LarDigestModule>();

		Element rootElement = getDocument().getRootElement();

		StringBundler sb = new StringBundler(5);

		sb = sb.append("//");
		sb = sb.append(LarDigesterConstants.NODE_MODULE_LABEL);
		sb = sb.append("[@name=");
		sb = sb.append(moduleName);
		sb = sb.append("]");

		String xPath = sb.toString();

		Element digestModuleEl = (Element)rootElement.selectSingleNode(xPath);

		if (digestModuleEl == null) {
			return null;
		}

		return new LarDigestModuleImpl(digestModuleEl);
	}

	private static Log _log = LogFactoryUtil.getLog(LarDigest.class);

	private File _digestFile;
	private Document _document;

	private List<LarDigestMetadata> _metadata =
		new ArrayList<LarDigestMetadata>();
	private List<LarDigestModule> _moduleList =
		new ArrayList<LarDigestModule>();

	private XMLStreamWriter _xmlStreamWriter;

}