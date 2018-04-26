package com.liferay.portal.search.engine.adapter.document;

import com.liferay.portal.search.engine.adapter.DocumentResponse;

/**
 * @author Dylan Rebelak
 */
public class DeleteByQueryDocumentResponse implements DocumentResponse {
	public DeleteByQueryDocumentResponse(long deleted, long took) {
		_deleted = deleted;
		_took = took;
	}

	public long getDeleted() {
		return _deleted;
	}

	public long getTook() {
		return _took;
	}

	private final long _deleted;
	private final long _took;
}
