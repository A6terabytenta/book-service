package com.ntatvr.core.controllers.validate;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.ntatvr.core.controllers.request.SearchRequest;
import com.ntatvr.core.exceptions.UnsupportedFilterFieldException;
import com.ntatvr.core.exceptions.UnsupportedSortFieldException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchRequestValidator {
  public static final String SORT_PARAM = "sort";

  public static void validateSearchRequest(final SearchRequest searchRequest,
                                           final List<String> supportSortFields,
                                           final List<String> supportFilterFields) {
    validateSortRequestParameter(searchRequest.getFilters(), supportSortFields);
    validateFilterFieldNames(searchRequest.getFilters(), supportFilterFields);
  }

  public static void validateSortRequestParameter(
      final Map<String, String> requestParameters, final List<String> supportedSortFields) {
    if (requestParameters.containsKey(SORT_PARAM)) {
      final String sortFieldName = requestParameters.get(SORT_PARAM);
      if (!StringUtils.isEmpty(sortFieldName) && !supportedSortFields.contains(sortFieldName)) {
        throw new UnsupportedSortFieldException();
      }
    }
  }

  public static void validateFilterFieldNames(final Map<String, String> requestParameters,
                                              final List<String> supportedFilterFields) {

    if (MapUtils.isEmpty(requestParameters)) {
      return;
    }

    if (!supportedFilterFields.containsAll(requestParameters.keySet())) {
      throw new UnsupportedFilterFieldException();
    }
  }
}
