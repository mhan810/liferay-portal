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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.*;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.xml.StAXReaderUtil;
import com.liferay.portal.xml.StAXWriterUtil;
import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import jcifs.dcerpc.msrpc.lsarpc;

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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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

		_xmlEventFactory = XMLEventFactory.newInstance();

		if (_digestFile == null) {
			_digestFile = getDigestFile();
		}
	}

	public void addMetaData(Map<String, String> metadata) throws Exception {
		_xmlStreamWriter.writeStartElement(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		for (String key : metadata.keySet()) {
			_xmlStreamWriter.writeStartElement(
				LarDigesterConstants.NODE_METADATA_LABEL);
			_xmlStreamWriter.writeAttribute(key, metadata.get(key));
			_xmlStreamWriter.writeEndElement();
		}

		_xmlStreamWriter.writeEndElement();
	}

	public void addPermissions(Map<String, List<String>> permissions)
		throws Exception {

		_xmlStreamWriter.writeStartElement(
			LarDigesterConstants.NODE_PERMISSIONS_LABEL);

		for (String key : permissions.keySet()) {
			_xmlStreamWriter.writeStartElement(
				LarDigesterConstants.NODE_PERMISSION_LABEL);
			for(String action : permissions.get(key)) {
				_xmlStreamWriter.writeStartElement(
					LarDigesterConstants.NODE_ACTION_KEY_LABEL);
				_xmlStreamWriter.writeCharacters(action);
				_xmlStreamWriter.writeEndElement();
			}
			_xmlStreamWriter.writeEndElement();
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

		if (_xmlEventReader != null) {
			_xmlEventReader.close();
		}
	}

	public List<LarDigestItem> findDigestItems(
		int action, String path, String type, String classPK) {

		List result = new ArrayList<LarDigestItem>();

		try {
			if (_document == null) {
				_document = SAXReaderUtil.read(getDigestString());
			}

			Element rootElement = _document.getRootElement();

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

				LarDigestItem digestItem = new LarDigestItemImpl();

				Element element = digestElement.element(
					LarDigesterConstants.NODE_ACTION_LABEL);
				digestItem.setAction(GetterUtil.getInteger(element.getText()));

				element = digestElement.element(
					LarDigesterConstants.NODE_CLASS_PK_LABEL);
				digestItem.setClassPK(element.getText());

				element = digestElement.element(
					LarDigesterConstants.NODE_PATH_LABEL);
				digestItem.setPath(element.getText());

				element = digestElement.element(
					LarDigesterConstants.NODE_TYPE_LABEL);
				digestItem.setType(element.getText());

				result.add(digestItem);
			}
		}
		catch (Exception ex) {
			_log.warn(ex);
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
				return null;
			}
		}

		return null;
	}

	public Map<String, String> getMetaData() throws Exception {
		XMLEvent event = null;

		HashMap<String, String> metadata = new HashMap<String, String>();

		try {
			XMLInputFactory xmlInputFactory =
				StAXReaderUtil.getXMLInputFactory();

			InputStream inputStream = new FileInputStream(getDigestFile());

			_xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

			while (_xmlEventReader.hasNext()) {
				event = _xmlEventReader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();

					String elementName = startElement.getName().getLocalPart();

					if (elementName.equals(
							LarDigesterConstants.NODE_METADATA_LABEL)) {

						Iterator attributesIt = startElement.getAttributes();

						while (attributesIt.hasNext()) {
						 	Attribute attribute =
								 (Attribute) attributesIt.next();

							metadata.put(
								attribute.getName().getLocalPart(),
								attribute.getValue());
						}
					}
				}
				else if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();

					String elementName = endElement.getName().getLocalPart();

					if (elementName.equals(
							LarDigesterConstants.NODE_METADATA_SET_LABEL)) {

						return metadata;
					}
				}
			}

			return null;
		}
		catch (Exception ex) {
			_log.error(ex);

			return null;
		}
		finally {
			if (_xmlEventReader != null) {
				_xmlEventReader.close();
			}
		}
	}

	public Iterator<LarDigestItem> iterator() {
		XMLInputFactory xmlInputFactory = StAXReaderUtil.getXMLInputFactory();

		try {
			InputStream inputStream = new FileInputStream(getDigestFile());

			_xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

			return new LarDigestIterator(_xmlEventReader);
		}
		catch (Exception e) {
			return null;
		}
	}

	public void write(LarDigestItem digestItem) throws Exception {

		try {
			_xmlStreamWriter.writeStartElement(
					LarDigesterConstants.NODE_DIGEST_ITEM_LABEL);

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

			_xmlStreamWriter.writeEndElement();
		}
		finally {
			try {
				_xmlStreamWriter.flush();
			}
			catch (Exception ex) {
				throw new PortalException(ex.getMessage());
			}
		}
	}

	protected void addXmlNode(String name, String value)
		throws XMLStreamException {

		_xmlStreamWriter.writeStartElement(name);
		_xmlStreamWriter.writeCharacters(value);
		_xmlStreamWriter.writeEndElement();
	}

	protected EndElement createEndElement(String name) {
		QName qName = new QName(name);

		EndElement endElement = _xmlEventFactory.createEndElement(qName, null);

		return endElement;
	}

	protected StartElement createStartElement(String name) {
		QName qName = new QName(name);

		StartElement startElement = _xmlEventFactory.createStartElement(
			qName, null, null);

		return startElement;
	}

	protected void format(File larDigest) {

		if (larDigest == null) {
			return;
		}

		File formattedLarDigest = null;

		TransformerFactory transformerFactory =
			TransformerFactory.newInstance();

		try {
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");

			formattedLarDigest = FileUtil.createTempFile();

			transformer.transform(
				new StreamSource(larDigest),
				new StreamResult(formattedLarDigest));

			if (formattedLarDigest.length() > 0) {
				FileUtil.copyFile(formattedLarDigest, larDigest);
			}
		}
		catch (Exception ex) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Can't format the " + larDigest.getName() + " file!", ex);
			}
		}
		finally {
			if (formattedLarDigest != null) {
				FileUtil.delete(formattedLarDigest);
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(LarDigest.class);

	private File _digestFile;
	private Document _document;

	private XMLEventFactory _xmlEventFactory;
	private XMLEventReader _xmlEventReader;
	private XMLStreamWriter _xmlStreamWriter;

}