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

package com.liferay.portal.security.cas;

import com.liferay.portal.kernel.cas.CASManager;
import com.liferay.portal.kernel.cas.CASServer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.CompanyServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;

import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;

/**
 * @author Edward C. Han
 */
public class CASManagerImpl implements CASManager {

	public void deleteCASServer(long companyId, String casServerId)
		throws PortalException, SystemException {

		String[] casServerIds = getCASServerIds(companyId);

		UnicodeProperties newProperties = new UnicodeProperties();

		casServerIds = ArrayUtil.remove(casServerIds, casServerId);

		newProperties.put(
			PropsKeys.CAS_SERVER_IDS, StringUtil.merge(casServerIds));

		newProperties.put(
			PropsKeys.CAS_LAST_UPDATE,
			String.valueOf(System.currentTimeMillis()));

		String postfix = getPropertyPostfix(casServerId);

		String[] keys = new String[_KEYS.length];

		for (int i = 0; i < keys.length; i++) {
			keys[i] = _KEYS[i] + postfix;
		}

		CompanyServiceUtil.removePreferences(companyId, keys);
		CompanyServiceUtil.updatePreferences(companyId, newProperties);
	}

	public CASServer getCASServer(long companyId, String casServerId) {
		Map<String, CASServer> companyCASServers = getCompanyCASServers(
			companyId);

		if (companyCASServers != null) {
			return companyCASServers.get(casServerId);
		}
		else {
			return null;
		}
	}

	public String[] getCASServerIds(long companyId) {
		String[] casServerIds = _DEFAULT_SERVER_IDS;

		try {
			String ids = PrefsPropsUtil.getString(
				companyId, PropsKeys.CAS_SERVER_IDS);

			if (Validator.isNotNull(ids)) {
				casServerIds = StringUtil.split(ids);
			}
		}
		catch (SystemException e) {
			if (_log.isErrorEnabled()) {
				_log.error("Unable to load list of serverIds", e);
			}
		}

		return casServerIds;
	}

	public Map<String, CASServer> getCASServers(long companyId) {
		Map<String, CASServer> companyCASServers = getCompanyCASServers(
			companyId);

		if ((companyCASServers == null) || companyCASServers.isEmpty()) {
			return MapUtils.EMPTY_MAP;
		}
		else {
			return MapUtils.unmodifiableMap(companyCASServers);
		}
	}

	public CASServer getDefaultCASServer(long companyId) {
		if (isExpired(companyId)) {
			loadCompanyCASServers(companyId);
		}

		return _defaultCASServers.get(companyId);
	}

	public String getLoginUrl(
			HttpServletRequest request, HttpServletResponse response,
			CASServer casServer) {

		String serviceUrl = getServiceUrl(request, response, casServer);

		String loginUrl = casServer.getLoginUrl();

		loginUrl = HttpUtil.addParameter(loginUrl, "service", serviceUrl);

		return loginUrl;
	}

	public String getServiceUrl(
			HttpServletRequest request, HttpServletResponse response,
			CASServer casServer) {

		String serviceUrl = casServer.getServiceUrl();

		if (Validator.isNull(serviceUrl)) {
			serviceUrl = PortalUtil.getCurrentCompleteURL(request);

			serviceUrl = HttpUtil.removeParameter(serviceUrl, "ticket");
			serviceUrl = HttpUtil.removeParameter(serviceUrl, _CAS_SERVER_ID);
		}

		String casServerId = casServer.getServerId();

		serviceUrl = HttpUtil.addParameter(
			serviceUrl, _CAS_SERVER_ID, casServerId);

		return serviceUrl;
	}

