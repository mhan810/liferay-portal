package com.liferay.portal.search.engine.adapter.document;

import com.liferay.portal.search.engine.adapter.DocumentResponse;

/**
 * @author Dylan Rebelak
 */
public class DeleteDocumentResponse implements DocumentResponse {
	public DeleteDocumentResponse(int status) {
		_status = status;
	}

	public int getStatus() {
		return _status;
	}

	private final int _status;
}
