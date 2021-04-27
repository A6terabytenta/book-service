package com.ntatvr.core.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.ntatvr.core.IntegrationTest;
import com.ntatvr.core.TestDataProvider;
import com.ntatvr.core.controllers.validate.BookFilterValidator;
import com.ntatvr.core.exceptions.ErrorEnum;
import com.ntatvr.core.repositories.AuthorRepository;
import com.ntatvr.core.repositories.BookRepository;
import com.ntatvr.domain.entities.author.AuthorEntity;
import com.ntatvr.domain.entities.book.BookEntity;

public class BookFilterControllerTest extends IntegrationTest {

  private static final String BOOK_FILTER_ENDPOINT = BookFilterController.BOOKS_FILTER_ENDPOINT;
  private static final String NOT_EXIST_ID = "6084331d392464040b27ade5";
  private static final String INVALID_PARAM = "invalidParam";

  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private BookRepository bookRepository;

  private AuthorEntity authorEntity1;
  private BookEntity bookEntity1;
  private AuthorEntity authorEntity2;
  private BookEntity bookEntity2;

  @Before
  public void before() {
    authorEntity1 = authorRepository.save(TestDataProvider.buildAuthorEntity());
    bookEntity1 = bookRepository.save(TestDataProvider.buildBookEntity(authorEntity1));
    authorEntity2 = authorRepository.save(TestDataProvider.buildNewAuthorEntity());
    bookEntity2 = bookRepository.save(TestDataProvider.buildBookEntity(authorEntity2));
  }

  @After
  public void after() {
    mongoTemplate.dropCollection(BookEntity.COLLECTION_NAME);
    mongoTemplate.dropCollection(AuthorEntity.COLLECTION_NAME);
  }

  @Test
  public void filter_book_by_incorrect_filter_field_should_return_bad_request() throws Exception {

    this.mockMvc.perform(MockMvcRequestBuilders.get(BOOK_FILTER_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .param(INVALID_PARAM, INVALID_PARAM)
        .param(BookFilterValidator.SIZE_PARAM, String.valueOf(10)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.userMessage")
            .value(ErrorEnum.UNSUPPORTED_FILTER_FIELD_NAME_EXCEPTION.getUserMessage()));
  }

  @Test
  public void filter_book_by_incorrect_sort_field_should_return_bad_request() throws Exception {

    this.mockMvc.perform(MockMvcRequestBuilders.get(BOOK_FILTER_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .param(BookFilterValidator.SORT_PARAM, INVALID_PARAM)
        .param(BookFilterValidator.SIZE_PARAM, String.valueOf(10)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.userMessage")
            .value(ErrorEnum.UNSUPPORTED_SORT_FIELD_NAME_EXCEPTION.getUserMessage()));
  }

  @Test
  public void filter_book_without_filter_should_return_data() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(BOOK_FILTER_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .param(BookFilterValidator.SIZE_PARAM, String.valueOf(10)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.content").value(Matchers.hasSize(2)))
        .andExpect(jsonPath("$.totalElements").value(2));
  }

  @Test
  public void filter_book_with_filter_should_return_data_c1() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(BOOK_FILTER_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .param(BookFilterValidator.AUTHOR_PARAM, authorEntity2.getFullName())
        .param(BookFilterValidator.SIZE_PARAM, String.valueOf(10)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.content").value(Matchers.hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(bookEntity2.getId()))
        .andExpect(jsonPath("$.content[0].authors[0].id").value(authorEntity2.getId()))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  public void filter_book_with_filter_should_return_data_c2() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(BOOK_FILTER_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .param(BookFilterValidator.AUTHOR_PARAM, authorEntity1.getFullName().substring(5, 10))
        .param(BookFilterValidator.SIZE_PARAM, String.valueOf(10)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.content").value(Matchers.hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(bookEntity1.getId()))
        .andExpect(jsonPath("$.content[0].authors[0].id").value(authorEntity1.getId()))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

}