package com.ntatvr.core.services.filter;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.ntatvr.core.aggregation.QueryBuilder;
import com.ntatvr.core.controllers.request.SearchRequest;
import com.ntatvr.core.controllers.validate.BookFilterValidator;
import com.ntatvr.domain.entities.BaseEntity;
import com.ntatvr.domain.entities.book.BookEntity;

@Service(BookFilterService.SERVICE_NAME)
public class BookFilterService extends BaseFilterService {

  public static final String SERVICE_NAME = "bookFilter";
  public static final String CASE_INSENSITIVE_OPTION = "i";

  @Override
  public Page<BookEntity> filter(final SearchRequest searchRequest) {
    return filter(searchRequest, BookEntity.class);
  }

  @Override
  public List<AggregationOperation> buildAggregations(final SearchRequest searchRequest) {
    final Map<String, String> parameters = searchRequest.getFilters();
    final List<AggregationOperation> aggregations = new ArrayList<>();
    this.buildAuthorCriteria(
        parameters.get(BookFilterValidator.AUTHOR_PARAM)
    ).map(Aggregation::match).ifPresent(aggregations::add);

    this.buildSortOperation(searchRequest).ifPresent(aggregations::add);
    return aggregations;
  }

  private Optional<SortOperation> buildSortOperation(final SearchRequest searchRequest) {

    if (searchRequest.getPageable().getSort().isEmpty()) {
      return Optional.of(Aggregation.sort(Sort.Direction.DESC, BaseEntity.LAST_MODIFIED_FIELD));
    }

    return searchRequest.getPageable().getSort()
        .map(order -> {
          final Sort.Direction direction = order.getDirection();
          final String property = order.getProperty();
          return Aggregation.sort(
              direction,
              property,
              QueryBuilder.buildAggregationFieldName(BookEntity.COLLECTION_NAME, property)
          );
        })
        .stream()
        .findAny();
  }

  private Optional<Criteria> buildAuthorCriteria(final String textSearch) {
    if (isBlank(textSearch)) {
      return Optional.empty();
    }

    final String literalTextSearch = Pattern.quote(textSearch);
    return Optional.of(Criteria.where(BookEntity.AUTHORS_FULL_NAME).regex(literalTextSearch, CASE_INSENSITIVE_OPTION));
  }
}
