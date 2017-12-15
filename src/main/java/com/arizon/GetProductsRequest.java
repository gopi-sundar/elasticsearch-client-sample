package com.arizon;

import java.util.List;
import java.util.Map;

public class GetProductsRequest {

	private long categoryId;

	private Map<String, List<String>> facetsMap;

	public GetProductsRequest(long categoryId, Map<String, List<String>> facetsMap) {
		super();
		this.categoryId = categoryId;
		this.facetsMap = facetsMap;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public Map<String, List<String>> getFacetsMap() {
		return facetsMap;
	}

	public void setFacetsMap(Map<String, List<String>> facetsMap) {
		this.facetsMap = facetsMap;
	}

}
