package com.ntatvr.core.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.ntatvr.domain.entities.BaseEntity;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, K extends String> extends MongoRepository<T, K> {

  Optional<T> findByIdAndIsDeletedFalse(K id);

  @Override
  default Optional<T> findById(final K id) {
    return this.findByIdAndIsDeletedFalse(id);
  }

  default <S extends T> S saveAndUpdateLastModifiedDate(S s) {
    s.setLastModified();
    return this.save(s);
  }

  default void markDeleted(final T entity) {
    entity.setIsDeleted(true);
    this.saveAndUpdateLastModifiedDate(entity);
  }

  Page<T> findByIsDeletedFalse(Pageable pageable);

  @Override
  default Page<T> findAll(Pageable pageable) {
    return findByIsDeletedFalse(pageable);
  }

  @Override
  default void deleteById(final K id) {
    findById(id).ifPresent(entity -> {
      entity.setIsDeleted(true);
      saveAndUpdateLastModifiedDate(entity);
    });
  }
}
