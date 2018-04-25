package com.liferay.portal.search.engine.adapter.index;

import com.liferay.portal.search.engine.adapter.IndexResponse;

/**
 * @author Dylan Rebelak
 */
public class GetMappingIndexResponse implements IndexResponse {

	public GetMappingIndexResponse(String mappings) {
		_mappings = mappings;
	}

	public String getMappings() {
		return _mappings;
	}

	private final String _mappings;

}