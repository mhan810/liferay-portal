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

		_xmlEventWriter = xmlOutputFactory.createXMLEventWriter(outputStream);

		_xmlEventWriter.add(_xmlEventFactory.createStartDocument());

		_xmlEventWriter.add(createStartElement("root"));
	}

	public LarDigestImpl(File digest) throws Exception {
		_digestFile = digest;

		_xmlEventFactory = XMLEventFactory.newInstance();

		if (_digestFile == null) {
			_digestFile = getDigestFile();
		}

		initElements();
	}

	public void addMetaData(HashMap<String, String> metadata) throws Exception {
		EndElement metadataEndElement = createEndElement(
			LarDigesterConstants.NODE_METADATA_LABEL);
		StartElement metadataStartElement = createStartElement(
			LarDigesterConstants.NODE_METADATA_LABEL);

		_xmlEventWriter.add(createStartElement(
			LarDigesterConstants.NODE_METADATA_SET_LABEL));

		for (String key : metadata.keySet()) {
			_xmlEventWriter.add(metadataStartElement);

			XMLEvent event = _xmlEventFactory.createAttribute
				(key, metadata.get(key));
			_xmlEventWriter.add(event);

			_xmlEventWriter.add(metadataEndElement);
		}

		_xmlEventWriter.add(createEndElement(
			LarDigesterConstants.NODE_METADATA_SET_LABEL));
	}

	public void close() throws Exception {
		if (_xmlEventWriter != null) {
			_xmlEventWriter.flush();
			_xmlEventWriter.close();
		}

		if (_xmlEventReader != null) {
			_xmlEventReader.close();
		}

		format(_digestFile);
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

	public HashMap<String, String> getMetaData() throws Exception {
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

	public void write(LarDigestItem digestItem)
		throws PortalException, XMLStreamException {

		write(
			digestItem.getAction(), digestItem.getPath(), digestItem.getType(),
			digestItem.getClassPK());
	}

	protected void addXmlNode(String name, String value)
		throws XMLStreamException {

		_xmlEventWriter.add(getStartElement(name));
		_xmlEventWriter.add(_xmlEventFactory.createCharacters(value));
		_xmlEventWriter.add(getEndElement(name));
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

	protected EndElement getEndElement(String name) {
		if ((_endElements == null) || _endElements.isEmpty()) {
			return null;
		}

		return _endElements.get(name);
	}

	protected StartElement getStartElement(String name) {
		if ((_startElements == null) || _startElements.isEmpty()) {
			return null;
		}

		return _startElements.get(name);
	}

	protected void initElements() {
		_endElements = new HashMap<String, EndElement>();
		_startElements = new HashMap<String, StartElement>();

		_endElements.put(
			LarDigesterConstants.NODE_ACTION_LABEL,
			createEndElement(LarDigesterConstants.NODE_ACTION_LABEL));

		_startElements.put(
			LarDigesterConstants.NODE_ACTION_LABEL,
			createStartElement(LarDigesterConstants.NODE_ACTION_LABEL));

		_endElements.put(
			LarDigesterConstants.NODE_CLASS_PK_LABEL,
			createEndElement(LarDigesterConstants.NODE_CLASS_PK_LABEL));

		_startElements.put(
			LarDigesterConstants.NODE_CLASS_PK_LABEL,
			createStartElement(LarDigesterConstants.NODE_CLASS_PK_LABEL));

		_endElements.put(
			LarDigesterConstants.NODE_DIGEST_ITEM_LABEL,
			createEndElement(LarDigesterConstants.NODE_DIGEST_ITEM_LABEL));

		_startElements.put(
			LarDigesterConstants.NODE_DIGEST_ITEM_LABEL,
			createStartElement(LarDigesterConstants.NODE_DIGEST_ITEM_LABEL));

		_endElements.put(
			LarDigesterConstants.NODE_CLASS_PK_LABEL,
			createEndElement(LarDigesterConstants.NODE_CLASS_PK_LABEL));

		_startElements.put(
			LarDigesterConstants.NODE_CLASS_PK_LABEL,
			createStartElement(LarDigesterConstants.NODE_CLASS_PK_LABEL));

		_endElements.put(
			LarDigesterConstants.NODE_PATH_LABEL,
			createEndElement(LarDigesterConstants.NODE_PATH_LABEL));

		_startElements.put(
			LarDigesterConstants.NODE_PATH_LABEL,
			createStartElement(LarDigesterConstants.NODE_PATH_LABEL));

		_endElements.put(
			LarDigesterConstants.NODE_TYPE_LABEL,
			createEndElement(LarDigesterConstants.NODE_TYPE_LABEL));

		_startElements.put(
			LarDigesterConstants.NODE_TYPE_LABEL,
			createStartElement(LarDigesterConstants.NODE_TYPE_LABEL));
	}

	protected void write(int action, String path, String type, String classPK)
		throws PortalException, XMLStreamException {

		if (_xmlEventWriter == null) {
			_log.warn(
				"It is not allowed to write into an existing digest file!");

			return;
		}

		try {
			_xmlEventWriter.add(
				getStartElement(LarDigesterConstants.NODE_DIGEST_ITEM_LABEL));

			addXmlNode(LarDigesterConstants.NODE_PATH_LABEL, path);

			addXmlNode(
				LarDigesterConstants.NODE_ACTION_LABEL,
				StringUtil.valueOf(action));

			addXmlNode(LarDigesterConstants.NODE_TYPE_LABEL, type);

			addXmlNode(LarDigesterConstants.NODE_CLASS_PK_LABEL, classPK);

			_xmlEventWriter.add(
				getEndElement(LarDigesterConstants.NODE_DIGEST_ITEM_LABEL));
		}
		finally {
			try {
				_xmlEventWriter.flush();
			}
			catch (Exception ex) {
				throw new PortalException(ex.getMessage());
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(LarDigest.class);

	private File _digestFile;

	private Document _document;
	private Map<String, EndElement> _endElements;
	private Map<String, StartElement> _startElements;
	private XMLEventFactory _xmlEventFactory;
	private XMLEventReader _xmlEventReader;
	private XMLEventWriter _xmlEventWriter;

}