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

import com.liferay.digital.signature.builder.DSParticipantBuilder;
import com.liferay.digital.signature.builder.SigningDSParticipantBuilder;
import com.liferay.digital.signature.internal.request.DSSignatureInfoImpl;
import com.liferay.digital.signature.internal.request.DSSignerParticipantImpl;
import com.liferay.digital.signature.request.DSField;
import com.liferay.digital.signature.request.DSParticipant;
import com.liferay.digital.signature.request.DSSignatureInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public abstract class BaseSigningDSParticipantBuilder<T extends DSParticipant>
	extends BaseDSParticipantBuilder implements SigningDSParticipantBuilder {

	public BaseSigningDSParticipantBuilder(
		String name, String email, int routingOrder) {

		super(name, email, routingOrder);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends DSParticipantBuilder> S addDSFields(
		Collection<DSField> dsFields) {

		_dsFields.addAll(dsFields);

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends DSParticipantBuilder> S addDSFields(DSField... dsFields) {
		Collections.addAll(_dsFields, dsFields);

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends DSParticipantBuilder> S setAutoNavigation(
		Boolean autoNavigation) {

		_autoNavigation = autoNavigation;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends DSParticipantBuilder> S setDefaultParticipant(
		Boolean defaultParticipant) {

		_defaultParticipant = defaultParticipant;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends DSParticipantBuilder> S setSignatureInfo(
		String signatureName, String signatureInitials, String signatureFont) {

		DSSignatureInfoImpl dsSignatureInfoImpl = new DSSignatureInfoImpl();

		dsSignatureInfoImpl.setSignatureName(signatureName);
		dsSignatureInfoImpl.setSignatureInitials(signatureInitials);
		dsSignatureInfoImpl.setSignatureFont(signatureFont);

		_dsSignatureInfo = dsSignatureInfoImpl;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends DSParticipantBuilder> S setSignInEachLocation(
		Boolean signInEachLocation) {

		_signInEachLocation = signInEachLocation;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected T createDSParticipant() {
		DSSignerParticipantImpl dsSignerParticipantImpl =
			createDSSignerParticipantImpl();

		dsSignerParticipantImpl.setAutoNavigation(getAutoNavigation());
		dsSignerParticipantImpl.setDefaultParticipant(getDefaultParticipant());
		dsSignerParticipantImpl.setDSSignatureInfo(getDSSignatureInfo());
		dsSignerParticipantImpl.setSignInEachLocation(getSignInEachLocation());

		dsSignerParticipantImpl.addDSFields(getDSFields());

		return (T)dsSignerParticipantImpl;
	}

	protected abstract DSSignerParticipantImpl createDSSignerParticipantImpl();

	protected Boolean getAutoNavigation() {
		return _autoNavigation;
	}

	protected Boolean getDefaultParticipant() {
		return _defaultParticipant;
	}

	protected Set<DSField> getDSFields() {
		return _dsFields;
	}

	protected DSSignatureInfo getDSSignatureInfo() {
		return _dsSignatureInfo;
	}

	protected Boolean getSignInEachLocation() {
		return _signInEachLocation;
	}

	private Boolean _autoNavigation;
	private Boolean _defaultParticipant;
	private Set<DSField> _dsFields = new HashSet<>();
	private DSSignatureInfo _dsSignatureInfo;
	private Boolean _signInEachLocation;

}