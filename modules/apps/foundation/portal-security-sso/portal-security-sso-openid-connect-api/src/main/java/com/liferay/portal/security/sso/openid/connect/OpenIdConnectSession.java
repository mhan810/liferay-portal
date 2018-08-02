package com.liferay.portal.security.sso.openid.connect;

public interface OpenIdConnectSession {

	public String getAccessToken();

	public long getLoginTime();

	public long getLoginUserId();

	public String getNonce();

	public OpenIdConnectFlowState getOpenIdConnectFlowState();

	public String getOpenIdProviderName()

	public String getRefreshToken();

	public String getState();

}
