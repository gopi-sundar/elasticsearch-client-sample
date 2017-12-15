package com.arizon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ElasticSearchClientApplication {

	private static Logger logger = LoggerFactory.getLogger(ElasticSearchClientApplication.class);

	public static void main(String[] args) {

		ApplicationContext applicationContext = SpringApplication.run(ElasticSearchClientApplication.class, args);

		ElasticSearchClient elasticSearchClient = applicationContext.getBean(ElasticSearchClient.class);

		String categoryId = "145";

		Map<String, List<String>> facetsMap = new HashMap<String, List<String>>();
		List<String> materialValues = new ArrayList<String>();
		materialValues.add("Plastic");
		materialValues.add("Wood");

		List<String> colorValues = new ArrayList<String>();
		colorValues.add("Seagreen");
		colorValues.add("Natural");

		List<String> heightValues = new ArrayList<String>();
		heightValues.add("1");
		heightValues.add("1.75");

		facetsMap.put("Material", materialValues);
		facetsMap.put("Color", colorValues);
		facetsMap.put("Height", heightValues);

		// Query 1
		runGetProducts(elasticSearchClient, new GetProductsRequest(categoryId, facetsMap));

		categoryId = "145";

		facetsMap.clear();
		facetsMap.put("Material", materialValues);
		facetsMap.put("Color", colorValues);

		// Query 2
		runGetProducts(elasticSearchClient, new GetProductsRequest(categoryId, facetsMap));

	}

	private static void runGetProducts(ElasticSearchClient elasticSearchClient, GetProductsRequest getProductsRequest) {

		logger.debug("Before");

		GetProductsResponse response = elasticSearchClient.getProducts(getProductsRequest);

		logger.debug("After");

		if (response.isSuccess()) {
			System.out.println("Total number of products is >>> " + response.getProductList().size());
			System.out.println("Total number of facets >>> " + response.getFacetsCountMap().size());
		}
	}
}