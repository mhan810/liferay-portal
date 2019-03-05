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

import com.liferay.digital.signature.model.builder.AgentDSParticipantBuilder;
import com.liferay.digital.signature.model.builder.CarbonCopyDSParticipantBuilder;
import com.liferay.digital.signature.model.builder.CertifiedDeliveryDSParticipantBuilder;
import com.liferay.digital.signature.model.builder.DSDocumentBuilder;
import com.liferay.digital.signature.model.builder.DSEmailNotificationBuilder;
import com.liferay.digital.signature.model.builder.DSModelBuildersFactory;
import com.liferay.digital.signature.model.builder.DSSignaturePackageBuilder;
import com.liferay.digital.signature.model.builder.EditorDSParticipantBuilder;
import com.liferay.digital.signature.model.builder.InPersonSignerDSParticipantBuilder;
import com.liferay.digital.signature.model.builder.InPersonSignerNotaryDSParticipantBuilder;
import com.liferay.digital.signature.model.builder.IntermediaryDSParticipantBuilder;
import com.liferay.digital.signature.model.builder.SealDSParticipantBuilder;
import com.liferay.digital.signature.model.builder.SignerDSParticipantBuilder;
import com.liferay.portal.kernel.uuid.PortalUUID;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = DSModelBuildersFactory.class)
public class DSModelBuildersFactoryImpl implements DSModelBuildersFactory {

	@Override
	public AgentDSParticipantBuilder createDSAgentParticipantBuilder(
		String name, String email, int routingOrder) {

		return new AgentDSParticipantBuilderImpl(name, email, routingOrder);
	}

	@Override
	public CarbonCopyDSParticipantBuilder createDSCarbonCopyParticipantBuilder(
		String name, String email, int routingOrder) {

		return new CarbonCopyDSParticipantBuilderImpl(
			name, email, routingOrder);
	}

	@Override
	public CertifiedDeliveryDSParticipantBuilder
		createDSCertifiedDeliveryParticipantBuilder(
			String name, String email, int routingOrder) {

		return new CertifiedDeliveryDSParticipantBuilderImpl(
			name, email, routingOrder);
	}

	@Override
	public DSDocumentBuilder createDSDocumentBuilder(
		String documentId, String name) {

		return new DSDocumentBuilderImpl(documentId, name);
	}

	@Override
	public EditorDSParticipantBuilder createDSEditorParticipantBuilder(
		String name, String email, int routingOrder) {

		return new EditorDSParticipantBuilderImpl(name, email, routingOrder);
	}

	@Override
	public DSEmailNotificationBuilder createDSEmailNotificationBuilder(
		String subject, String message) {

		return new DSEmailNotificationBuilderImpl(subject, message);
	}

	@Override
	public InPersonSignerNotaryDSParticipantBuilder
		createDSInPersonSignerNotaryParticipantBuilder(
			String name, String email, int routingOrder,
			String notaryParticipantId, String notaryName, String notaryEmail) {

		return new InPersonSignerNotaryDSParticipantBuilderImpl(
			name, email, routingOrder, notaryParticipantId, notaryName,
			notaryEmail);
	}

	@Override
	public InPersonSignerDSParticipantBuilder
		createDSInPersonSignerParticipantBuilder(
			String hostName, String hostEmail, String signerName,
			String signerEmail, int routingOrder) {

		return new InPersonSignerDSParticipantBuilderImpl(
			hostName, hostEmail, signerName, signerEmail, routingOrder);
	}

	@Override
	public IntermediaryDSParticipantBuilder
		createDSIntermediaryParticipantBuilder(
			String name, String email, int routingOrder) {

		return new IntermediaryDSParticipantBuilderImpl(
			name, email, routingOrder);
	}

	@Override
	public SealDSParticipantBuilder createDSSealParticipantBuilder(
		String participantId, String name, String email, int routingOrder) {

		return new SealDSParticipantBuilderImpl(
			participantId, name, email, routingOrder);
	}

	@Override
	public DSSignaturePackageBuilder createDSSignatureRequestBuilder() {
		return new DSSignaturePackageBuilderImpl(_portalUUID);
	}

	@Override
	public SignerDSParticipantBuilder createDSSignerParticipantBuilder(
		String name, String email, int routingOrder) {

		return new SignerDSParticipantBuilderImpl(name, email, routingOrder);
	}

	protected void setPortalUUID(PortalUUID portalUUID) {
		_portalUUID = portalUUID;
	}

	@Reference
	private PortalUUID _portalUUID;

}