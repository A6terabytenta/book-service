package com.ntatvr.core.aggregation;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.FacetOperation;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.util.Assert;

import com.ntatvr.core.helps.CollectionHelpers;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryBuilder {

  private static final String PAGING_TOTAL = "total";
  private static final String PAGING = "paging";
  private static final String CONTENT = "content";

  public static String extractCollectionName(final Class<?> clazz) {
    // In case @Document("collection")
    if (StringUtils.isNotEmpty(clazz.getAnnotation(org.springframework.data.mongodb.core.mapping.Document.class).value())) {
      return clazz.getAnnotation(org.springframework.data.mongodb.core.mapping.Document.class).value();
    }
    // In case @Document(collection = "collection")
    return clazz.getAnnotation(org.springframework.data.mongodb.core.mapping.Document.class).collection();
  }

  public static Aggregation buildAggregation(final List<AggregationOperation> allOperations) {
    return Aggregation.newAggregation(allOperations)
        .withOptions(
            AggregationOptions.builder()
                .collation(Collation.of(Locale.ENGLISH))
                .allowDiskUse(Boolean.TRUE)
                .build());
  }

  public static FacetOperation buildCommonFacet(final Pageable pageable) {
    return Aggregation.facet()
        .and(Aggregation.count().as(PAGING_TOTAL)).as(PAGING)
        .and(
            Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()),
            Aggregation.limit(pageable.getPageSize())
        ).as(CONTENT);
  }

  public static String buildAggregationFieldName(final String collectionName, final String fieldName) {
    return collectionName.concat(".").concat(fieldName);
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class PaginationData<T> {
    private static final String DOCUMENT_RESULTS = "results";

    private List<T> items;
    private long total;

    public PaginationData(final MongoTemplate mongoTemplate, final Class<T> clazz, final Document document) {
      Assert.notNull(clazz, "Convert class cannot be null");
      Assert.notNull(document, "Document cannot be null");

      final Document result = CollectionHelpers.toFilteredStream(document.get(DOCUMENT_RESULTS), Document.class)
          .findFirst()
          .orElseThrow();

      this.total = CollectionHelpers.toFilteredStream(result.get(PAGING), Document.class)
          .findAny()
          .map(doc -> doc.get(PAGING_TOTAL))
          .map(Object::toString)
          .map(Long::parseLong)
          .orElse(0L);

      this.items = CollectionHelpers.toFilteredStream(result.get(CONTENT), Document.class)
          .map(item -> mongoTemplate.getConverter().read(clazz, item))
          .collect(Collectors.toList());
    }

  }
}
