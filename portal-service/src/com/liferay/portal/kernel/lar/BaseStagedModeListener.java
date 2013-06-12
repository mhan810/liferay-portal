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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.model.StagedModel;

/**
 * @author Zsolt Berentey
 */
public class BaseStagedModeListener<T extends StagedModel>
	implements ModelListener<T> {

	@Override
	@SuppressWarnings("unused")
	public void onAfterAddAssociation(
			Object classPK, String associationClassName,
			Object associationClassPK)
		throws ModelListenerException {
	}

	@Override
	@SuppressWarnings("unused")
	public void onAfterCreate(T stagedModel) throws ModelListenerException {
	}

	@Override
	@SuppressWarnings("unused")
	public void onAfterRemove(T stagedModel) throws ModelListenerException {
	}

	@Override
	@SuppressWarnings("unused")
	public void onAfterRemoveAssociation(
			Object classPK, String associationClassName,
			Object associationClassPK)
		throws ModelListenerException {
	}

	@Override
	@SuppressWarnings("unused")
	public void onAfterUpdate(T stagedModel) throws ModelListenerException {
	}

	@Override
	@SuppressWarnings("unused")
	public void onBeforeAddAssociation(
			Object classPK, String associationClassName,
			Object associationClassPK)
		throws ModelListenerException {
	}

	@Override
	@SuppressWarnings("unused")
	public void onBeforeCreate(T stagedModel) throws ModelListenerException {
	}

	@Override
	@SuppressWarnings("unused")
	public void onBeforeRemove(T stagedModel) throws ModelListenerException {
	}

	@Override
	@SuppressWarnings("unused")
	public void onBeforeRemoveAssociation(
			Object classPK, String associationClassName,
			Object associationClassPK)
		throws ModelListenerException {
	}

	@Override
	@SuppressWarnings("unused")
	public void onBeforeUpdate(T stagedModel) throws ModelListenerException {
	}

}