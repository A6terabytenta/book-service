package com.ntatvr.core.services.filter;

import org.springframework.data.domain.Page;

import com.ntatvr.core.controllers.request.SearchRequest;
import com.ntatvr.domain.entities.BaseEntity;

public interface EntityFilterService {

  <T extends BaseEntity> Page<T> filter(final SearchRequest searchRequest);
}
