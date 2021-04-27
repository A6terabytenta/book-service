package com.ntatvr.core.services.impl;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntatvr.core.exceptions.EntityNotFoundException;
import com.ntatvr.core.repositories.AuthorRepository;
import com.ntatvr.core.services.AuthorService;
import com.ntatvr.core.services.BookService;
import com.ntatvr.domain.entities.author.AuthorEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository repository;
  private final BookService bookService;
  private final Validator validator;

  @Override
  public void validate(final AuthorEntity entity) {
    final Set<ConstraintViolation<Object>> violations = this.validator.validate(entity);
    if (CollectionUtils.isNotEmpty(violations)) {
      throw new ConstraintViolationException(violations);
    }
  }

  @Override
  public AuthorEntity findById(final String id) {
    log.debug("[Get Author]: {}", id);
    return repository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public List<AuthorEntity> findByIds(final List<String> ids) {
    log.debug("[Get Author by Ids]: {}", ids);
    return repository.findByIdInAndIsDeletedFalse(ids);
  }

  @Override
  @Transactional
  public AuthorEntity save(final AuthorEntity entity) {
    log.debug("[Save Author]: {}", entity);
    validate(entity);

    if (StringUtils.isNotBlank(entity.getId())) {
      bookService.updateByAuthor(entity);
    }
    return repository.save(entity);
  }

  @Override
  @Transactional
  public void deleteById(final String id) {
    log.debug("[Delete Author]: {}", id);
    final AuthorEntity entity = findById(id);
    repository.markDeleted(entity);
    bookService.deleteAllByAuthorIds(List.of(id));
  }
}
