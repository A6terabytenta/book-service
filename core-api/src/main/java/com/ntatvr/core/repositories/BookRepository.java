package com.ntatvr.core.repositories;

import org.springframework.stereotype.Repository;

import com.ntatvr.domain.entities.book.BookEntity;

@Repository
public interface BookRepository extends BaseRepository<BookEntity, String> {
}
