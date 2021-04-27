package com.ntatvr.core.services;

import java.util.List;

import com.ntatvr.domain.entities.author.AuthorEntity;


public interface AuthorService {

  /**
   * Used to validate business logic and reference data if needed. And, we can customise the error messages as well.
   *
   * @param entity {@link AuthorEntity}
   */
  void validate(final AuthorEntity entity);

  AuthorEntity findById(final String id);

  List<AuthorEntity> findByIds(final List<String> ids);

  AuthorEntity save(final AuthorEntity entity);

  void deleteById(final String id);
}
