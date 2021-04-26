package com.ntatvr.core.services.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import com.ntatvr.core.aggregation.QueryExecutor;
import com.ntatvr.core.controllers.request.SearchRequest;
import com.ntatvr.core.controllers.validate.BookFilterValidator;
import com.ntatvr.domain.entities.BaseEntity;

public abstract class BaseFilterService implements EntityFilterService {

  @Autowired
  private QueryExecutor queryExecutor;

  protected <T extends BaseEntity> Page<T> filter(final SearchRequest searchRequest,
                                                  final Class<T> clazz) {
    BookFilterValidator.validateSearchRequest(searchRequest);
    final List<AggregationOperation> aggregationOperations = this.buildAggregations(searchRequest);
    return queryExecutor.filterWithPagination(aggregationOperations, clazz, searchRequest.getPageable());
  }

  abstract List<AggregationOperation> buildAggregations(final SearchRequest searchRequest);
}
