package com.ntatvr.core.controllers.validate;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.ntatvr.core.controllers.request.SearchRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookFilterValidator {

  public static final String FILTER_LAST_MODIFIED_SORT_ASC = "lastModified,asc";
  public static final String FILTER_LAST_MODIFIED_SORT_DESC = "lastModified,desc";
  public static final String FILTER_CREATED_SORT_ASC = "created,asc";
  public static final String FILTER_CREATED_SORT_DESC = "created,desc";
  public static final String AUTHOR_PARAM = "author";
  public static final String SORT_PARAM = "sort";
  public static final String PAGE_PARAM = "page";
  public static final String SIZE_PARAM = "size";

  private static final List<String> SUPPORTED_SORT_FIELD_NAMES =
      List.of(FILTER_LAST_MODIFIED_SORT_ASC,
          FILTER_LAST_MODIFIED_SORT_DESC,
          FILTER_CREATED_SORT_ASC,
          FILTER_CREATED_SORT_DESC);

  public static final List<String> SUPPORTED_FILTER_FIELD_NAMES = List.of(
      AUTHOR_PARAM, SORT_PARAM, PAGE_PARAM, SIZE_PARAM
  );

  public static void validateSearchRequest(final SearchRequest searchRequest) {
    SearchRequestValidator.validateSearchRequest(
        searchRequest,
        SUPPORTED_SORT_FIELD_NAMES,
        SUPPORTED_FILTER_FIELD_NAMES);
  }
}
