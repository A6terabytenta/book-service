package com.ntatvr.core.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntatvr.core.exceptions.EntityNotFoundException;
import com.ntatvr.core.repositories.AuthorRepository;
import com.ntatvr.core.repositories.BookRepository;
import com.ntatvr.core.services.BookService;
import com.ntatvr.domain.entities.BaseEntity;
import com.ntatvr.domain.entities.author.AuthorEntity;
import com.ntatvr.domain.entities.book.BookEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookRepository repository;
  private final AuthorRepository authorRepository;
  private final Validator validator;

  @Override
  public void validate(final BookEntity entity) {
    final Set<ConstraintViolation<Object>> violations = this.validator.validate(entity);
    if (CollectionUtils.isNotEmpty(violations)) {
      throw new ConstraintViolationException(violations);
    }
  }

  @Override
  public BookEntity findById(final String id) {
    log.debug("[Get Book]: {}", id);
    return repository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public List<BookEntity> findByAuthorIds(final List<String> ids) {
    return repository.findByAuthorsIdInAndIsDeletedFalse(ids);
  }

  @Override
  @Transactional
  public BookEntity save(final BookEntity entity) {
    log.debug("[Save Book]: {}", entity);
    validate(entity);

    final Map<String, String> authorMap = authorRepository.findByIdInAndIsDeletedFalse(entity.collectAuthorIds())
        .stream().collect(Collectors.toMap(BaseEntity::getId, AuthorEntity::getFullName));
    final List<String> removedAuthors = new ArrayList<>();
    entity.getAuthors().forEach(author -> {
      if (authorMap.containsKey(author.getId())) {
        author.setFullName(authorMap.get(author.getId()));
      } else {
        removedAuthors.add(author.getId());
      }
    });

    if (CollectionUtils.isNotEmpty(removedAuthors)) {
      throw new EntityNotFoundException("Authors " + Arrays.toString(removedAuthors.toArray()) + " not found");
    }

    return repository.save(entity);
  }

  @Override
  @Transactional
  public void deleteById(final String id) {
    log.debug("[deleteById]: {}", id);
    final BookEntity entity = findById(id);
    repository.markDeleted(entity);
  }

  @Override
  public void deleteAllByAuthorIds(final List<String> ids) {
    log.debug("[deleteAllByAuthorIds]: {}", ids);
    final List<BookEntity> bookEntities = findByAuthorIds(ids);
    bookEntities.forEach(book -> book.setIsDeleted(true));
    repository.saveAll(bookEntities);
  }

  @Override
  public List<BookEntity> updateByAuthor(final AuthorEntity authorEntity) {
    log.debug("[updateByAuthor]: {}", authorEntity);
    final List<BookEntity> bookEntities = findByAuthorIds(List.of(authorEntity.getId()));
    bookEntities.forEach(book -> book.getAuthors().stream()
        .filter(author -> author.getId().equals(authorEntity.getId()))
        .forEach(author -> author.setFullName(authorEntity.getFullName())));
    return repository.saveAll(bookEntities);
  }
}
