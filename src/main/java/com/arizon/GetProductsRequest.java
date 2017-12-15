package com.arizon;

import java.util.List;
import java.util.Map;

public class GetProductsRequest {

	private String categoryId;

	private Map<String, List<String>> facetsMap;

	public GetProductsRequest(String categoryId, Map<String, List<String>> facetsMap) {
		super();
		this.categoryId = categoryId;
		this.facetsMap = facetsMap;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Map<String, List<String>> getFacetsMap() {
		return facetsMap;
	}

	public void setFacetsMap(Map<String, List<String>> facetsMap) {
		this.facetsMap = facetsMap;
	}

}
