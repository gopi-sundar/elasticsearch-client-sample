package com.arizon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchClient {

	private static Logger logger = LoggerFactory.getLogger(ElasticSearchClient.class);

	// Error messages
	private static final String ERR_GET_PRODUCTS = "Error occured while fetching products";

	private static final String ES_DOCUMENT_TYPE_PRODUCTS = "products";
	// TODO move ES_HOST and ES_INDEX to external properties file (residing outside
	// of spring project)
	private static final String ES_HOST = "ec2-34-201-101-90.compute-1.amazonaws.com";
	private static final String ES_INDEX = "rwdomain";
	private static final String ES_PROTOCOL_HTTP = "http";
	private static final short ES_PORT = 9200;

	// Fields
	private static final String F_BC_ID = "bc_id";
	private static final String F_BC_URL = "bc_url";
	private static final String F_CATEGORIES = "categories";
	private static final String F_CATEGORY_ID = "categories.category_id";
	private static final String F_CODE = "code";
	private static final String F_DESCRIPTION = "description";
	private static final String F_FACET_KEY = "facets.facet_key.keyword";
	private static final String F_FACET_VALUE = "facets.facet_value.keyword";
	private static final String F_FACETS = "facets";
	private static final String F_ID = "id";
	private static final String F_IMAGES = "images";
	private static final String F_LONG_DESCRIPTION = "long_description";
	private static final String F_NAME = "name";
	private static final String F_PRICE = "price";
	private static final String F_USE_AS_THUMBNAIL = "use_as_thumbnail";
	private static final String F_WEB_URL = "web_url";
	private static final String FACET_AGGREGATION = "facet_aggregation";
	private static final String FACET_KEY_AGGREGATION = "facet_key_aggregation";
	private static final String FACET_VALUE_AGGREGATION = "facet_value_aggregation";

	private static final String OTHER_FACETS = "OtherFacets";
	private static final String[] PRODUCTS_SOURCE_FIELDS;
	private static RestHighLevelClient ES_REST_CLIENT;

	static {
		PRODUCTS_SOURCE_FIELDS = new String[] { F_ID, F_BC_ID, F_NAME, F_CODE, F_LONG_DESCRIPTION, F_PRICE, F_IMAGES,
				F_BC_URL };
	}

	@PostConstruct
	private void init() {
		ES_REST_CLIENT = new RestHighLevelClient(RestClient.builder(new HttpHost(ES_HOST, ES_PORT, ES_PROTOCOL_HTTP)));
	}

	@PreDestroy
	private void destroy() {
		try {
			ES_REST_CLIENT.close();
			logger.debug("ES_REST_CLIENT closed");
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public GetProductsResponse getProducts(GetProductsRequest request) {

		logger.debug("Entering getProducts with 'categoryId':{} and 'facetsMap':{}", request.getCategoryId(),
				request.getFacetsMap());

		GetProductsResponse response = new GetProductsResponse();

		try {

			// Set the index and document type
			SearchRequest searchRequest = new SearchRequest(ES_INDEX);
			searchRequest.types(ES_DOCUMENT_TYPE_PRODUCTS);

			// Build the search request
			SearchSourceBuilder searchSourceBuilder = buildGetProductsSearchRequest(request);
			searchRequest.source(searchSourceBuilder);

			logger.debug("getProducts ES query is >>> {}", searchRequest);

			// Execute the search
			SearchResponse searchResponse = ES_REST_CLIENT.search(searchRequest);

			logger.debug("getProducts ES response is >>> {}", searchResponse);

			if (searchResponse.status() == RestStatus.OK) {
				response.setSuccess(true);
				processGetProductsSearchResponse(request, searchResponse, response);

			} else {
				response.setErrorMessage(ERR_GET_PRODUCTS);
			}

		} catch (ElasticsearchException e) {
			if (e.status() == RestStatus.NOT_FOUND) {
				logger.error("Index '{}' not found ", ES_INDEX, e);
				response.setErrorMessage(ERR_GET_PRODUCTS);
			}
		} catch (IOException e) {
			logger.error("", e);
			response.setErrorMessage(ERR_GET_PRODUCTS);
		}

		logger.debug("Exiting getProducts with \n'productList':{}\n'facetsCountMap':{}", response.getProductList(),
				response.getFacetsCountMap());
		return response;

	}

	private static SearchSourceBuilder buildGetProductsSearchRequest(GetProductsRequest request) {

		logger.debug("Entering buildGetProductsSearchRequest");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		// Source fields
		searchSourceBuilder.fetchSource(PRODUCTS_SOURCE_FIELDS, Strings.EMPTY_ARRAY);

		// Build the category_id query
		NestedQueryBuilder query = QueryBuilders.nestedQuery(F_CATEGORIES,
				QueryBuilders.termQuery(F_CATEGORY_ID, request.getCategoryId()), ScoreMode.Avg);
		searchSourceBuilder.query(query);

		// Build the postFilter
		final Map<String, BoolQueryBuilder> facetFilterMap = new HashMap<String, BoolQueryBuilder>();
		BoolQueryBuilder postFilter = new BoolQueryBuilder();
		request.getFacetsMap().forEach((key, valueArray) -> {
			BoolQueryBuilder postFilterInnerBoolQueryBuilder = new BoolQueryBuilder();
			postFilterInnerBoolQueryBuilder.must(QueryBuilders.termQuery(F_FACET_KEY, key));
			postFilterInnerBoolQueryBuilder.must(QueryBuilders.termsQuery(F_FACET_VALUE, valueArray));
			postFilter.must(QueryBuilders.nestedQuery(F_FACETS, postFilterInnerBoolQueryBuilder, ScoreMode.Avg));
			facetFilterMap.put(key, postFilterInnerBoolQueryBuilder);
		});

		searchSourceBuilder.postFilter(postFilter);

		// Build the facet bucket aggregation
		NestedAggregationBuilder facetAggregationBuilder = new NestedAggregationBuilder(FACET_AGGREGATION, F_FACETS);
		TermsAggregationBuilder facetKeyAggBuilder = AggregationBuilders.terms(FACET_KEY_AGGREGATION)
				.field(F_FACET_KEY);

		TermsAggregationBuilder facetValueAggBuilder = AggregationBuilders.terms(FACET_VALUE_AGGREGATION)
				.field(F_FACET_VALUE);

		facetKeyAggBuilder.subAggregation(facetValueAggBuilder);

		facetAggregationBuilder.subAggregation(facetKeyAggBuilder);

		// Build Aggregation filters
		BoolQueryBuilder allFacetFilterBuilder = new BoolQueryBuilder();
		facetFilterMap.forEach((outerKey, filterQuery) -> {
			BoolQueryBuilder singleFacetFilterBuilder = new BoolQueryBuilder();

			// Build aggregation filter for current facet
			facetFilterMap.forEach((innerkey, innerFilterQuery) -> {
				if (!outerKey.equals(innerkey)) {
					singleFacetFilterBuilder.must(QueryBuilders.nestedQuery(F_FACETS, innerFilterQuery, ScoreMode.Avg));
				}
			});

			// Add the aggregation for the current facet
			searchSourceBuilder.aggregation(AggregationBuilders.filter(outerKey, singleFacetFilterBuilder)
					.subAggregation(facetAggregationBuilder));

			allFacetFilterBuilder.must(QueryBuilders.nestedQuery(F_FACETS, filterQuery, ScoreMode.Avg));

		});

		// Add the aggregation for all other facets
		searchSourceBuilder.aggregation(AggregationBuilders.filter(OTHER_FACETS, allFacetFilterBuilder)
				.subAggregation(facetAggregationBuilder));

		logger.debug("Exiting buildGetProductsSearchRequest");

		return searchSourceBuilder;

	}

	private static void processGetProductsSearchResponse(GetProductsRequest request, SearchResponse searchResponse,
			GetProductsResponse response) {

		logger.debug("Entering processGetProductsSearchResponse");

		// Process the product list
		List<ProductDO> productList = new ArrayList<ProductDO>();
		searchResponse.getHits().forEach(product -> {
			Map<String, Object> sourceMap = product.getSourceAsMap();
			ProductDO productDO = new ProductDO();
			productDO.setId(((Integer) sourceMap.get(F_ID)).longValue());
			productDO.setBcId(((Integer) sourceMap.get(F_BC_ID)).longValue());
			productDO.setName((String) sourceMap.get(F_NAME));
			productDO.setDescription((String) sourceMap.get(F_LONG_DESCRIPTION));
			productDO.setRetailPrice((Double) sourceMap.get(F_PRICE));
			productDO.setSalePrice((Double) sourceMap.get(F_PRICE));
			productDO.setBcUrl((String) sourceMap.get(F_BC_URL));

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> sourceImages = (List<Map<String, Object>>) sourceMap.get(F_IMAGES);

			List<ProductImageDO> images = new ArrayList<ProductImageDO>();
			if (sourceImages != null) {
				sourceImages.forEach(map -> {
					ProductImageDO productImageDO = new ProductImageDO();
					productImageDO.setId((Integer) map.get(F_ID));
					productImageDO.setIsThumbnail("Y".equals(((String) map.get(F_USE_AS_THUMBNAIL))) ? true : false);
					if (productImageDO.getIsThumbnail()) {
						productImageDO.setThumbnailUrl((String) map.get(F_WEB_URL));
					}
					productImageDO.setStandardUrl((String) map.get(F_WEB_URL));
					productImageDO.setAltText((String) map.get(F_DESCRIPTION));
					images.add(productImageDO);
				});
			}

			productDO.setImages(images);
			productList.add(productDO);
		});

		response.setProductList(productList);

		// Process the aggregations
		Map<String, Map<String, Long>> facetCountsMap = new HashMap<String, Map<String, Long>>();

		// Get the counts for filtered facets
		searchResponse.getAggregations().forEach(aggregation -> {
			ParsedFilter parsedFilter = (ParsedFilter) aggregation;
			if (!OTHER_FACETS.equals(parsedFilter.getName())) {
				ParsedNested parsedNested = null;
				if (parsedFilter.getAggregations() != null) {
					parsedNested = parsedFilter.getAggregations().get(FACET_AGGREGATION);

					Terms keyTerms = parsedNested.getAggregations().get(FACET_KEY_AGGREGATION);

					if (keyTerms != null) {

						Bucket keyBucket = keyTerms.getBucketByKey(parsedFilter.getName());
						Map<String, Long> currentFacetCountsMap = new HashMap<String, Long>();
						Terms valueTerms = keyBucket.getAggregations().get(FACET_VALUE_AGGREGATION);
						valueTerms.getBuckets().forEach(valueBucket -> {
							currentFacetCountsMap.put(valueBucket.getKeyAsString(), valueBucket.getDocCount());
						});
						facetCountsMap.put(parsedFilter.getName(), currentFacetCountsMap);
					}
				}
			}

		});

		// Get the counts for all the non-filtered facets
		ParsedFilter parsedFilter = searchResponse.getAggregations().get(OTHER_FACETS);
		ParsedNested parsedNested = null;
		if (parsedFilter.getAggregations() != null) {
			parsedNested = parsedFilter.getAggregations().get(FACET_AGGREGATION);

			Terms keyTerms = parsedNested.getAggregations().get(FACET_KEY_AGGREGATION);

			if (keyTerms != null) {

				keyTerms.getBuckets().forEach(bucket -> {
					if (!request.getFacetsMap().containsKey(bucket.getKey())) {
						Map<String, Long> currentFacetCountsMap = new HashMap<String, Long>();
						Terms valueTerms = bucket.getAggregations().get(FACET_VALUE_AGGREGATION);
						valueTerms.getBuckets().forEach(valueBucket -> {
							currentFacetCountsMap.put(valueBucket.getKeyAsString(), valueBucket.getDocCount());
						});
						facetCountsMap.put(bucket.getKeyAsString(), currentFacetCountsMap);
					}
				});
			}
		}

		response.setFacetsCountMap(facetCountsMap);

		logger.debug("Exiting processGetProductsSearchResponse");
	}

}
