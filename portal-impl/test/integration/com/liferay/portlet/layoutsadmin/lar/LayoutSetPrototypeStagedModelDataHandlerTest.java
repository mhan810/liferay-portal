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

package com.liferay.portlet.layoutsadmin.lar;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.lar.BaseStagedModelDataHandlerTestCase;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.LayoutTestUtil;
import com.liferay.portlet.layoutsetprototypes.lar.LayoutSetPrototypePortletDataHandler;

import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.testng.Assert;

/**
 * @author Daniela Zapata Riesco
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class LayoutSetPrototypeStagedModelDataHandlerTest
	extends BaseStagedModelDataHandlerTestCase {

	@Override
	protected StagedModel addStagedModel(
			Group group,
			Map<String, List<StagedModel>> dependentStagedModelsMap)
		throws Exception {

		boolean exportLayoutPrototypes = portletDataContext.getBooleanParameter(
			LayoutSetPrototypePortletDataHandler.NAMESPACE,
			LayoutSetPrototypePortletDataHandler.
				LAYOUT_PROTOTYPE_EXPORT_CONTROL);

		LayoutSetPrototype layoutSetPrototype =
			LayoutTestUtil.addLayoutSetPrototype(
				ServiceTestUtil.randomString());

		List<Layout> layoutSetPrototypeLayouts =
			LayoutLocalServiceUtil.getLayouts(
				layoutSetPrototype.getGroup().getGroupId(), true,
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

		Assert.assertEquals(1, layoutSetPrototypeLayouts.size());

		addDependentStagedModel(
			dependentStagedModelsMap, Layout.class,
			layoutSetPrototypeLayouts.get(0));

		if (exportLayoutPrototypes) {
			LayoutPrototype layoutPrototype = LayoutTestUtil.addLayoutPrototype(
			ServiceTestUtil.randomString());

			addDependentStagedModel(
				dependentStagedModelsMap, LayoutPrototype.class,
				layoutPrototype);

			List<Layout> layoutPrototypeLayouts =
				LayoutLocalServiceUtil.getLayouts(
					layoutPrototype.getGroupId(), true,
					LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

			Assert.assertEquals(1, layoutPrototypeLayouts.size());

			addDependentStagedModel(
				dependentStagedModelsMap, Layout.class,
				layoutPrototypeLayouts.get(0));

			Layout layoutPrototypeLayout = LayoutTestUtil.addLayout(
				layoutSetPrototype.getGroup().getGroupId(),
				ServiceTestUtil.randomString(), true, layoutPrototype, true);

			addDependentStagedModel(
				dependentStagedModelsMap, Layout.class, layoutPrototypeLayout);
		}

		return layoutSetPrototype;
	}

	@Override
	protected StagedModel getStagedModel(String uuid, Group group) {
		try {
			return LayoutSetPrototypeLocalServiceUtil.
				fetchLayoutSetPrototypeByUuidAndCompanyId(
					uuid, group.getCompanyId());
		}
		catch (Exception e) {
			return null;
		}
	}

	@Override
	protected Class<? extends StagedModel> getStagedModelClass() {
		return LayoutSetPrototype.class;
	}

	@Override
	protected void validateImport(
			StagedModel stagedModel,
			Map<String, List<StagedModel>> dependentStagedModelsMap,
			Group group)
		throws Exception {

		LayoutSetPrototype layoutSetPrototype =
			LayoutSetPrototypeLocalServiceUtil.
				fetchLayoutSetPrototypeByUuidAndCompanyId(
					stagedModel.getUuid(), group.getCompanyId());

		Group layoutSetPrototypeGroup = layoutSetPrototype.getGroup();

		List<StagedModel> dependentLayoutPrototypeStagedModels =
			dependentStagedModelsMap.get(LayoutPrototype.class.getSimpleName());

		if (dependentLayoutPrototypeStagedModels != null) {
			Assert.assertEquals(1, dependentLayoutPrototypeStagedModels.size());

			LayoutPrototype layoutPrototype =
				(LayoutPrototype)dependentLayoutPrototypeStagedModels.get(0);

			LayoutPrototype importedLayoutPrototype =
				LayoutPrototypeLocalServiceUtil.
					fetchLayoutPrototypeByUuidAndCompanyId(
						layoutPrototype.getUuid(), group.getCompanyId());

			Assert.assertNotNull(importedLayoutPrototype);

			List<StagedModel> dependentLayoutStagedModels =
				dependentStagedModelsMap.get(Layout.class.getSimpleName());

			Assert.assertEquals(3, dependentLayoutStagedModels.size());

			Layout layoutPrototypeLayout =
				(Layout)dependentLayoutStagedModels.get(2);

			Layout importedLayoutPrototypeLayout =
				LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
					layoutPrototypeLayout.getUuid(),
					layoutSetPrototypeGroup.getGroupId(), true);

			Assert.assertNotNull(importedLayoutPrototypeLayout);

			Assert.assertNotNull(
				layoutPrototypeLayout.getLayoutPrototypeUuid());

			Assert.assertEquals(
				importedLayoutPrototype.getUuid(),
				importedLayoutPrototypeLayout.getLayoutPrototypeUuid());
		}
	}

}