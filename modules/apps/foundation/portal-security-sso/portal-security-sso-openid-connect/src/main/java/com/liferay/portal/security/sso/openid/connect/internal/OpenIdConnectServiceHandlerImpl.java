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

package com.liferay.portal.security.sso.openid.connect.internal;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectProvider;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectProviderRegistry;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectServiceException;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectServiceHandler;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectUserInfoProcessor;
import com.liferay.portal.security.sso.openid.connect.constants.OpenIdConnectWebKeys;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.GeneralException;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.Tokens;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.SubjectType;
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.nimbusds.openid.connect.sdk.UserInfoSuccessResponse;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientMetadata;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import com.nimbusds.openid.connect.sdk.validators.IDTokenValidator;

import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Thuong Dinh
 */
@Component(immediate = true, service = OpenIdConnectServiceHandler.class)
public class OpenIdConnectServiceHandlerImpl
	implements OpenIdConnectServiceHandler {

	@Override
	public String processAuthenticationResponse(
			ThemeDisplay themeDisplay, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws PortalException {

		try {
			AuthenticationResponse authenticationResponse =
				AuthenticationResponseParser.parse(
					new URI(themeDisplay.getURLCurrent()));

			if (authenticationResponse instanceof AuthenticationErrorResponse) {
				AuthenticationErrorResponse authenticationerrorresponse =
					(AuthenticationErrorResponse)authenticationResponse;

				ErrorObject errorObject =
					authenticationerrorresponse.getErrorObject();

				throw new OpenIdConnectServiceException.
					AuthenticationErrorException(errorObject.toString());
			}

			HttpServletRequest httpServletRequest = getOriginalServletRequest(
				actionRequest);

			HttpSession httpSession = httpServletRequest.getSession();

			AuthenticationSuccessResponse authenticationSuccessResponse =
				(AuthenticationSuccessResponse)authenticationResponse;

			if (!verifyState(
					httpSession, authenticationSuccessResponse.getState())) {

				throw new OpenIdConnectServiceException.
					AuthenticationErrorException("Invalid state");
			}

			AuthorizationCode authorizationCode =
				authenticationSuccessResponse.getAuthorizationCode();

			String openIdConnectProviderName = (String)httpSession.getAttribute(
				OpenIdConnectWebKeys.OPEN_ID_CONNECT_PROVIDER_NAME);

			OpenIdConnectProvider openIdConnectProvider =
				_openIdConnectProviderRegistry.getOpenIdConnectProvider(
					openIdConnectProviderName);

			if (openIdConnectProvider == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to get OpenId Connect provider with name " +
							openIdConnectProviderName);
				}

				return null;
			}

			OIDCProviderMetadata oidcProviderMetadata =
				initOIDCProviderMetadata(openIdConnectProvider);

			OIDCTokenResponse oidcTokenResponse = requestToken(
				openIdConnectProviderName, httpServletRequest,
				authorizationCode, oidcProviderMetadata);

			validateToken(
				openIdConnectProviderName, actionRequest, oidcProviderMetadata,
				oidcTokenResponse);

			Tokens tokens = oidcTokenResponse.getTokens();

			UserInfo userInfo = requestUserInfo(
				tokens.getAccessToken(), oidcProviderMetadata);

			long userId = _openIdConnectUserInfoProcessor.processUserInfo(
				userInfo, themeDisplay.getCompanyId());

			httpSession.setAttribute(
				OpenIdConnectWebKeys.OPEN_ID_CONNECT_LOGIN, userId);

			return null;
		}
		catch (BadJOSEException | GeneralException | IOException |
			   JOSEException | URISyntaxException e) {

			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}

			throw new SystemException(e);
		}
	}

	@Override
	public void requestAuthentication(
			String openIdConnectProviderName, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws PortalException {

		OpenIdConnectProvider openIdConnectProvider =
			_openIdConnectProviderRegistry.getOpenIdConnectProvider(
				openIdConnectProviderName);

		if (openIdConnectProvider == null) {
			throw new SystemException(
				"Unable to get OpenId Connect provider with name " +
					openIdConnectProviderName);
		}

		HttpServletRequest httpServletRequest = getOriginalServletRequest(
			actionRequest);

		HttpSession httpSession = httpServletRequest.getSession();

		httpSession.setAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_PROVIDER_NAME,
			openIdConnectProviderName);

		Nonce nonce = new Nonce();

		httpSession.setAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_NONCE, nonce);

		State state = new State();

		httpSession.setAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_STATE, state);

		try {
			HttpServletResponse httpServletResponse =
				_portal.getHttpServletResponse(actionResponse);

			OIDCProviderMetadata oidcProviderMetadata =
				initOIDCProviderMetadata(openIdConnectProvider);

			OIDCClientInformation oidcClientInformation =
				getOIDCClientInformation(
					openIdConnectProviderName, oidcProviderMetadata);

			AuthenticationRequest authenticationRequest =
				new AuthenticationRequest(
					oidcProviderMetadata.getAuthorizationEndpointURI(),
					new ResponseType(ResponseType.Value.CODE),
					Scope.parse("openid email profile"),
					oidcClientInformation.getID(),
					createRedirectURI(httpServletRequest), state, nonce);

			URI authenticationRequestURI = authenticationRequest.toURI();

			httpServletResponse.sendRedirect(
				authenticationRequestURI.toString());
		}
		catch (IOException | ParseException | URISyntaxException e) {
			httpSession.removeAttribute(
				OpenIdConnectWebKeys.OPEN_ID_CONNECT_PROVIDER_NAME);
			httpSession.removeAttribute(
				OpenIdConnectWebKeys.OPEN_ID_CONNECT_STATE);
			httpSession.removeAttribute(
				OpenIdConnectWebKeys.OPEN_ID_CONNECT_NONCE);

			throw new SystemException(
				"Unable to communicate with OpenId Connect provider", e);
		}
	}

	protected URI createRedirectURI(HttpServletRequest httpServletRequest)
		throws PortalException, URISyntaxException {

		StringBundler sb = new StringBundler(5);

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		sb.append(
			_portal.getLayoutFriendlyURL(
				themeDisplay.getLayout(), themeDisplay));

		sb.append(StringPool.SLASH);
		sb.append(StringPool.DASH);
		sb.append(OpenIdConnectWebKeys.OPEN_ID_CONNECT_RESPONSE_ACTION_NAME);

		URI uri = new URI(sb.toString());

		return uri;
	}

	protected OIDCClientInformation getOIDCClientInformation(
			String openIdConnectProviderName,
			OIDCProviderMetadata oidcProviderMetadata)
		throws OpenIdConnectServiceException {

		OpenIdConnectProvider openIdConnectProvider =
			_openIdConnectProviderRegistry.getOpenIdConnectProvider(
				openIdConnectProviderName);

		if (Validator.isNull(openIdConnectProvider)) {
			_log.error(
				"Unable to get OpenId Connect provider with name " +
					openIdConnectProviderName);

			throw new OpenIdConnectServiceException.
				MissingClientInformationException(
					"Unable to get OpenId Connect provider with name " +
						openIdConnectProviderName);
		}

		ClientID clientID = new ClientID(openIdConnectProvider.getClientId());

		OIDCClientMetadata oidcClientMetadata = new OIDCClientMetadata();

		List<JWEAlgorithm> jweAlgorithms =
			oidcProviderMetadata.getIDTokenJWEAlgs();

		if (ListUtil.isNotEmpty(jweAlgorithms)) {
			oidcClientMetadata.setIDTokenJWEAlg(jweAlgorithms.get(0));
		}

		List<JWSAlgorithm> jwsAlgorithms =
			oidcProviderMetadata.getIDTokenJWSAlgs();

		if (ListUtil.isNotEmpty(jwsAlgorithms)) {
			oidcClientMetadata.setIDTokenJWSAlg(jwsAlgorithms.get(0));
		}

		oidcClientMetadata.setJWKSetURI(oidcProviderMetadata.getJWKSetURI());

		Secret secret = new Secret(openIdConnectProvider.getClientSecret());

		return new OIDCClientInformation(
			clientID, new Date(), oidcClientMetadata, secret);
	}

	protected HttpServletRequest getOriginalServletRequest(
		ActionRequest actionRequest) {

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			actionRequest);

		httpServletRequest = _portal.getOriginalServletRequest(
			httpServletRequest);

		return httpServletRequest;
	}

	protected OIDCProviderMetadata initOIDCProviderMetadata(
			OpenIdConnectProvider openIdConnectProvider)
		throws IOException, ParseException, PortalException,
			URISyntaxException {

		OIDCProviderMetadata oidcProviderMetadata = null;

		String discoveryEndpoint = openIdConnectProvider.getDiscoveryEndPoint();

		if (Validator.isNull(discoveryEndpoint)) {
			Issuer issuer = new Issuer(openIdConnectProvider.getIssuerURL());

			List<SubjectType> subjectTypes = new ArrayList<>();

			for (String subjectType : openIdConnectProvider.getSubjectTypes()) {
				subjectTypes.add(SubjectType.parse(subjectType));
			}

			oidcProviderMetadata = new OIDCProviderMetadata(
				issuer, subjectTypes,
				new URI(openIdConnectProvider.getJWKSURI()));

			oidcProviderMetadata.setAuthorizationEndpointURI(
				new URI(openIdConnectProvider.getAuthorizationEndPoint()));
			oidcProviderMetadata.setTokenEndpointURI(
				new URI(openIdConnectProvider.getTokenEndPoint()));
			oidcProviderMetadata.setUserInfoEndpointURI(
				new URI(openIdConnectProvider.getUserInfoEndPoint()));
		}
		else {
			String json = null;

			URI discoveryEndpointURI = new URI(
				openIdConnectProvider.getDiscoveryEndPoint());

			URL discoveryEndpointURL = discoveryEndpointURI.toURL();

			try (InputStream discoveryEndpointStream =
					discoveryEndpointURL.openStream();
				Scanner scanner = new Scanner(discoveryEndpointStream)) {

				Scanner delimiterScanner = scanner.useDelimiter("\\A");

				if (delimiterScanner.hasNext()) {
					json = scanner.next();
				}
				else {
					json = StringPool.BLANK;
				}
			}

			oidcProviderMetadata = OIDCProviderMetadata.parse(json);
		}

		return oidcProviderMetadata;
	}

	protected OIDCTokenResponse requestToken(
			String openIdConnectProviderName,
			HttpServletRequest httpServletRequest,
			AuthorizationCode authorizationCode,
			OIDCProviderMetadata oidcProviderMetadata)
		throws IOException, ParseException,
			PortalException, URISyntaxException {

		OIDCClientInformation oidcClientInformation = getOIDCClientInformation(
			openIdConnectProviderName, oidcProviderMetadata);

		URI redirectURI = createRedirectURI(httpServletRequest);

		TokenRequest tokenRequest = new TokenRequest(
			oidcProviderMetadata.getTokenEndpointURI(),
			new ClientSecretBasic(
				oidcClientInformation.getID(),
				oidcClientInformation.getSecret()),
			new AuthorizationCodeGrant(authorizationCode, redirectURI));

		HTTPRequest httpRequest = tokenRequest.toHTTPRequest();

		HTTPResponse httpResponse = httpRequest.send();

		TokenResponse tokenResponse = OIDCTokenResponseParser.parse(
			httpResponse);

		if (tokenResponse instanceof TokenErrorResponse) {
			TokenErrorResponse tokenErrorResponse =
				(TokenErrorResponse)tokenResponse;

			ErrorObject errorObject = tokenErrorResponse.getErrorObject();

			throw new OpenIdConnectServiceException.TokenErrorException(
				errorObject.toString());
		}

		return (OIDCTokenResponse)tokenResponse;
	}

	protected UserInfo requestUserInfo(
			AccessToken accessToken, OIDCProviderMetadata oidcProviderMetadata)
		throws IOException, ParseException, PortalException {

		UserInfoRequest userInfoRequest = new UserInfoRequest(
			oidcProviderMetadata.getUserInfoEndpointURI(),
			(BearerAccessToken)accessToken);

		HTTPRequest httpRequest = userInfoRequest.toHTTPRequest();

		HTTPResponse httpResponse = httpRequest.send();

		UserInfoResponse userInfoResponse = UserInfoResponse.parse(
			httpResponse);

		if (userInfoResponse instanceof UserInfoErrorResponse) {
			UserInfoErrorResponse userInfoErrorResponse =
				(UserInfoErrorResponse)userInfoResponse;

			ErrorObject errorObject = userInfoErrorResponse.getErrorObject();

			throw new OpenIdConnectServiceException.UserInfoErrorException(
				errorObject.toString());
		}

		UserInfoSuccessResponse userInfoSuccessResponse =
			(UserInfoSuccessResponse)userInfoResponse;

		return userInfoSuccessResponse.getUserInfo();
	}

	protected IDTokenClaimsSet validateToken(
			String openIdConnectProviderName, ActionRequest actionRequest,
			OIDCProviderMetadata oidcProviderMetadata,
			OIDCTokenResponse oidcTokenResponse)
		throws BadJOSEException, GeneralException, JOSEException,
			   MalformedURLException, OpenIdConnectServiceException {

		OIDCClientInformation oidcClientInformation = getOIDCClientInformation(
			openIdConnectProviderName, oidcProviderMetadata);

		IDTokenValidator idTokenValidator = IDTokenValidator.create(
			oidcProviderMetadata, oidcClientInformation, null);

		OIDCTokens oidcTokens = oidcTokenResponse.getOIDCTokens();

		HttpServletRequest httpServletRequest = getOriginalServletRequest(
			actionRequest);

		Nonce nonce = (Nonce)httpServletRequest.getAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_NONCE);

		return idTokenValidator.validate(oidcTokens.getIDToken(), nonce);
	}

	protected boolean verifyState(HttpSession httpSession, State state) {
		State httpSessionState = (State)httpSession.getAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_STATE);

		if (Validator.isNull(httpSessionState)) {
			return false;
		}

		return state.equals(httpSessionState);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OpenIdConnectServiceHandlerImpl.class);

	@Reference
	private OpenIdConnectProviderRegistry _openIdConnectProviderRegistry;

	@Reference
	private OpenIdConnectUserInfoProcessor _openIdConnectUserInfoProcessor;

	@Reference
	private Portal _portal;

}