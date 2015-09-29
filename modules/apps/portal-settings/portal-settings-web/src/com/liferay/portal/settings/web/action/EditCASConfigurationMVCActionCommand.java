package com.liferay.portal.settings.web.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;
import com.liferay.portal.kernel.settings.ModifiableSettings;
import com.liferay.portal.kernel.settings.Settings;
import com.liferay.portal.kernel.settings.SettingsDescriptor;
import com.liferay.portal.kernel.settings.SettingsFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.sso.cas.constants.CASConstants;
import com.liferay.portal.settings.web.constants.PortalSettingsPortletKeys;
import com.liferay.portal.theme.ThemeDisplay;

@Component(
		property = {
			"javax.portlet.name=" + PortalSettingsPortletKeys.PORTAL_SETTINGS,
			"mvc.command.name=/portal_settings/edit_company_cas_configuration"
		},
		service = MVCActionCommand.class
	)
public class EditCASConfigurationMVCActionCommand extends BaseMVCActionCommand implements MVCActionCommand {

	@Override
	protected void doProcessAction(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {
		
		validateCAS(actionRequest);
		
		if (SessionErrors.isEmpty(actionRequest)) {
			updateCASSettings(actionRequest);
		}
	}
	
	private void updateCASSettings(ActionRequest actionRequest) throws Exception {
		
		Settings settings = getSettings(actionRequest);		
		ModifiableSettings modifiableSettings = settings.getModifiableSettings();
		
		SettingsDescriptor settingsDescriptor =
				SettingsFactoryUtil.getSettingsDescriptor(_settingsId);

		for (String name : settingsDescriptor.getAllKeys()) {
			
			String value = actionRequest.getParameter(name);
			String oldValue = settings.getValue(name, null);

			if (!StringUtil.equalsIgnoreBreakLine(value, oldValue)) {
				modifiableSettings.setValue(name, value);
			}
		}
		
		modifiableSettings.store();		
	}
	
	protected Settings getSettings(ActionRequest actionRequest) throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return SettingsFactoryUtil.getSettings(
			new CompanyServiceSettingsLocator(
				themeDisplay.getCompanyId(), _settingsId));
	}
	
	
	protected void validateCAS(ActionRequest actionRequest) {
		
		boolean casEnabled = ParamUtil.getBoolean(actionRequest, CASConstants.CAS_AUTH_ENABLED);

		if (!casEnabled) {
			return;
		}
		
		boolean importFromLDAP = ParamUtil.getBoolean(actionRequest, CASConstants.CAS_IMPORT_FROM_LDAP);		
		String casLoginURL = ParamUtil.getString(actionRequest, CASConstants.CAS_LOGIN_URL);
		String casLogoutURL = ParamUtil.getString(actionRequest, CASConstants.CAS_LOGOUT_URL);
		String casServerName = ParamUtil.getString(actionRequest, CASConstants.CAS_SERVER_NAME);
		String casServerURL = ParamUtil.getString(actionRequest, CASConstants.CAS_SERVER_URL);
		String casServiceURL = ParamUtil.getString(actionRequest, CASConstants.CAS_SERVICE_URL);
		String casNoSuchUserRedirectURL = ParamUtil.getString(actionRequest, CASConstants.CAS_NO_SUCH_USER_REDIRECT_URL);

		if (!Validator.isUrl(casLoginURL)) {
			SessionErrors.add(actionRequest, "casLoginURLInvalid");
		}

		if (!Validator.isUrl(casLogoutURL)) {
			SessionErrors.add(actionRequest, "casLogoutURLInvalid");
		}

		if (Validator.isNull(casServerName)) {
			SessionErrors.add(actionRequest, "casServerNameInvalid");
		}

		if (!Validator.isUrl(casServerURL)) {
			SessionErrors.add(actionRequest, "casServerURLInvalid");
		}

		if (Validator.isNotNull(casServiceURL) &&
			!Validator.isUrl(casServiceURL)) {

			SessionErrors.add(actionRequest, "casServiceURLInvalid");
		}

		if (Validator.isNotNull(casNoSuchUserRedirectURL) &&
			!Validator.isUrl(casNoSuchUserRedirectURL)) {

			SessionErrors.add(actionRequest, "casNoSuchUserURLInvalid");
		}
	}	
	
	
	public String getParameter(PortletRequest portletRequest, String name) {
		return ParamUtil.getString(portletRequest, name);
	}

	private static final String _settingsId = CASConstants.SERVICE_NAME;
}
