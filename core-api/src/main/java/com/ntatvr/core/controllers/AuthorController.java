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

import com.ntatvr.core.api.AuthorApi;
import com.ntatvr.core.api.model.AuthorEntityRequest;
import com.ntatvr.core.api.model.AuthorEntityResponse;
import com.ntatvr.core.controllers.converter.AuthorMapper;
import com.ntatvr.core.services.AuthorService;
import com.ntatvr.domain.entities.author.AuthorEntity;

@RequiredArgsConstructor
@RestController
@RequestMapping(AuthorController.AUTHORS_ENDPOINT)
public class AuthorController implements AuthorApi {

  public static final String AUTHORS_ENDPOINT = "/authors";
  public static final String ID_PARAM_ENDPOINT = "/{id}";

  private final AuthorMapper authorMapper;
  private final AuthorService authorService;

  @Override
  @GetMapping(ID_PARAM_ENDPOINT)
  public ResponseEntity<AuthorEntityResponse> getAuthorById(final @PathVariable String id) {
    return ResponseEntity.ok(authorMapper.toResponse(authorService.findById(id)));
  }

  @Override
  @PostMapping
  public ResponseEntity<AuthorEntityResponse> insertAuthor(final @RequestBody AuthorEntityRequest request) {
    final AuthorEntity authorEntity = authorService.save(authorMapper.toEntity(request));
    return new ResponseEntity<>(authorMapper.toResponse(authorEntity), HttpStatus.CREATED);
  }

  @Override
  @PutMapping(ID_PARAM_ENDPOINT)
  public ResponseEntity<AuthorEntityResponse> updateAuthor(final @PathVariable String id,
                                                           final @RequestBody AuthorEntityRequest request) {
    final AuthorEntity oldEntity = authorService.findById(id);
    final AuthorEntity newEntity = authorMapper.toEntity(request);
    newEntity.setId(oldEntity.getId());
    return ResponseEntity.ok(authorMapper.toResponse(authorService.save(newEntity)));
  }

  @Override
  @DeleteMapping(ID_PARAM_ENDPOINT)
  public ResponseEntity<Void> deleteAuthorById(final @PathVariable String id) {
    authorService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