	public void updateCASServer(
			long companyId, String casServerId, UnicodeProperties properties,
			boolean add)
		throws PortalException, SystemException {

		UnicodeProperties newProperties = new UnicodeProperties();

		String loginUrl = properties.get(PropsKeys.CAS_LOGIN_URL);
		String logoutUrl = properties.get(PropsKeys.CAS_LOGOUT_URL);
		String serverUrl = properties.get(PropsKeys.CAS_SERVER_URL);
		String serviceUrl = properties.get(PropsKeys.CAS_SERVICE_URL);

		List<String> errors = validate(
			loginUrl, logoutUrl, serverUrl, serviceUrl);

		if (Validator.isNull(casServerId)) {
			errors.add("ServerIdInvalid");
		}

		String[] casServerIds = getCASServerIds(companyId);

		if (!ArrayUtil.contains(casServerIds, casServerId)) {
			if (add) {
				casServerIds = ArrayUtil.append(casServerIds, casServerId);

				newProperties.put(
					PropsKeys.CAS_SERVER_IDS, StringUtil.merge(casServerIds));
			}
			else {
				errors.add("ServerIdDoesNotExist");
			}
		}
		else {
			if (add) {
				errors.add("ServerIdDuplicate");
			}
		}

		if (!errors.isEmpty()) {
			throw new InvalidCASSettingException(errors);
		}

		String propertyPostfix = getPropertyPostfix(casServerId);

		newProperties.put(
			PropsKeys.CAS_LAST_UPDATE,
			String.valueOf(System.currentTimeMillis()));

		newProperties.put(PropsKeys.CAS_LOGIN_URL + propertyPostfix, loginUrl);
		newProperties.put(
			PropsKeys.CAS_LOGOUT_URL + propertyPostfix, logoutUrl);
		newProperties.put(
			PropsKeys.CAS_SERVER_URL + propertyPostfix, serverUrl);
		newProperties.put(
			PropsKeys.CAS_SERVICE_URL + propertyPostfix, serviceUrl);

		CompanyServiceUtil.updatePreferences(companyId, newProperties);
	}

	protected Map<String, CASServer> getCompanyCASServers(long companyId) {
		Map<String, CASServer> companyCASServers = _casServers.get(companyId);

		if (isExpired(companyId) || (companyCASServers == null)) {
			companyCASServers = loadCompanyCASServers(companyId);
		}

		return companyCASServers;
	}

	protected String getPropertyPostfix(String casServerId) {
		if (Validator.isNull(casServerId)) {
			return StringPool.BLANK;
		}

		return StringPool.PERIOD + casServerId;
	}

	protected TicketValidator getTicketValidator(
			String serverUrl, String loginUrl) {

		Cas20ProxyTicketValidator cas20ProxyTicketValidator =
			new Cas20ProxyTicketValidator(serverUrl);

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("casServerUrlPrefix", serverUrl);
		parameters.put("casServerLoginUrl", loginUrl);
		parameters.put("redirectAfterValidation", "false");

		cas20ProxyTicketValidator.setCustomParameters(parameters);

		return cas20ProxyTicketValidator;
	}

	protected boolean isExpired(long companyId) {
		long timestamp = GetterUtil.get(_lastLoadTimestamp.get(companyId), -1L);

		long loadedTimestamp = 0L;

		try {
			loadedTimestamp = PrefsPropsUtil.getLong(
				companyId, PropsKeys.CAS_LAST_UPDATE, 0L);
		}
		catch (SystemException e) {
			if (_log.isErrorEnabled()) {
				_log.error("Unable to load last updated timestamp", e);
			}
		}

		return timestamp == -1 || timestamp != loadedTimestamp;
	}

	protected CASServer loadCASServer(long companyId, String casServerId) {
		CASServer casServer = null;

		try {
			String propertyPrefix = getPropertyPostfix(casServerId);

			String loginUrl = PrefsPropsUtil.getString(
				companyId, PropsKeys.CAS_LOGIN_URL + propertyPrefix);
			String logoutUrl = PrefsPropsUtil.getString(
				companyId, PropsKeys.CAS_LOGOUT_URL + propertyPrefix);
			String serverUrl = PrefsPropsUtil.getString(
				companyId, PropsKeys.CAS_SERVER_URL + propertyPrefix);
			String serviceUrl = PrefsPropsUtil.getString(
				companyId, PropsKeys.CAS_SERVICE_URL + propertyPrefix);

			List<String> errors = validate(
				loginUrl, logoutUrl, serverUrl, serviceUrl);

			if (errors.isEmpty()) {
				TicketValidator ticketValidator = getTicketValidator(
					serverUrl, loginUrl);

				casServer = new CASServerImpl(
					loginUrl, logoutUrl, casServerId, serverUrl, serviceUrl,
					ticketValidator);
			}
			else {
				if (_log.isWarnEnabled()) {
					_log.warn("The following settings are invalid: " +
						StringUtil.merge(errors));
				}
			}

			if (_log.isInfoEnabled()) {
				_log.info(
					"Loaded CAS server for " + companyId +
						" with serverUrl [" + serverUrl + "] loginUrl [" +
						loginUrl + "] serviceUrl [" + serviceUrl + "]");
			}
		}
		catch (SystemException e) {
			if (_log.isErrorEnabled()) {
				_log.error(
					"Unable to get settings for CAS server Id " + casServerId +
						" for companyId " + companyId, e);
			}
		}

		return casServer;
	}

