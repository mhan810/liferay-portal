/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.usersadmin.lar;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.EmailAddress;
import com.liferay.portal.model.OrgLabor;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.OrganizationConstants;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.Website;
import com.liferay.portal.service.AddressLocalServiceUtil;
import com.liferay.portal.service.EmailAddressLocalServiceUtil;
import com.liferay.portal.service.OrgLaborLocalServiceUtil;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.PhoneLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.WebsiteLocalServiceUtil;
import com.liferay.portlet.usersadmin.util.UsersAdminUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author David Mendez Gonzalez
 */
public class OrganizationStagedModelDataHandler
	extends BaseStagedModelDataHandler<Organization> {

	public static final String[] CLASS_NAMES = {Organization.class.getName()};

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Organization organization)
		throws Exception {

		Queue<Organization> organizations = new LinkedList<Organization>();

		organizations.add(organization);

		while (!organizations.isEmpty()) {
			Organization exportedOrganization = organizations.remove();

			Element organizationElement =
				portletDataContext.getExportDataElement(exportedOrganization);

			exportOrgLabors(portletDataContext, exportedOrganization);

			Element dependentModelsElement = organizationElement.addElement(
				"dependent-models");

			exportAddresses(
				organizationElement, portletDataContext, exportedOrganization);
			exportEmailAddresses(
				organizationElement, portletDataContext, exportedOrganization);
			exportPhones(
				organizationElement, portletDataContext, exportedOrganization);
			exportWebsites(
				organizationElement, portletDataContext, exportedOrganization);

			portletDataContext.addClassedModel(
				organizationElement,
				ExportImportPathUtil.getModelPath(exportedOrganization),
				exportedOrganization, UsersAdminPortletDataHandler.NAMESPACE);

			organizations.addAll(exportedOrganization.getSuborganizations());
		}
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Organization organization)
		throws Exception {

		long userId = portletDataContext.getUserId(organization.getUserUuid());

		Map<Long, Long> organizationIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				Organization.class);

		long parentOrganizationId = MapUtil.getLong(
			organizationIds, organization.getParentOrganizationId(),
			organization.getParentOrganizationId());

		if ((parentOrganizationId !=
				OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID) &&
			(parentOrganizationId == organization.getParentOrganizationId())) {

			String parentOrganizationPath = ExportImportPathUtil.getModelPath(
				portletDataContext, Organization.class.getName(),
				parentOrganizationId);

			Organization parentOrganization =
				(Organization)portletDataContext.getZipEntryAsObject(
					parentOrganizationPath);

			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, parentOrganization);

			parentOrganizationId = MapUtil.getLong(
				organizationIds, organization.getParentOrganizationId(),
				organization.getParentOrganizationId());
		}

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			organization, UsersAdminPortletDataHandler.NAMESPACE);

		serviceContext.setUserId(userId);

		Organization existingOrganization =
			OrganizationLocalServiceUtil.fetchOrganizationByUuidAndCompanyId(
				organization.getUuid(), portletDataContext.getCompanyId());

		if (existingOrganization == null) {
			existingOrganization =
				OrganizationLocalServiceUtil.fetchOrganization(
					portletDataContext.getCompanyId(), organization.getName());
		}

		List<OrgLabor> orgLabors = readOrgLabors(
			portletDataContext, organization);

		Organization importedOrganization = null;

		if (existingOrganization == null) {
			serviceContext.setUuid(organization.getUuid());

			importedOrganization =
				OrganizationLocalServiceUtil.addOrganization(
					userId, parentOrganizationId, organization.getName(),
					organization.getType(), organization.getRegionId(),
					organization.getCountryId(), organization.getStatusId(),
					organization.getComments(), false, serviceContext);
		}
		else {
			importedOrganization =
				OrganizationLocalServiceUtil.updateOrganization(
					portletDataContext.getCompanyId(),
					existingOrganization.getOrganizationId(),
					parentOrganizationId, organization.getName(),
					organization.getType(), organization.getRegionId(),
					organization.getCountryId(), organization.getStatusId(),
					organization.getComments(), false, serviceContext);
		}

		long importedOrganizationId = importedOrganization.getOrganizationId();

		UsersAdminUtil.updateOrgLabors(importedOrganizationId, orgLabors);

		importAddresses(portletDataContext, importedOrganization, organization);
		importEmailAddresses(
			portletDataContext, importedOrganization, organization);
		importPhones(portletDataContext, importedOrganization, organization);
		importWebsites(portletDataContext, importedOrganization, organization);

		portletDataContext.importClassedModel(
			organization, importedOrganization,
			UsersAdminPortletDataHandler.NAMESPACE);
	}

	protected void exportAddresses(
			Element element, PortletDataContext portletDataContext,
			Organization organization)
		throws PortalException, SystemException {

		List<Address> addresses = AddressLocalServiceUtil.getAddresses(
			organization.getCompanyId(), organization.getModelClassName(),
			organization.getOrganizationId());

		for (Address address : addresses) {

			portletDataContext.addReferenceElement(
				organization, element, address, false);

			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, address);
		}
	}

	protected void exportEmailAddresses(
			Element element, PortletDataContext portletDataContext,
			Organization organization)
		throws PortalException, SystemException {

		List<EmailAddress> emailAddresses =
			EmailAddressLocalServiceUtil.getEmailAddresses(
				organization.getCompanyId(), organization.getModelClassName(),
				organization.getOrganizationId());

		for (EmailAddress emailAddress : emailAddresses) {

			portletDataContext.addReferenceElement(
				organization, element, emailAddress, false);

			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, emailAddress);
		}
	}

	protected void exportOrgLabors(
			PortletDataContext portletDataContext, Organization organization)
		throws PortalException, SystemException {

		List<OrgLabor> orgLabors = OrgLaborLocalServiceUtil.getOrgLabors(
			organization.getOrganizationId());

		String orgLaborsXML = portletDataContext.toXML(orgLabors);

		String path = ExportImportPathUtil.getModelPath(
			organization, OrgLabor.class.getSimpleName());

		portletDataContext.addZipEntry(path, orgLaborsXML);
	}

	protected void exportPhones(
			Element element, PortletDataContext portletDataContext,
			Organization organization)
		throws PortalException, SystemException {

		List<Phone> phones = PhoneLocalServiceUtil.getPhones(
			organization.getCompanyId(), organization.getModelClassName(),
			organization.getOrganizationId());

		for (Phone phone : phones) {

			portletDataContext.addReferenceElement(
				organization, element, phone, false);

			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, phone);
		}
	}

	protected void exportWebsites(
			Element element, PortletDataContext portletDataContext,
			Organization organization)
		throws PortalException, SystemException {

		List<Website> websites = WebsiteLocalServiceUtil.getWebsites(
			organization.getCompanyId(), organization.getModelClassName(),
			organization.getOrganizationId());

		for (Website website : websites) {

			portletDataContext.addReferenceElement(
				organization, element, website, false);

			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, website);
		}
	}

	protected void importAddresses(
			PortletDataContext portletDataContext,
			Organization importedOrganization, Organization organization)
		throws PortalException {

		List<Element> addressesElement =
			portletDataContext.getReferenceDataElements(
				organization, Address.class);

		for (Element addressElement : addressesElement) {

			String path = addressElement.attributeValue("path");

			Address importedAddress =
				(Address)portletDataContext.getZipEntryAsObject(path);

			importedAddress.setClassPK(
				importedOrganization.getOrganizationId());

			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, importedAddress);
		}

	}

	protected void importEmailAddresses(
			PortletDataContext portletDataContext,
			Organization importedOrganization, Organization organization)
		throws PortalException {

		List<Element> emailAddressesElement =
			portletDataContext.getReferenceDataElements(
				organization, EmailAddress.class);

		for (Element emailAddressElement : emailAddressesElement) {

			String path = emailAddressElement.attributeValue("path");

			EmailAddress importedEmailAddress =
				(EmailAddress)portletDataContext.getZipEntryAsObject(path);

			importedEmailAddress.setClassPK(
				importedOrganization.getOrganizationId());

			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, importedEmailAddress);
		}

	}

	protected void importPhones(
			PortletDataContext portletDataContext,
			Organization importedOrganization, Organization organization)
		throws PortalException {

		List<Element> phonesElement =
			portletDataContext.getReferenceDataElements(
				organization, Phone.class);

		for (Element phoneElement : phonesElement) {

			String path = phoneElement.attributeValue("path");

			Phone importedPhone = (Phone)portletDataContext.getZipEntryAsObject(
				path);

			importedPhone.setClassPK(importedOrganization.getOrganizationId());

			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, importedPhone);
		}
	}

	protected void importWebsites(
			PortletDataContext portletDataContext,
			Organization importedOrganization, Organization organization)
		throws PortalException {

		List<Element> websitesElement =
			portletDataContext.getReferenceDataElements(
				organization, Website.class);

		for (Element websiteElement : websitesElement) {

			String path = websiteElement.attributeValue("path");

			Website importedWebsite =
				(Website)portletDataContext.getZipEntryAsObject(path);

			importedWebsite.setClassPK(
				importedOrganization.getOrganizationId());

			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, importedWebsite);
		}
	}

	protected List<OrgLabor> readOrgLabors(
		PortletDataContext portletDataContext, Organization organization) {

		String path = ExportImportPathUtil.getModelPath(
			organization, OrgLabor.class.getSimpleName());

		List<OrgLabor> rawEntries =
			(List<OrgLabor>)portletDataContext.getZipEntryAsObject(path);

		for (OrgLabor orgLabor : rawEntries) {
			orgLabor.setOrgLaborId(0);
		}

		return rawEntries;
	}

}