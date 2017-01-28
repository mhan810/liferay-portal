package com.liferay.journal.util;

import aQute.bnd.annotation.ProviderType;
import com.liferay.journal.model.JournalArticleDisplay;
import com.liferay.portal.kernel.portlet.PortletRequestModel;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.framework.FrameworkUtil;
import com.liferay.osgi.util.ServiceTrackerFactory;

/**
 * Provides the local utility for JournalContent. This utility wraps
 * {@link com.liferay.journal.util.impl.JournalContentImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Dave Nebinger
 * @see JournalContent
 * @see com.liferay.journal.util.impl.JournalContentImpl
 * @generated
 */
public class JournalContentUtil {

	public static void clearCache() {
		getJournalContent().clearCache();
	}

	public static void clearCache(
			long groupId, String articleId, String ddmTemplateKey) {
		getJournalContent().clearCache(groupId, articleId, ddmTemplateKey);
	}

	public static String getContent(
			long groupId, String articleId, String viewMode, String languageId,
			PortletRequestModel portletRequestModel) {
		return getJournalContent().getContent(groupId, articleId, viewMode,
				languageId, portletRequestModel);
	}

	public static String getContent(
			long groupId, String articleId, String ddmTemplateKey,
			String viewMode, String languageId,
			PortletRequestModel portletRequestModel) {
		return getJournalContent().getContent(
				groupId, articleId, ddmTemplateKey, viewMode, languageId,
				portletRequestModel);
	}

	public static String getContent(
			long groupId, String articleId, String ddmTemplateKey,
			String viewMode, String languageId,
			PortletRequestModel portletRequestModel,
			ThemeDisplay themeDisplay) {
		return getJournalContent().getContent(
				groupId, articleId, ddmTemplateKey, viewMode, languageId,
				portletRequestModel, themeDisplay);
	}

	public static String getContent(
			long groupId, String articleId, String ddmTemplateKey,
			String viewMode, String languageId, ThemeDisplay themeDisplay) {
		return getJournalContent().getContent(
				groupId, articleId, ddmTemplateKey, viewMode, languageId,
				themeDisplay);
	}

	public static String getContent(
			long groupId, String articleId, String viewMode, String languageId,
			ThemeDisplay themeDisplay) {
		return getJournalContent().getContent(
				groupId, articleId, viewMode, languageId, themeDisplay);
	}

	public static JournalArticleDisplay getDisplay(
			long groupId, String articleId, double version,
			String ddmTemplateKey, String viewMode, String languageId,
			int page, PortletRequestModel portletRequestModel,
			ThemeDisplay themeDisplay) {
		return getJournalContent().getDisplay(
				groupId, articleId, version, ddmTemplateKey, viewMode,
				languageId, page, portletRequestModel, themeDisplay);
	}

	public static JournalArticleDisplay getDisplay(
			long groupId, String articleId, String viewMode, String languageId,
			int page, ThemeDisplay themeDisplay) {
		return getJournalContent().getDisplay(
				groupId, articleId, viewMode, languageId, page, themeDisplay);
	}

	public static JournalArticleDisplay getDisplay(
			long groupId, String articleId, String viewMode, String languageId,
			PortletRequestModel portletRequestModel) {
		return getJournalContent().getDisplay(
				groupId, articleId, viewMode, languageId, portletRequestModel);
	}

	public static JournalArticleDisplay getDisplay(
			long groupId, String articleId, String ddmTemplateKey, String viewMode,
			String languageId, int page, PortletRequestModel portletRequestModel,
			ThemeDisplay themeDisplay) {
		return getJournalContent().getDisplay(
				groupId, articleId, ddmTemplateKey, viewMode, languageId, page,
				portletRequestModel, themeDisplay);
	}

	public static JournalArticleDisplay getDisplay(
			long groupId, String articleId, String ddmTemplateKey, String viewMode,
			String languageId, PortletRequestModel portletRequestModel) {
		return getJournalContent().getDisplay(
				groupId, articleId, ddmTemplateKey, viewMode, languageId,
				portletRequestModel);
	}

	public static JournalArticleDisplay getDisplay(
			long groupId, String articleId, String ddmTemplateKey,
			String viewMode, String languageId, ThemeDisplay themeDisplay) {
		return getJournalContent().getDisplay(
			groupId, articleId, ddmTemplateKey, viewMode, languageId,
			themeDisplay);
	}

	public static JournalArticleDisplay getDisplay(
			long groupId, String articleId, String viewMode, String languageId,
			ThemeDisplay themeDisplay) {
		return getJournalContent().getDisplay(
			groupId, articleId, viewMode, languageId, themeDisplay);
	}

	protected static JournalContent getJournalContent() {
		return _serviceTracker.getService();
	}

	private static final ServiceTracker<JournalContent, JournalContent>
			_serviceTracker = ServiceTrackerFactory.open(
			FrameworkUtil.getBundle(JournalContentUtil.class),
			JournalContent.class);

}
