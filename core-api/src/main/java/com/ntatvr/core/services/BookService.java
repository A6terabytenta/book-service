package com.ntatvr.core.services;

import com.ntatvr.domain.entities.book.BookEntity;

public interface BookService {

  void validate(final BookEntity entity);

  BookEntity findById(final String id);

  BookEntity save(final BookEntity entity);

  void deleteById(final String id);
}
