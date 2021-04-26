package com.ntatvr.core.controllers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.ntatvr.core.IntegrationTest;
import com.ntatvr.core.TestDataProvider;
import com.ntatvr.core.exceptions.ErrorEnum;
import com.ntatvr.core.repositories.AuthorRepository;
import com.ntatvr.core.repositories.BookRepository;
import com.ntatvr.domain.entities.author.AuthorEntity;
import com.ntatvr.domain.entities.book.BookEntity;

public class BookControllerTest extends IntegrationTest {

  private static final String BOOKS_ENDPOINT = BookController.BOOKS_ENDPOINT;
  private static final String NOT_EXIST_ID = "6084331d392464040b27ade5";

  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private BookRepository bookRepository;

  private AuthorEntity authorEntity;
  private BookEntity bookEntity;

  @Before
  public void before() {
    authorEntity = authorRepository.save(TestDataProvider.buildAuthorEntity());
    bookEntity = bookRepository.save(TestDataProvider.buildBookEntity(authorEntity));
  }

  @After
  public void after() {
    mongoTemplate.dropCollection(BookEntity.COLLECTION_NAME);
    mongoTemplate.dropCollection(AuthorEntity.COLLECTION_NAME);
  }

  @Test
  public void get_Book_with_wrong_id_should_return_not_found() throws Exception {

    this.mockMvc.perform(get(BOOKS_ENDPOINT + "/" + NOT_EXIST_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.userMessage").value(ErrorEnum.ENTITY_NOT_FOUND_EXCEPTION.getUserMessage()));
  }

  @Test
  public void get_deleted_Book_should_return_not_found() throws Exception {
    bookRepository.deleteById(bookEntity.getId());
    this.mockMvc.perform(get(BOOKS_ENDPOINT + "/" + bookEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.userMessage").value(ErrorEnum.ENTITY_NOT_FOUND_EXCEPTION.getUserMessage()));
  }

  @Test
  public void get_Book_with_correct_id_should_return_the_entity() throws Exception {

    this.mockMvc.perform(get(BOOKS_ENDPOINT + "/" + bookEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookEntity.getId()))
        .andExpect(jsonPath("$.title").value(bookEntity.getTitle()))
        .andExpect(jsonPath("$.subTitle").value(bookEntity.getSubTitle()))
        .andExpect(jsonPath("$.description").value(bookEntity.getDescription()))
        .andExpect(jsonPath("$.thumbnailImage").value(bookEntity.getThumbnailImage()))
        .andExpect(jsonPath("$.backgroundImage").value(bookEntity.getBackgroundImage()))
        .andExpect(jsonPath("$.isDeleted").value(false))
        .andExpect(jsonPath("$.authors[0].id").value(authorEntity.getId()));
  }

  @Test
  public void create_a_Book_with_incorrect_data_should_return_bad_request() throws Exception {

    this.mockMvc.perform(post(BOOKS_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new BookEntity())))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath(buildJsonPathMessage("authors")).value("must not be empty"))
        .andExpect(jsonPath(buildJsonPathMessage("backgroundImage")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("description")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("subTitle")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("thumbnailImage")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("title")).value("must not be blank"));
  }

  @Test
  public void create_a_Book_with_correct_data_should_successful() throws Exception {
    final BookEntity bookEntity = TestDataProvider.buildBookEntity(authorEntity);
    bookEntity.setTitle("Clean Code II");
    this.mockMvc.perform(post(BOOKS_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookEntity)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.title").value(bookEntity.getTitle()))
        .andExpect(jsonPath("$.subTitle").value(bookEntity.getSubTitle()))
        .andExpect(jsonPath("$.description").value(bookEntity.getDescription()))
        .andExpect(jsonPath("$.thumbnailImage").value(bookEntity.getThumbnailImage()))
        .andExpect(jsonPath("$.backgroundImage").value(bookEntity.getBackgroundImage()))
        .andExpect(jsonPath("$.isDeleted").value(false))
        .andExpect(jsonPath("$.authors[0].id").value(authorEntity.getId()));
  }

  @Test
  public void update_a_Book_with_incorrect_data_should_return_bad_request() throws Exception {

    this.mockMvc.perform(put(BOOKS_ENDPOINT + "/" + bookEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new BookEntity())))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath(buildJsonPathMessage("authors")).value("must not be empty"))
        .andExpect(jsonPath(buildJsonPathMessage("backgroundImage")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("description")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("subTitle")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("thumbnailImage")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("title")).value("must not be blank"));
  }

  @Test
  public void update_a_Book_with_correct_data_should_successful() throws Exception {
    final BookEntity newBookEntity = TestDataProvider.buildNewBookEntity(authorEntity);
    this.mockMvc.perform(put(BOOKS_ENDPOINT + "/" + bookEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(newBookEntity)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.title").value(newBookEntity.getTitle()))
        .andExpect(jsonPath("$.subTitle").value(newBookEntity.getSubTitle()))
        .andExpect(jsonPath("$.description").value(newBookEntity.getDescription()))
        .andExpect(jsonPath("$.thumbnailImage").value(newBookEntity.getThumbnailImage()))
        .andExpect(jsonPath("$.backgroundImage").value(newBookEntity.getBackgroundImage()))
        .andExpect(jsonPath("$.isDeleted").value(false))
        .andExpect(jsonPath("$.authors[0].id").value(authorEntity.getId()));
  }

  @Test
  public void delete_Book_with_wrong_id_should_return_not_found() throws Exception {

    this.mockMvc.perform(delete(BOOKS_ENDPOINT + "/" + NOT_EXIST_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.userMessage").value(ErrorEnum.ENTITY_NOT_FOUND_EXCEPTION.getUserMessage()));
  }

  @Test
  public void delete_Book_with_correct_id_should_successful() throws Exception {

    this.mockMvc.perform(delete(BOOKS_ENDPOINT + "/" + bookEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }
}