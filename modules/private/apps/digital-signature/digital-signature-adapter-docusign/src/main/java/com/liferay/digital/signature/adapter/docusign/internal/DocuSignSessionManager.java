/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.digital.signature.adapter.docusign.internal;

import com.liferay.digital.signature.request.DSSessionId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Edward C. Han
 */
@Component(service = DocuSignSessionManager.class)
public class DocuSignSessionManager {

	public DocuSignSession getDocuSignSession(DSSessionId dsSessionId) {
		DocuSignSession docuSignSession = _docuSignSessions.get(dsSessionId);

		if (docuSignSession.isExpired()) {
			docuSignSession.connect();
		}

		return docuSignSession;
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void setDocuSignSession(DocuSignSession docuSignSession) {
		DSSessionId dsSessionId = docuSignSession.getDSSessionId();

		_docuSignSessions.put(dsSessionId, docuSignSession);
	}

	protected void unsetDocuSignSession(DocuSignSession docuSignSession) {
		DSSessionId dsSessionId = docuSignSession.getDSSessionId();

		_docuSignSessions.remove(dsSessionId);
	}

	private final Map<DSSessionId, DocuSignSession> _docuSignSessions =
		new ConcurrentHashMap<>();

}