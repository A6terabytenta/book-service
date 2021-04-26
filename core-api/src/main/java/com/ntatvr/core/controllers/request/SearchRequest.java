package com.ntatvr.core.controllers.request;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.domain.Pageable;

@Getter
@Setter
@AllArgsConstructor
public class SearchRequest {

  public Map<String, String> filters;

  public Pageable pageable;

}
