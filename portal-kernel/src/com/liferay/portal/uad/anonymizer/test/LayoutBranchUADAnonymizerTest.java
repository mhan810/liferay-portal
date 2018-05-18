/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.uad.anonymizer.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.LayoutBranch;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutBranchLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.uad.constants.PortalUADConstants;
import com.liferay.portal.uad.test.LayoutBranchUADEntityTestHelper;

import com.liferay.user.associated.data.aggregator.UADAggregator;
import com.liferay.user.associated.data.anonymizer.UADAnonymizer;
import com.liferay.user.associated.data.test.util.BaseUADAnonymizerTestCase;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;

import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@RunWith(Arquillian.class)
public class LayoutBranchUADAnonymizerTest extends BaseUADAnonymizerTestCase {
	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule = new LiferayIntegrationTestRule();

	@Override
	protected BaseModel<?> addBaseModel(long userId) throws Exception {
		return addBaseModel(userId, true);
	}

	@Override
	protected BaseModel<?> addBaseModel(long userId, boolean deleteAfterTestRun)
		throws Exception {
		LayoutBranch layoutBranch = _layoutBranchUADEntityTestHelper.addLayoutBranch(userId);

		if (deleteAfterTestRun) {
			_layoutBranchs.add(layoutBranch);
		}

		return layoutBranch;
	}

	@Override
	protected UADAggregator getUADAggregator() {
		return _uadAggregator;
	}

	@Override
	protected UADAnonymizer getUADAnonymizer() {
		return _uadAnonymizer;
	}

	@Override
	protected boolean isBaseModelAutoAnonymized(long baseModelPK, User user)
		throws Exception {
		LayoutBranch layoutBranch = _layoutBranchLocalService.getLayoutBranch(baseModelPK);

		String userName = layoutBranch.getUserName();

		if ((layoutBranch.getUserId() != user.getUserId()) &&
				!userName.equals(user.getFullName())) {
			return true;
		}

		return false;
	}

	@Override
	protected boolean isBaseModelDeleted(long baseModelPK) {
		if (_layoutBranchLocalService.fetchLayoutBranch(baseModelPK) == null) {
			return true;
		}

		return false;
	}

	@After
	public void tearDown() throws Exception {
		_layoutBranchUADEntityTestHelper.cleanUpDependencies(_layoutBranchs);
	}

	@DeleteAfterTestRun
	private final List<LayoutBranch> _layoutBranchs = new ArrayList<LayoutBranch>();
	@Inject
	private LayoutBranchLocalService _layoutBranchLocalService;
	@Inject
	private LayoutBranchUADEntityTestHelper _layoutBranchUADEntityTestHelper;
	@Inject(filter = "model.class.name=" +
	PortalUADConstants.CLASS_NAME_LAYOUT_BRANCH)
	private UADAggregator _uadAggregator;
	@Inject(filter = "model.class.name=" +
	PortalUADConstants.CLASS_NAME_LAYOUT_BRANCH)
	private UADAnonymizer _uadAnonymizer;
}