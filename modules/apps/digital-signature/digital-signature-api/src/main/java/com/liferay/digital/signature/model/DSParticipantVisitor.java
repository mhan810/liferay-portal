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

package com.liferay.digital.signature.model;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface DSParticipantVisitor<T> {

	public T visit(DSAgentParticipant dsAgentParticipant);

	public T visit(DSCarbonCopyParticipant dsCarbonCopyParticipant);

	public T visit(
		DSCertifiedDeliveryParticipant dsCertifiedDeliveryParticipant);

	public T visit(DSEditorParticipant dsEditorParticipant);

	public T visit(
		DSInPersonSignerNotaryParticipant dsInPersonSignerNotaryParticipant);

	public T visit(DSInPersonSignerParticipant dsInPersonSignerParticipant);

	public T visit(DSIntermediaryParticipant dsIntermediaryParticipant);

	public T visit(DSSealParticipant dsSealParticipant);

	public T visit(DSSignerParticipant dsSignerParticipant);

}