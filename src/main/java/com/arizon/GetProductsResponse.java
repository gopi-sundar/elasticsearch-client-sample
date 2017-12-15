package com.arizon;

import java.util.List;
import java.util.Map;

public class GetProductsResponse {

	private boolean success;
	private String errorMessage;
	private List<ProductDO> productList;
	private Map<String, Map<String, Long>> facetsCountMap;

	public Map<String, Map<String, Long>> getFacetsCountMap() {
		return facetsCountMap;
	}

	public void setFacetsCountMap(Map<String, Map<String, Long>> facetsCountMap) {
		this.facetsCountMap = facetsCountMap;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public List<ProductDO> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductDO> productList) {
		this.productList = productList;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
