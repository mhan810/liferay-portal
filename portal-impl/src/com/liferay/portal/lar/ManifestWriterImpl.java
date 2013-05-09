/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.lar.ManifestEntry;
import com.liferay.portal.kernel.lar.ManifestWriter;
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.nio.channels.FileChannel;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * @author Daniel Kocsis
 */
public class ManifestWriterImpl implements ManifestWriter {

	public ManifestWriterImpl() throws PortletDataException {
		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

		_manifestFile = FileUtil.createTempFile();
		_manifestIndexFile = FileUtil.createTempFile();

		_xmlEventFactory = XMLEventFactory.newInstance();

		_newLine = _xmlEventFactory.createCharacters("\n");
		_tab = _xmlEventFactory.createCharacters("\t");

		try {
			_indexWriter = new FileWriter(_manifestIndexFile);

			FileOutputStream fos = new FileOutputStream(_manifestFile);

			_fileChannel = fos.getChannel();

			_xmlEventWriter = xmlOutputFactory.createXMLEventWriter(fos);

			StartDocument startDocument =
				_xmlEventFactory.createStartDocument();

			_xmlEventWriter.add(startDocument);
			_xmlEventWriter.add(_newLine);
			_xmlEventWriter.add(_newLine);

			writeOpenElement(ELEMENT_NAME_ROOT);

			_xmlEventWriter.add(_newLine);
		}
		catch (Exception e) {
			throw new PortletDataException(e);
		}
	}

	public void addManifestEntry(ManifestEntry manifestEntry) {
		try {
			long startPosition = getFilePosition();

			writeElementFormatted(manifestEntry.asXmlElement(), 2);

			index(manifestEntry, startPosition);
		}
		catch (XMLStreamException xse) {
			StringBundler sb = new StringBundler(4);

			sb.append("Unable to serialize manifest entry with class name ");
			sb.append(manifestEntry.getModelClassName());
			sb.append(" and uuid ");
			sb.append(manifestEntry.getUuid());

			_log.error(sb.toString());
		}
	}

	public void close() throws PortletDataException {
		try {
			writeCloseElement(ELEMENT_NAME_ROOT);

			_xmlEventWriter.flush();

			_xmlEventWriter.close();

			_indexWriter.flush();

			_indexWriter.close();
		} catch (Exception e) {
			_log.error("Unable to close data manifest!");

			throw new PortletDataException(e);
		}
	}

	public void closeSection() throws XMLStreamException {
		writeCloseElementFormatted(ELEMENT_NAME_SECTION, 1);
	}

	public InputStream getManifestAsStream() {
		try {
			return new FileInputStream(_manifestFile);
		}
		catch (FileNotFoundException fnfe) {
		}

		return null;
	}

	public InputStream getManifestIndexAsStream() {
		try {
			return new FileInputStream(_manifestIndexFile);
		}
		catch (FileNotFoundException fnfe) {
		}

		return null;
	}

	public void openSection(String portletId) throws XMLStreamException {
		Map<String, String> attributesMap = new HashMap<String, String>();

		attributesMap.put("portlet-id", portletId);

		writeOpenElementFormatted(ELEMENT_NAME_SECTION, attributesMap, 1);
	}

	protected long getFilePosition() {
		try {
			_xmlEventWriter.flush();

			return _fileChannel.position();
		}
		catch (Exception e) {
		}

		return 0;
	}

	protected void index(ManifestEntry manifestEntry, long startPosition) {
		long length = getFilePosition() - startPosition;

		StringBundler sb = new StringBundler(9);

		sb.append(startPosition);
		sb.append(StringPool.SPACE);
		sb.append(length);
		sb.append(StringPool.SPACE);
		sb.append(manifestEntry.getGroupId());
		sb.append(StringPool.POUND);
		sb.append(manifestEntry.getUuid());
		sb.append(StringPool.NEW_LINE);

		String indexEntry = sb.toString();

		try {
			_indexWriter.write(indexEntry);
		} catch (IOException e) {
			return;
		}
	}

	protected void writeCloseElement(String name) throws XMLStreamException {
		EndElement endElement = _xmlEventFactory.createEndElement("", "", name);

		_xmlEventWriter.add(endElement);
	}

	protected void writeCloseElementFormatted(String name, int indentation)
		throws XMLStreamException {

		for (int i = 1; i <= indentation; i++) {
			_xmlEventWriter.add(_tab);
		}

		writeCloseElement(name);

		_xmlEventWriter.add(_newLine);
	}

	protected void writeElementFormatted(Element element, int indentation)
		throws XMLStreamException {

		Map<String, String> attributesMap = new HashMap<String, String>();

		for (com.liferay.portal.kernel.xml.Attribute attribute :
				element.attributes()) {

			attributesMap.put(attribute.getName(), attribute.getValue());
		}

		writeOpenElementFormatted(
			element.getName(), attributesMap, indentation);

		for (Element childElement : element.elements()) {
			writeElementFormatted(childElement, ++indentation);
		}

		writeCloseElementFormatted(element.getName(), indentation);
	}

	protected void writeOpenElement(String name) throws XMLStreamException {
		writeOpenElement(name, null);
	}

	protected void writeOpenElement(String name, Map<String, String> attributes)
		throws XMLStreamException {

		StartElement startElement = _xmlEventFactory.createStartElement(
			"", "", name);

		_xmlEventWriter.add(startElement);

		if (attributes == null) {
			return;
		}

		for (Map.Entry<String, String> attributeEntry : attributes.entrySet()) {
			Attribute attributeEvent = _xmlEventFactory.createAttribute(
				attributeEntry.getKey(), attributeEntry.getValue());

			_xmlEventWriter.add(attributeEvent);
		}
	}

	protected void writeOpenElementFormatted(String name, int indentation)
		throws XMLStreamException {

		writeOpenElementFormatted(name, null, indentation);
	}

	protected void writeOpenElementFormatted(
			String name, Map<String, String> attributes, int indentation)
		throws XMLStreamException {

		for (int i = 1; i <= indentation; i++) {
			_xmlEventWriter.add(_tab);
		}

		writeOpenElement(name, attributes);

		_xmlEventWriter.add(_newLine);
	}

	private static Log _log = LogFactoryUtil.getLog(ManifestWriterImpl.class);

	private FileChannel _fileChannel;

	private FileWriter _indexWriter;
	private File _manifestFile;
	private File _manifestIndexFile;

	private XMLEvent _newLine;
	private XMLEvent _tab;

	private XMLEventFactory _xmlEventFactory;
	private XMLEventWriter _xmlEventWriter;

}