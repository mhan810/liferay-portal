/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.machine.learning.forecast.model;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Riccardo Ferrari
 */
@ProviderType
public interface AssetCategoryCommerceMLForecast extends CommerceMLForecast {

	public long getAssetCategoryId();

	public long getCommerceAccountId();

	public void setAssetCategoryId(long assetCategoryId);

	public void setCommerceAccountId(long commerceAccountId);

}