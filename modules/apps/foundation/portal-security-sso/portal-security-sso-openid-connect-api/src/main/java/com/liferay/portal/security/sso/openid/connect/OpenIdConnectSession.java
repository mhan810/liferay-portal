package com.liferay.portal.security.sso.openid.connect;

public interface OpenIdConnectSession {

	public String getAccessTokenString();

	public long getLoginTime();

	public long getLoginUserId();

	public String getNonceString();

	public OpenIdConnectFlowState getOpenIdConnectFlowState();

	public String getOpenIdProviderName();

	public String getRefreshTokenString();

	public String getStateString();

	public void setOpenIdConnectFlowState(
		OpenIdConnectFlowState openIdConnectFlowState);

}
