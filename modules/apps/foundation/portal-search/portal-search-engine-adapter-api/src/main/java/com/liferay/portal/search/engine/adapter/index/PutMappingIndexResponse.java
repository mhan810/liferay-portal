package com.liferay.portal.search.engine.adapter.index;

import com.liferay.portal.search.engine.adapter.IndexResponse;

/**
 * @author Dylan Rebelak
 */
public class PutMappingIndexResponse implements IndexResponse {

	public PutMappingIndexResponse(Boolean acknowledged) {
		_acknowledged = acknowledged;
	}

	public Boolean getAcknowledged() {
		return _acknowledged;
	}

	private final Boolean _acknowledged;

}