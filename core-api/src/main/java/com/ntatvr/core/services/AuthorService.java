package com.ntatvr.core.services;

import java.util.List;

import com.ntatvr.domain.entities.author.AuthorEntity;

public interface AuthorService {

  void validate(final AuthorEntity entity);

  AuthorEntity findById(final String id);

  List<AuthorEntity> findByIds(final List<String> ids);

  AuthorEntity save(final AuthorEntity entity);

  void deleteById(final String id);
}
