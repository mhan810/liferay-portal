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

package com.liferay.digital.signature.adapter.docusign.internal;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.auth.OAuth;

import com.liferay.digital.signature.adapter.docusign.internal.config.DocuSignJWTSessionConfiguration;
import com.liferay.digital.signature.request.DSSessionId;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Edward C. Han
 */
@Component(
	configurationPid = "com.liferay.digital.signature.adapter.docusign.internal.config.DocuSignJWTSessionConfiguration",
	service = DocuSignSession.class
)
public class DocuSignJWTSession implements DocuSignSession {

	@Override
	public synchronized void connect() {
		if (!isExpired()) {
			return;
		}

		_apiClient = new ApiClient();

		//	There is a bug with how DocuSign handle the UserInfo URL, it adds
		//	an extra https:// just for the getUserInfo call. This removes the
		//	protocol from the url to allow us to complete the getUserInfo call.
		String baseURLWithProtocolRemoved = StringUtil.removeSubstring(
			_docuSignJwtSessionConfiguration.authenticationServerURL(),
			_HTTPS_PROTOCOL);

		_apiClient.setOAuthBasePath(baseURLWithProtocolRemoved);

		List<String> scopes = new ArrayList<>();

		scopes.add(OAuth.Scope_SIGNATURE);

		String privateKey = StringUtil.replace(
			_docuSignJwtSessionConfiguration.privateKey(), "\\n", "\n");

		try {
			OAuth.OAuthToken oAuthToken = _apiClient.requestJWTUserToken(
				_docuSignJwtSessionConfiguration.clientId(),
				_docuSignJwtSessionConfiguration.userId(), scopes,
				privateKey.getBytes(),
				_docuSignJwtSessionConfiguration.expiration());

			_accessToken = oAuthToken.getAccessToken();
			_expirationTime =
				System.currentTimeMillis() + (oAuthToken.getExpiresIn() * 1000);

			_apiClient.setAccessToken(_accessToken, oAuthToken.getExpiresIn());
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public ApiClient getApiClient() {
		return _apiClient;
	}

	@Override
	public DSSessionId getDSSessionId() {
		return new DSSessionId(
			_docuSignJwtSessionConfiguration.companyId(),
			_docuSignJwtSessionConfiguration.userId(),
			_docuSignJwtSessionConfiguration.accountId());
	}

	@Override
	public boolean isExpired() {
		if ((_accessToken == null) || (_expirationTime == null)) {
			return true;
		}

		if ((System.currentTimeMillis() + _DEFAULT_TOKEN_GRACE_PERIOD) >
				_expirationTime) {

			return true;
		}

		return false;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_docuSignJwtSessionConfiguration = ConfigurableUtil.createConfigurable(
			DocuSignJWTSessionConfiguration.class, properties);
	}

	protected String getAccessToken() {
		return _accessToken;
	}

	protected void setExpirationTime(long expirationTime) {
		_expirationTime = expirationTime;
	}

	private static final long _DEFAULT_TOKEN_GRACE_PERIOD = 10 * 60 * 1000;

	private static final String _HTTPS_PROTOCOL = "https://";

	private volatile String _accessToken;
	private ApiClient _apiClient;
	private volatile DocuSignJWTSessionConfiguration
		_docuSignJwtSessionConfiguration;
	private volatile Long _expirationTime = 0L;

}