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

package com.liferay.digital.signature.internal.model.builder;

import com.liferay.digital.signature.model.builder.DSAgentParticipantBuilder;
import com.liferay.digital.signature.model.builder.DSCarbonCopyParticipantBuilder;
import com.liferay.digital.signature.model.builder.DSCertifiedDeliveryParticipantBuilder;
import com.liferay.digital.signature.model.builder.DSDocumentBuilder;
import com.liferay.digital.signature.model.builder.DSEditorParticipantBuilder;
import com.liferay.digital.signature.model.builder.DSEmailNotificationBuilder;
import com.liferay.digital.signature.model.builder.DSInPersonSignerNotaryParticipantBuilder;
import com.liferay.digital.signature.model.builder.DSInPersonSignerParticipantBuilder;
import com.liferay.digital.signature.model.builder.DSIntermediaryParticipantBuilder;
import com.liferay.digital.signature.model.builder.DSModelBuildersFactory;
import com.liferay.digital.signature.model.builder.DSSealParticipantBuilder;
import com.liferay.digital.signature.model.builder.DSSignaturePackageBuilder;
import com.liferay.digital.signature.model.builder.DSSignerParticipantBuilder;
import com.liferay.portal.kernel.uuid.PortalUUID;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = DSModelBuildersFactory.class)
public class DSModelBuildersFactoryImpl implements DSModelBuildersFactory {

	@Override
	public DSAgentParticipantBuilder createDSAgentParticipantBuilder(
		String name, String email, int routingOrder) {

		return new DSAgentParticipantBuilderImpl(name, email, routingOrder);
	}

	@Override
	public DSCarbonCopyParticipantBuilder createDSCarbonCopyParticipantBuilder(
		String name, String email, int routingOrder) {

		return new DSCarbonCopyParticipantBuilderImpl(
			name, email, routingOrder);
	}

	@Override
	public DSCertifiedDeliveryParticipantBuilder
		createDSCertifiedDeliveryParticipantBuilder(
			String name, String email, int routingOrder) {

		return new DSCertifiedDeliveryParticipantBuilderImpl(
			name, email, routingOrder);
	}

	@Override
	public DSDocumentBuilder createDSDocumentBuilder(
		String documentId, String name) {

		return new DSDocumentBuilderImpl(documentId, name);
	}

	@Override
	public DSEditorParticipantBuilder createDSEditorParticipantBuilder(
		String name, String email, int routingOrder) {

		return new DSEditorParticipantBuilderImpl(name, email, routingOrder);
	}

	@Override
	public DSEmailNotificationBuilder createDSEmailNotificationBuilder(
		String subject, String message) {

		return new DSEmailNotificationBuilderImpl(subject, message);
	}

	@Override
	public DSInPersonSignerNotaryParticipantBuilder
		createDSInPersonSignerNotaryParticipantBuilder(
			String name, String email, int routingOrder,
			String notaryParticipantId, String notaryName, String notaryEmail) {

		return new DSInPersonSignerNotaryParticipantBuilderImpl(
			name, email, routingOrder, notaryParticipantId, notaryName,
			notaryEmail);
	}

	@Override
	public DSInPersonSignerParticipantBuilder
		createDSInPersonSignerParticipantBuilder(
			String hostName, String hostEmail, String signerName,
			String signerEmail, int routingOrder) {

		return new DSInPersonSignerParticipantBuilderImpl(
			hostName, hostEmail, signerName, signerEmail, routingOrder);
	}

	@Override
	public DSIntermediaryParticipantBuilder
		createDSIntermediaryParticipantBuilder(
			String name, String email, int routingOrder) {

		return new DSIntermediaryParticipantBuilderImpl(
			name, email, routingOrder);
	}

	@Override
	public DSSealParticipantBuilder createDSSealParticipantBuilder(
		String participantId, String name, String email, int routingOrder) {

		return new DSSealParticipantBuilderImpl(
			participantId, name, email, routingOrder);
	}

	@Override
	public DSSignaturePackageBuilder createDSSignatureRequestBuilder() {
		return new DSSignaturePackageBuilderImpl(_portalUUID);
	}

	@Override
	public DSSignerParticipantBuilder createDSSignerParticipantBuilder(
		String name, String email, int routingOrder) {

		return new DSSignerParticipantBuilderImpl(name, email, routingOrder);
	}

	protected void setPortalUUID(PortalUUID portalUUID) {
		_portalUUID = portalUUID;
	}

	@Reference
	private PortalUUID _portalUUID;

}