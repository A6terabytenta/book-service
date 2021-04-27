package com.ntatvr.core.controllers.request;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class SearchRequest {

  public Map<String, String> filters;

  public Pageable pageable;

}
