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

package com.liferay.portal.search.web.internal.portlet.results;

import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.security.permission.ResourceActions;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.search.web.internal.portlet.shared.search.NullPortletURL;
import com.liferay.portal.search.web.internal.portlet.shared.task.PortletSharedRequestHelper;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchRequest;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchResponse;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.css-class-wrapper=" +
			SearchResultsConstants.CSS_CLASS_WRAPPER,
		"com.liferay.portlet.display-category=category.search",
		"com.liferay.portlet.icon=/icons/search.png",
		"com.liferay.portlet.instanceable=true",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.restore-current-view=false",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=" + SearchResultsConstants.DISPLAY_NAME,
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=" +
			SearchResultsConstants.VIEW_TEMPLATE,
		"javax.portlet.name=" + SearchResultsPortletKeys.PORTLET_NAME,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=guest,power-user,user",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = {Portlet.class, PortletSharedSearchContributor.class}
)
public class SearchResultsPortlet
	extends MVCPortlet implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		SearchResultsPortletPreferences searchResultsPortletPreferences =
			new SearchResultsPortletPreferencesImpl(
				portletSharedSearchSettings.getPortletPreferences());

		paginate(searchResultsPortletPreferences, portletSharedSearchSettings);

		highlight(searchResultsPortletPreferences, portletSharedSearchSettings);
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		PortletSharedSearchResponse portletSharedSearchResponse =
			portletSharedSearchRequest.search(renderRequest);

		SearchResultsDisplayContext searchResultsDisplayContext =
			buildDisplayContext(
				portletSharedSearchResponse, renderRequest, renderResponse);

		renderRequest.setAttribute(
			SearchResultsWebKeys.DISPLAY_CONTEXT, searchResultsDisplayContext);

		super.render(renderRequest, renderResponse);
	}

	protected SearchResultsDisplayContext buildDisplayContext(
			PortletSharedSearchResponse portletSharedSearchResponse,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		SearchResultsDisplayContext searchResultsDisplayContext =
			new SearchResultsDisplayContext();

		List<Document> documents = portletSharedSearchResponse.getDocuments();

		searchResultsDisplayContext.setDocuments(documents);

		SearchResultsPortletPreferences searchResultsPortletPreferences =
			new SearchResultsPortletPreferencesImpl(
				portletSharedSearchResponse.getPortletPreferences(
					renderRequest));

		Optional<String> keywordsOptional =
			portletSharedSearchResponse.getKeywords();

		searchResultsDisplayContext.setKeywords(
			keywordsOptional.orElse(StringPool.BLANK));

		searchResultsDisplayContext.setRenderNothing(
			!keywordsOptional.isPresent());

		searchResultsDisplayContext.setSearchResultsSummariesHolder(
			buildSummaries(
				portletSharedSearchResponse, renderRequest, renderResponse));

		int totalHits = portletSharedSearchResponse.getTotalHits();

		searchResultsDisplayContext.setTotalHits(totalHits);

		int paginationStart = portletSharedSearchResponse.getPaginationStart();

		String paginationStartParameterName =
			searchResultsPortletPreferences.getPaginationStartParameterName();

		int paginationDelta = portletSharedSearchResponse.getPaginationDelta();

		String paginationDeltaParameterName =
			searchResultsPortletPreferences.getPaginationDeltaParameterName();

		searchResultsDisplayContext.setSearchContainer(
			buildSearchContainer(
				documents, totalHits, paginationStart,
				paginationStartParameterName, paginationDelta,
				paginationDeltaParameterName, renderRequest));

		return searchResultsDisplayContext;
	}

	protected SearchContainer<Document> buildSearchContainer(
			List<Document> documents, int totalHits, int paginationStart,
			String paginationStartParameterName, int paginationDelta,
			String paginationDeltaParameterName, RenderRequest renderRequest)
		throws PortletException {

		PortletRequest portletRequest = renderRequest;
		DisplayTerms displayTerms = null;
		DisplayTerms searchTerms = null;
		String curParam = paginationStartParameterName;
		int cur = paginationStart;
		int delta = paginationDelta;
		PortletURL portletURL = getPortletURL(
			renderRequest, paginationStartParameterName,
			paginationDeltaParameterName);
		List<String> headerNames = null;
		String emptyResultsMessage = null;
		String cssClass = null;

		SearchContainer<Document> searchContainer = new SearchContainer<>(
			portletRequest, displayTerms, searchTerms, curParam, cur, delta,
			portletURL, headerNames, emptyResultsMessage, cssClass);

		searchContainer.setDeltaParam(paginationDeltaParameterName);

		searchContainer.setResults(documents);
		searchContainer.setTotal(totalHits);

		return searchContainer;
	}

	protected SearchResultsSummariesHolder buildSummaries(
			PortletSharedSearchResponse portletSharedSearchResponse,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		SearchResultsSummariesBuilder searchResultsSummariesBuilder =
			new SearchResultsSummariesBuilder(
				portletSharedSearchResponse, assetEntryLocalService,
				blogsEntryLocalService, resourceActions, language,
				renderRequest, renderResponse);

		try {
			return searchResultsSummariesBuilder.build();
		}
		catch (PortletException pe) {
			throw pe;
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	protected PortletURL getPortletURL(
		RenderRequest renderRequest, String paginationStartParameterName,
		String paginationDeltaParameterName) {

		final String urlString = getURLString(
			renderRequest, paginationStartParameterName,
			paginationDeltaParameterName);

		return new NullPortletURL() {

			@Override
			public String toString() {
				return urlString;
			}

		};
	}

	protected String getURLString(
		RenderRequest renderRequest, String paginationStartParameterName,
		String paginationDeltaParameterName) {

		String urlString = portletSharedRequestHelper.getCompleteURL(
			renderRequest);

		urlString = HttpUtil.removeParameter(
			urlString, paginationDeltaParameterName);
		urlString = HttpUtil.removeParameter(
			urlString, paginationStartParameterName);

		return urlString;
	}

	protected void highlight(
		SearchResultsPortletPreferences searchResultsPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		boolean highlightEnabled =
			searchResultsPortletPreferences.isHighlightEnabled();

		QueryConfig queryConfig = portletSharedSearchSettings.getQueryConfig();

		queryConfig.setHighlightEnabled(highlightEnabled);
	}

	protected void paginate(
		SearchResultsPortletPreferences searchResultsPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		String paginationStartParameterName =
			searchResultsPortletPreferences.getPaginationStartParameterName();

		portletSharedSearchSettings.setPaginationStartParameterName(
			paginationStartParameterName);

		Optional<String> paginationStartParameterValueOptional =
			portletSharedSearchSettings.getParameter(
				paginationStartParameterName);

		Optional<Integer> paginationStartOptional =
			paginationStartParameterValueOptional.map(Integer::valueOf);

		paginationStartOptional.ifPresent(
			portletSharedSearchSettings::setPaginationStart);

		String paginationDeltaParameterName =
			searchResultsPortletPreferences.getPaginationDeltaParameterName();

		Optional<String> paginationDeltaParameterValueOptional =
			portletSharedSearchSettings.getParameter(
				paginationDeltaParameterName);

		Optional<Integer> paginationDeltaOptional =
			paginationDeltaParameterValueOptional.map(Integer::valueOf);

		int paginationDelta = paginationDeltaOptional.orElse(
			searchResultsPortletPreferences.getPaginationDelta());

		portletSharedSearchSettings.setPaginationDelta(paginationDelta);
	}

	@Reference
	protected AssetEntryLocalService assetEntryLocalService;

	@Reference
	protected BlogsEntryLocalService blogsEntryLocalService;

	@Reference
	protected Language language;

	@Reference
	protected PortletSharedRequestHelper portletSharedRequestHelper;

	@Reference
	protected PortletSharedSearchRequest portletSharedSearchRequest;

	@Reference
	protected ResourceActions resourceActions;

}