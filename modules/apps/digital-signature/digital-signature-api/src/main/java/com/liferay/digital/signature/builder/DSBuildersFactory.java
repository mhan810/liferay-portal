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

package com.liferay.digital.signature.builder;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface DSBuildersFactory {

	public DSAgentParticipantBuilder createDSAgentParticipantBuilder(
		String name, String email, int routingOrder);

	public DSCarbonCopyParticipantBuilder createDSCarbonCopyParticipantBuilder(
		String name, String email, int routingOrder);

	public DSCertifiedDeliveryParticipantBuilder
		createDSCertifiedDeliveryParticipantBuilder(
			String name, String email, int routingOrder);

	public DSDocumentBuilder createDSDocumentBuilder(
		String documentId, String name);

	public DSEditorParticipantBuilder createDSEditorParticipantBuilder(
		String name, String email, int routingOrder);

	public DSEmailNotificationBuilder createDSEmailNotificationBuilder(
		String subject, String message);

	public DSFieldBuilder createDSFieldBuilder();

	public DSInPersonSignerNotaryParticipantBuilder
		createDSInPersonSignerNotaryParticipantBuilder(
			String name, String email, int routingOrder, String notaryHost,
			String notaryName, String notaryEmail);

	public DSInPersonSignerParticipantBuilder
		createDSInPersonSignerParticipantBuilder(
			String hostName, String hostEmail, String signerName,
			String signerEmail, int routingOrder);

	public DSIntermediaryParticipantBuilder
		createDSIntermediaryParticipantBuilder(
			String name, String email, int routingOrder);

	public DSSignatureRequestBuilder createDSRequestBuilder();

	public DSSealParticipantBuilder createDSSealParticipantBuilder(
		String participantId, String name, String email, int routingOrder);

	public DSSignerParticipantBuilder createDSSignerParticipantBuilder(
		String name, String email, int routingOrder);

}