	protected synchronized Map<String, CASServer> loadCompanyCASServers(
			long companyId) {

		String[] casServerIds = getCASServerIds(companyId);

		Map<String, CASServer> companyCASServers = _casServers.get(companyId);

		if (companyCASServers == null) {
			companyCASServers = new ConcurrentHashMap<String, CASServer>();

			_casServers.put(companyId, companyCASServers);
		}
		else {
			companyCASServers.clear();
		}

		for (String casServerId : casServerIds) {
			CASServer casServer = loadCASServer(companyId, casServerId);

			if (casServer != null) {
				companyCASServers.put(casServerId, casServer);
			}
			else {
				if (_log.isErrorEnabled()) {
					_log.error(
						"Unable to find configured CAS servers for " +
							"companyId " + companyId);
				}
			}
		}

		CASServer defaultCASServer = loadCASServer(companyId, StringPool.BLANK);

		_defaultCASServers.put(companyId, defaultCASServer);

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Number of CAS servers loaded for companyId " +
					companyId + " : " + companyCASServers.size());
		}

		long loadedTimestamp = 0L;

		try {
			loadedTimestamp = PrefsPropsUtil.getLong(
				companyId, PropsKeys.CAS_LAST_UPDATE, 0L);
		}
		catch (SystemException e) {
			if (_log.isErrorEnabled()) {
				_log.error("Unable to load last updated timestamp", e);
			}
		}

		_lastLoadTimestamp.put(companyId, loadedTimestamp);

		return companyCASServers;
	}

	protected void resetCASSettings(long companyId) {
		if (_log.isInfoEnabled()) {
			_log.info("Clearing loaded CAS servers for company " + companyId);
		}

		_lastLoadTimestamp.remove(companyId);
	}

	protected List<String> validate(
			String loginUrl, String logoutUrl, String serverUrl,
			String serviceUrl) {

		List<String> errors = new ArrayList<String>(9);

		if (!Validator.isUrl(loginUrl)) {
			errors.add("LoginURLInvalid");
		}

		if (!Validator.isUrl(logoutUrl)) {
			errors.add("LogoutURLInvalid");
		}

		if (!Validator.isUrl(serverUrl)) {

			errors.add("ServerURLInvalid");
		}

		if (Validator.isNotNull(serviceUrl) && !Validator.isUrl(serviceUrl)) {
			errors.add("ServiceURLInvalid");
		}

		return errors;
	}

	private static final String _CAS_SERVER_ID = "casServerId";

	private static final String[] _DEFAULT_SERVER_IDS = {};

	private static final String[] _KEYS = {
		PropsKeys.CAS_LOGIN_URL, PropsKeys.CAS_LOGOUT_URL,
		PropsKeys.CAS_SERVICE_URL, PropsKeys.CAS_SERVER_URL
	};

	private static Log _log = LogFactoryUtil.getLog(CASManagerImpl.class);

	private Map<Long, Map<String, CASServer>> _casServers =
		new ConcurrentHashMap<Long, Map<String, CASServer>>();
	private Map<Long, CASServer> _defaultCASServers =
		new ConcurrentHashMap<Long, CASServer>();
	private Map<Long, Long> _lastLoadTimestamp =
		new ConcurrentHashMap<Long, Long>();

}