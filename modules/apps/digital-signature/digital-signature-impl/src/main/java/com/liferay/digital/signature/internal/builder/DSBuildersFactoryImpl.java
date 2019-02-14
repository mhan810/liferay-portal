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

package com.liferay.digital.signature.internal.builder;

import com.liferay.digital.signature.builder.DSAgentParticipantBuilder;
import com.liferay.digital.signature.builder.DSBuildersFactory;
import com.liferay.digital.signature.builder.DSCarbonCopyParticipantBuilder;
import com.liferay.digital.signature.builder.DSCertifiedDeliveryParticipantBuilder;
import com.liferay.digital.signature.builder.DSDocumentBuilder;
import com.liferay.digital.signature.builder.DSEditorParticipantBuilder;
import com.liferay.digital.signature.builder.DSEmailNotificationBuilder;
import com.liferay.digital.signature.builder.DSFieldBuilder;
import com.liferay.digital.signature.builder.DSInPersonSignerNotaryParticipantBuilder;
import com.liferay.digital.signature.builder.DSInPersonSignerParticipantBuilder;
import com.liferay.digital.signature.builder.DSIntermediaryParticipantBuilder;
import com.liferay.digital.signature.builder.DSSealParticipantBuilder;
import com.liferay.digital.signature.builder.DSSignatureRequestBuilder;
import com.liferay.digital.signature.builder.DSSignerParticipantBuilder;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(service = DSBuildersFactory.class)
public class DSBuildersFactoryImpl implements DSBuildersFactory {

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
	public DSFieldBuilder createDSFieldBuilder() {
		return null;
	}

	@Override
	public DSInPersonSignerNotaryParticipantBuilder
		createDSInPersonSignerNotaryParticipantBuilder(
			String name, String email, int routingOrder, String notaryHost,
			String notaryName, String notaryEmail) {

		return new DSInPersonSignerNotaryParticipantBuilderImpl(
			name, email, routingOrder, notaryHost, notaryName, notaryEmail);
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
	public DSSignatureRequestBuilder createDSRequestBuilder() {
		return new DSSignatureRequestBuilderImpl();
	}

	@Override
	public DSSealParticipantBuilder createDSSealParticipantBuilder(
		String participantId, String name, String email, int routingOrder) {

		return new DSSealParticipantBuilderImpl(
			participantId, name, email, routingOrder);
	}

	@Override
	public DSSignerParticipantBuilder createDSSignerParticipantBuilder(
		String name, String email, int routingOrder) {

		return new DSSignerParticipantBuilderImpl(name, email, routingOrder);
	}

}