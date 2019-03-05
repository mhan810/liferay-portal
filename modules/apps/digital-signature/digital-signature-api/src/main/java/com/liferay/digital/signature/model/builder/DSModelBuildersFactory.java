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

package com.liferay.digital.signature.model.builder;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface DSModelBuildersFactory {

	public AgentDSParticipantBuilder createDSAgentParticipantBuilder(
		String name, String email, int routingOrder);

	public CarbonCopyDSParticipantBuilder createDSCarbonCopyParticipantBuilder(
		String name, String email, int routingOrder);

	public CertifiedDeliveryDSParticipantBuilder
		createDSCertifiedDeliveryParticipantBuilder(
			String name, String email, int routingOrder);

	public DSDocumentBuilder createDSDocumentBuilder(
		String documentId, String name);

	public EditorDSParticipantBuilder createDSEditorParticipantBuilder(
		String name, String email, int routingOrder);

	public DSEmailNotificationBuilder createDSEmailNotificationBuilder(
		String subject, String message);

	public InPersonSignerNotaryDSParticipantBuilder
		createDSInPersonSignerNotaryParticipantBuilder(
			String name, String email, int routingOrder,
			String notaryParticipantId, String notaryName, String notaryEmail);

	public InPersonSignerDSParticipantBuilder
		createDSInPersonSignerParticipantBuilder(
			String hostName, String hostEmail, String signerName,
			String signerEmail, int routingOrder);

	public IntermediaryDSParticipantBuilder
		createDSIntermediaryParticipantBuilder(
			String name, String email, int routingOrder);

	public SealDSParticipantBuilder createDSSealParticipantBuilder(
		String participantId, String name, String email, int routingOrder);

	public DSSignaturePackageBuilder createDSSignatureRequestBuilder();

	public SignerDSParticipantBuilder createDSSignerParticipantBuilder(
		String name, String email, int routingOrder);

}