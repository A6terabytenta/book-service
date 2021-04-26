package com.ntatvr.core.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntatvr.core.api.model.BookEntityResponse;
import com.ntatvr.core.api.model.BookFilterPagingResponse;
import com.ntatvr.core.controllers.converter.BookMapper;
import com.ntatvr.core.controllers.request.SearchRequest;
import com.ntatvr.core.services.filter.BookFilterService;
import com.ntatvr.domain.entities.BaseEntity;
import com.ntatvr.domain.entities.book.BookEntity;

@RequiredArgsConstructor
@RestController
@RequestMapping(BookFilterController.BOOKS_FILTER_ENDPOINT)
public class BookFilterController {

  public static final String BOOKS_FILTER_ENDPOINT = "/books/filter";

  @Qualifier(BookFilterService.SERVICE_NAME)
  private final BookFilterService bookFilterService;

  private final BookMapper bookMapper;

  @GetMapping
  public ResponseEntity<BookFilterPagingResponse> filter(final @RequestParam Map<String, String> filters,
                                                         final @PageableDefault(
                                                             sort = BaseEntity.LAST_MODIFIED_FIELD,
                                                             direction = Sort.Direction.DESC) Pageable pageable) {
    final Page<BookEntity> filterResult = bookFilterService.filter(new SearchRequest(filters, pageable));
    final List<BookEntityResponse> bookEntities = filterResult.getContent().stream()
        .map(bookMapper::toResponse)
        .collect(Collectors.toList());
    final BookFilterPagingResponse bookFilterPagingResponse = new BookFilterPagingResponse();
    bookFilterPagingResponse.content(bookEntities);
    bookFilterPagingResponse.setPageNumber(filterResult.getPageable().getPageNumber());
    bookFilterPagingResponse.setPageSize(filterResult.getPageable().getPageSize());
    bookFilterPagingResponse.setTotalPages(filterResult.getTotalPages());
    bookFilterPagingResponse.setTotalElements(filterResult.getTotalElements());
    return ResponseEntity.ok(bookFilterPagingResponse);
  }
}
