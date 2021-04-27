package com.ntatvr.core.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ntatvr.domain.entities.book.BookEntity;

@Repository
public interface BookRepository extends BaseRepository<BookEntity, String> {

  List<BookEntity> findByAuthorsIdInAndIsDeletedFalse(Collection<String> ids);

}
