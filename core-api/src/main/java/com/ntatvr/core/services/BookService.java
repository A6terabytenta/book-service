package com.ntatvr.core.services;

import java.util.List;

import com.ntatvr.domain.entities.author.AuthorEntity;
import com.ntatvr.domain.entities.book.BookEntity;

public interface BookService {

  /**
   * Used to validate business logic and reference data if needed. And, we can customise the error messages as well.
   *
   * @param entity {@link AuthorEntity}
   */
  void validate(final BookEntity entity);

  BookEntity findById(final String id);

  List<BookEntity> findByAuthorIds(final List<String> ids);

  BookEntity save(final BookEntity entity);

  void deleteById(final String id);

  void deleteAllByAuthorIds(final List<String> ids);

  List<BookEntity> updateByAuthor(final AuthorEntity authorEntity);
}
