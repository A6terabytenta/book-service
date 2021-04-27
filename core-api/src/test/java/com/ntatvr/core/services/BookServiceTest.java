package com.ntatvr.core.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ntatvr.core.MockTest;
import com.ntatvr.core.TestDataProvider;
import com.ntatvr.core.exceptions.EntityNotFoundException;
import com.ntatvr.core.repositories.AuthorRepository;
import com.ntatvr.core.repositories.BookRepository;
import com.ntatvr.core.services.impl.BookServiceImpl;
import com.ntatvr.domain.entities.author.AuthorEntity;
import com.ntatvr.domain.entities.book.BookEntity;

public class BookServiceTest extends MockTest {

  private static final String BOOK_ID = "6084331d392464040b27ade5";
  private static final String AUTHOR_ID = "6084331d392464040b27ccc5";

  @InjectMocks
  private BookServiceImpl bookService;

  @Mock
  private BookRepository repository;

  @Mock
  private AuthorRepository authorRepository;

  @Before
  public void before() {
    when(validator.validate(any())).thenReturn(Collections.emptySet());
  }

  @Test
  public void findById_should_invoke_repository() {
    when(repository.findById(eq(BOOK_ID))).thenReturn(Optional.of(new BookEntity()));
    bookService.findById(BOOK_ID);
    verify(repository, times(1)).findById(BOOK_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void findById_should_throw_exception() {
    when(repository.findById(eq(BOOK_ID))).thenReturn(Optional.empty());
    bookService.findById(BOOK_ID);
  }

  @Test
  public void save_should_invoke_repository() {
    final AuthorEntity authorEntity = TestDataProvider.buildAuthorEntity();
    authorEntity.setId(AUTHOR_ID);
    final BookEntity bookEntity = TestDataProvider.buildBookEntity(authorEntity);

    when(authorRepository.findByIdInAndIsDeletedFalse(any())).thenReturn(List.of(authorEntity));
    bookService.save(bookEntity);
    verify(repository, times(1)).save(bookEntity);
  }

  @Test(expected = EntityNotFoundException.class)
  public void save_should_throw_exception() {
    final AuthorEntity authorEntity = TestDataProvider.buildAuthorEntity();
    authorEntity.setId(AUTHOR_ID);
    final BookEntity bookEntity = TestDataProvider.buildBookEntity(authorEntity);

    when(authorRepository.findByIdInAndIsDeletedFalse(any())).thenReturn(List.of());
    bookService.save(bookEntity);
  }

  @Test
  public void deleteById_should_invoke_repository() {

    when(repository.findById(any())).thenReturn(Optional.of(new BookEntity()));
    bookService.deleteById(BOOK_ID);
    verify(repository, times(1)).markDeleted(any());
  }
}