package com.ntatvr.core.aggregation;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.FacetOperation;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ntatvr.domain.entities.BaseEntity;

@Component
@RequiredArgsConstructor
public class QueryExecutor {

  private final MongoTemplate mongoTemplate;

  public <T extends BaseEntity> Page<T> filterWithPagination(final List<AggregationOperation> aggregationOperations,
                                                             final Class<T> clazz,
                                                             final Pageable pageable) {

    Assert.isTrue(pageable.isPaged(), "filterWithPagination only apply for paging query");
    final List<AggregationOperation> allOperations = new ArrayList<>(aggregationOperations);
    final FacetOperation paginationOperation = QueryBuilder.buildCommonFacet(pageable);
    allOperations.add(paginationOperation);

    final Document document = mongoTemplate.aggregate(
        QueryBuilder.buildAggregation(allOperations),
        QueryBuilder.extractCollectionName(clazz),
        Object.class
    ).getRawResults();
    final QueryBuilder.PaginationData<T> paginationData = new QueryBuilder.PaginationData<>(
        mongoTemplate,
        clazz,
        document);
    return new PageImpl<>(paginationData.getItems(), pageable, paginationData.getTotal());
  }

}
