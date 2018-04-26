package com.liferay.portal.search.engine.adapter.document;

import com.liferay.portal.search.engine.adapter.DocumentResponse;

/**
 * @author Dylan Rebelak
 */
public class UpdateByQueryDocumentResponse implements DocumentResponse {
	public UpdateByQueryDocumentResponse(long updated, long took) {
		_updated = updated;
		_took = took;
	}

	public long getUpdated() {
		return _updated;
	}

	public long getTook() {
		return _took;
	}

	private final long _updated;
	private final long _took;
}
