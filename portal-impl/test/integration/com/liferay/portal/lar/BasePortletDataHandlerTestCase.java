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

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.ManifestSummary;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataContextFactoryUtil;
import com.liferay.portal.kernel.lar.PortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.ElementHandler;
import com.liferay.portal.kernel.xml.ElementProcessor;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portlet.PortletPreferencesImpl;

import java.io.StringReader;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.xerces.parsers.SAXParser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.powermock.api.mockito.PowerMockito;

import org.xml.sax.InputSource;

/**
 * @author Zsolt Berentey
 */
public abstract class BasePortletDataHandlerTestCase extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();

		stagingGroup = GroupTestUtil.addGroup();

		portletDataHandler = createPortletDataHandler();
		portletId = getPortletId();
	}

	@After
	public void tearDown() throws Exception {
		GroupLocalServiceUtil.deleteGroup(stagingGroup);
	}

	@Test
	@Transactional
	public void testDeletions() throws Exception {
		initExport();

		addStagedModels();

		portletDataContext.setEndDate(getEndDate());

		portletDataHandler.prepareManifestSummary(portletDataContext);

		ManifestSummary manifestSummary =
			portletDataContext.getManifestSummary();

		Map<String, Long> modelCounters = manifestSummary.getModelCounters();

		Map<String, Long> expectedModelCounters = new HashMap<String, Long>(
			modelCounters);

		modelCounters.clear();

		portletDataHandler.exportData(
			portletDataContext, portletId, new PortletPreferencesImpl());

		deleteStagedModels();

		portletDataContext.setEndDate(getEndDate());

		portletDataHandler.prepareManifestSummary(portletDataContext);

		checkCounters(
			expectedModelCounters, manifestSummary.getDeletionCounters());

		Map<String, Long> deletionCounters =
			manifestSummary.getDeletionCounters();

		deletionCounters.clear();

		PortletExporter portletExporter = new PortletExporter();

		portletExporter.exportDeletions(portletDataContext);

		checkDeletions();
	}

	@Test
	@Transactional
	public void testPrepareManifestSummary() throws Exception {
		initExport();

		addStagedModels();

		portletDataContext.setEndDate(getEndDate());

		portletDataHandler.prepareManifestSummary(portletDataContext);

		ManifestSummary manifestSummary =
			portletDataContext.getManifestSummary();

		Map<String, Long> modelCounters = manifestSummary.getModelCounters();

		Map<String, Long> preparedModelCounters = new HashMap<String, Long>(
			modelCounters);

		modelCounters.clear();

		portletDataHandler.exportData(
			portletDataContext, portletId, new PortletPreferencesImpl());

		Set<String> classNames = preparedModelCounters.keySet();

		for (Iterator<String> it = classNames.iterator(); it.hasNext(); ) {
			String className = it.next();

			if (preparedModelCounters.get(className) == 0) {
				it.remove();
			}
		}

		checkCounters(
			manifestSummary.getModelCounters(), preparedModelCounters);
	}

	protected void addBooleanParameter(
		Map<String, String[]> parameterMap, String namespace, String name,
		boolean value) {

		PortletDataHandlerBoolean portletDataHandlerBoolean =
			new PortletDataHandlerBoolean(namespace, name);

		parameterMap.put(
			portletDataHandlerBoolean.getNamespacedControlName(),
			new String[] {String.valueOf(value)});
	}

	protected void addParameters(Map<String, String[]> parameterMap) {
	}

	protected abstract void addStagedModels() throws Exception;

	protected void checkCounters(
		Map<String, Long> expectedCounters, Map<String, Long> actualCounters) {

		int expectedCountersSize = expectedCounters.size();

		for (String className : expectedCounters.keySet()) {
			Assert.assertEquals(
				className, expectedCounters.get(className),
				actualCounters.get(className));
		}

		Assert.assertEquals(expectedCountersSize, actualCounters.size());
	}

	protected void checkDeletions() throws Exception {

		final Map<String, Long> deletionsCounters = new HashMap<String, Long>();

		SAXParser saxParser = new SAXParser();

		ElementHandler elementHandler = new ElementHandler(
			new ElementProcessor() {

				@Override
				public void processElement(Element element) {
					String className = element.attributeValue("class-name");
					long count = GetterUtil.getLong(
						deletionsCounters.get(className));

					deletionsCounters.put(className, count + 1);
				}

			},
			new String[] {"deletion"});

		saxParser.setContentHandler(elementHandler);

		ZipReader zipReader = (ZipReader)portletDataContext.getZipWriter();

		String deletions = zipReader.getEntryAsString(
			ExportImportPathUtil.getRootPath(portletDataContext) +
				"/deletions.xml");

		Assert.assertNotNull(deletions);

		saxParser.parse(new InputSource(new StringReader(deletions)));

		ManifestSummary manifestSummary =
			portletDataContext.getManifestSummary();

		checkCounters(manifestSummary.getDeletionCounters(), deletionsCounters);
	}

	protected abstract PortletDataHandler createPortletDataHandler();

	protected void deleteStagedModels() throws Exception {};

	protected Date getEndDate() {
		return new Date();
	}

	protected abstract String getPortletId();

	protected Date getStartDate() {
		return new Date(System.currentTimeMillis() - Time.HOUR);
	}

	protected void initExport() throws Exception {
		Map<String, String[]> parameterMap =
			new LinkedHashMap<String, String[]>();

		addParameters(parameterMap);

		TestReaderWriter testReaderWriter = new TestReaderWriter();

		portletDataContext =
			PortletDataContextFactoryUtil.createExportPortletDataContext(
				stagingGroup.getCompanyId(), stagingGroup.getGroupId(),
				parameterMap, getStartDate(), getEndDate(), testReaderWriter);

		rootElement = SAXReaderUtil.createElement("root");

		portletDataContext.setExportDataRootElement(rootElement);
	}

	protected PortletDataContext portletDataContext;
	protected PortletDataHandler portletDataHandler;
	protected String portletId;
	protected Element rootElement;
	protected Group stagingGroup;

}