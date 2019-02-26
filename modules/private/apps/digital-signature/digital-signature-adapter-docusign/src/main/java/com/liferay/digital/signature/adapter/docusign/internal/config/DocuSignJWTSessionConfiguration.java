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

package com.liferay.digital.signature.adapter.docusign.internal.config;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Michael C. Han
 */
@ExtendedObjectClassDefinition(
	category = "digital-signature", factoryInstanceLabelAttribute = "companyId",
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	factory = true,
	id = "com.liferay.digital.signature.adapter.docusign.internal.config.DocuSignJWTSessionConfiguration",
	localization = "content/Language",
	name = "docu-sign-jwt-session-configuration-name"
)
public interface DocuSignJWTSessionConfiguration {

	@Meta.AD(deflt = "0", name = "company-id", required = false)
	public long companyId();

	@Meta.AD(deflt = "", name = "user-id")
	public String userId();

	@Meta.AD(deflt = "", name = "account-id")
	public String accountId();

	@Meta.AD(
		deflt = "https://account-d.docusign.com",
		name = "authentication-server-url"
	)
	public String authenticationServerURL();

	@Meta.AD(deflt = "", name = "client-id")
	public String clientId();

	@Meta.AD(deflt = "3600", name = "expiration")
	public long expiration();

	@Meta.AD(deflt = "", name = "private-key")
	public String privateKey();

}