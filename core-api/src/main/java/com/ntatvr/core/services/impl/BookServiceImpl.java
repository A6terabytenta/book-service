package com.ntatvr.core.services.impl;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntatvr.core.exceptions.EntityNotFoundException;
import com.ntatvr.core.repositories.BookRepository;
import com.ntatvr.core.services.BookService;
import com.ntatvr.domain.entities.book.BookEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookRepository repository;
  private final Validator validator;

  @Override
  public void validate(final BookEntity entity) {
    final Set<ConstraintViolation<Object>> violations = this.validator.validate(entity);
    if (CollectionUtils.isNotEmpty(violations)) {
      throw new ConstraintViolationException(violations);
    }
    // validate authors
  }

  @Override
  public BookEntity findById(final String id) {
    log.debug("[Get Book]: {}", id);
    return repository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Transactional
  public BookEntity save(final BookEntity entity) {
    log.debug("[Save Book]: {}", entity);
    validate(entity);
    // Get Author and set fullname
    return repository.save(entity);
  }

  @Override
  @Transactional
  public void deleteById(final String id) {
    log.debug("[Delete Book]: {}", id);
    final BookEntity entity = findById(id);
    repository.markDeleted(entity);
  }
}
