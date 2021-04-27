package com.ntatvr.core.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ntatvr.core.MockTest;
import com.ntatvr.core.TestDataProvider;
import com.ntatvr.core.exceptions.EntityNotFoundException;
import com.ntatvr.core.repositories.AuthorRepository;
import com.ntatvr.core.services.impl.AuthorServiceImpl;
import com.ntatvr.domain.entities.author.AuthorEntity;

public class AuthorServiceTest extends MockTest {

  private static final String AUTHOR_ID = "6084331d392464040b27ccc5";

  @InjectMocks
  private AuthorServiceImpl authorService;

  @Mock
  private AuthorRepository repository;

  @Mock
  private BookService bookService;


  @Before
  public void before() {
    when(validator.validate(any())).thenReturn(Collections.emptySet());
  }

  @Test
  public void findById_should_invoke_repository() {
    when(repository.findById(eq(AUTHOR_ID))).thenReturn(Optional.of(new AuthorEntity()));
    authorService.findById(AUTHOR_ID);
    verify(repository, times(1)).findById(AUTHOR_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void findById_should_throw_exception() {
    when(repository.findById(eq(AUTHOR_ID))).thenReturn(Optional.empty());
    authorService.findById(AUTHOR_ID);
  }

  @Test
  public void save_should_invoke_repository() {
    final AuthorEntity authorEntity = TestDataProvider.buildAuthorEntity();
    authorEntity.setId(AUTHOR_ID);
    authorService.save(authorEntity);
    verify(bookService, times(1)).updateByAuthor(authorEntity);
  }

  @Test
  public void deleteById_should_invoke_repository() {
    when(repository.findById(any())).thenReturn(Optional.of(new AuthorEntity()));
    authorService.deleteById(AUTHOR_ID);
    verify(bookService, times(1)).deleteAllByAuthorIds(any());
  }
}