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
import com.ntatvr.core.repositories.AuthorRepository;
import com.ntatvr.core.services.AuthorService;
import com.ntatvr.domain.entities.author.AuthorEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository repository;
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
    log.debug("[Get Book]: {}", id);
    return repository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Transactional
  public AuthorEntity save(final AuthorEntity entity) {
    log.debug("[Save Author]: {}", entity);
    validate(entity);
    // TODO: Update All reference
    return repository.save(entity);
  }

  @Override
  @Transactional
  public void deleteById(final String id) {
    log.debug("[Delete Author]: {}", id);
    final AuthorEntity entity = findById(id);
    // TODO: Delete all reference
    repository.markDeleted(entity);
  }
}
