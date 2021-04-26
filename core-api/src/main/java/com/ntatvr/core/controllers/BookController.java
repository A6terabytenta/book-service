package com.ntatvr.core.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntatvr.core.api.BookApi;
import com.ntatvr.core.api.model.BookEntityRequest;
import com.ntatvr.core.api.model.BookEntityResponse;
import com.ntatvr.core.controllers.converter.BookMapper;
import com.ntatvr.core.services.BookService;
import com.ntatvr.domain.entities.book.BookEntity;

@RequiredArgsConstructor
@RestController
@RequestMapping(BookController.BOOKS_ENDPOINT)
public class BookController implements BookApi {

  public static final String BOOKS_ENDPOINT = "/books";
  public static final String ID_PARAM_ENDPOINT = "/{id}";

  private final BookService bookService;
  private final BookMapper bookMapper;

  @Override
  @GetMapping(ID_PARAM_ENDPOINT)
  public ResponseEntity<BookEntityResponse> getBookById(final @PathVariable String id) {
    return ResponseEntity.ok(bookMapper.toResponse(bookService.findById(id)));
  }

  @Override
  @PostMapping
  public ResponseEntity<BookEntityResponse> insertBook(final @RequestBody BookEntityRequest request) {
    final BookEntity bookEntity = bookService.save(bookMapper.toEntity(request));
    return new ResponseEntity<>(bookMapper.toResponse(bookEntity), HttpStatus.CREATED);
  }

  @Override
  @PutMapping(ID_PARAM_ENDPOINT)
  public ResponseEntity<BookEntityResponse> updateBook(final @PathVariable String id,
                                                       final @RequestBody BookEntityRequest request) {
    final BookEntity oldEntity = bookService.findById(id);
    final BookEntity newEntity = bookMapper.toEntity(request);
    newEntity.setId(oldEntity.getId());
    return ResponseEntity.ok(bookMapper.toResponse(bookService.save(newEntity)));
  }

  @Override
  @DeleteMapping(ID_PARAM_ENDPOINT)
  public ResponseEntity<Void> deleteBookById(final @PathVariable String id) {
    bookService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
