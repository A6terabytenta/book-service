package com.ntatvr.core.services;

import com.ntatvr.domain.entities.author.AuthorEntity;

public interface AuthorService {

  void validate(final AuthorEntity entity);

  AuthorEntity findById(final String id);

  AuthorEntity save(final AuthorEntity entity);

  void deleteById(final String id);
}
