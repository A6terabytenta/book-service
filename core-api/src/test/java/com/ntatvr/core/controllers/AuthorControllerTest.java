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
import com.ntatvr.domain.entities.author.AuthorEntity;
import com.ntatvr.domain.entities.book.BookEntity;

public class AuthorControllerTest extends IntegrationTest {

  private static final String AUTHORS_ENDPOINT = AuthorController.AUTHORS_ENDPOINT;
  private static final String NOT_EXIST_ID = "6084331d392464040b27ade5";

  @Autowired
  private AuthorRepository authorRepository;

  private AuthorEntity authorEntity;

  @Before
  public void before() {
    authorEntity = authorRepository.save(TestDataProvider.buildAuthorEntity());
  }

  @After
  public void after() {
    mongoTemplate.dropCollection(AuthorEntity.COLLECTION_NAME);
  }

  @Test
  public void get_Author_with_wrong_id_should_return_not_found() throws Exception {

    this.mockMvc.perform(get(AUTHORS_ENDPOINT + "/" + NOT_EXIST_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.userMessage").value(ErrorEnum.ENTITY_NOT_FOUND_EXCEPTION.getUserMessage()));
  }

  @Test
  public void get_deleted_Author_should_return_not_found() throws Exception {
    authorRepository.deleteById(authorEntity.getId());
    this.mockMvc.perform(get(AUTHORS_ENDPOINT + "/" + authorEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.userMessage").value(ErrorEnum.ENTITY_NOT_FOUND_EXCEPTION.getUserMessage()));
  }

  @Test
  public void get_Author_with_correct_id_should_return_the_entity() throws Exception {

    this.mockMvc.perform(get(AUTHORS_ENDPOINT + "/" + authorEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(authorEntity.getId()))
        .andExpect(jsonPath("$.firstName").value(authorEntity.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(authorEntity.getLastName()))
        .andExpect(jsonPath("$.fullName").value(authorEntity.getFullName()))
        .andExpect(jsonPath("$.address").value(authorEntity.getAddress()))
        .andExpect(jsonPath("$.email").value(authorEntity.getEmail()))
        .andExpect(jsonPath("$.isDeleted").value(false));
  }

  @Test
  public void create_a_Author_with_incorrect_data_should_return_bad_request() throws Exception {

    this.mockMvc.perform(post(AUTHORS_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new BookEntity())))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath(buildJsonPathMessage("firstName")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("lastName")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("address")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("email")).value("must not be blank"));
  }

  @Test
  public void create_a_Author_with_correct_data_should_successful() throws Exception {
    final AuthorEntity authorEntity = TestDataProvider.buildAuthorEntity();
    this.mockMvc.perform(post(AUTHORS_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(authorEntity)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.firstName").value(authorEntity.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(authorEntity.getLastName()))
        .andExpect(jsonPath("$.fullName").value(authorEntity.getFullName()))
        .andExpect(jsonPath("$.address").value(authorEntity.getAddress()))
        .andExpect(jsonPath("$.email").value(authorEntity.getEmail()))
        .andExpect(jsonPath("$.isDeleted").value(false));
  }

  @Test
  public void update_a_Author_with_incorrect_data_should_return_bad_request() throws Exception {

    this.mockMvc.perform(put(AUTHORS_ENDPOINT + "/" + authorEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new AuthorEntity())))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath(buildJsonPathMessage("firstName")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("lastName")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("address")).value("must not be blank"))
        .andExpect(jsonPath(buildJsonPathMessage("email")).value("must not be blank"));
  }

  @Test
  public void update_a_Author_with_correct_data_should_successful() throws Exception {
    final AuthorEntity newAuthorEntity = TestDataProvider.buildNewAuthorEntity();
    this.mockMvc.perform(put(AUTHORS_ENDPOINT + "/" + authorEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(newAuthorEntity)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.firstName").value(newAuthorEntity.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(newAuthorEntity.getLastName()))
        .andExpect(jsonPath("$.fullName").value(newAuthorEntity.getFullName()))
        .andExpect(jsonPath("$.address").value(newAuthorEntity.getAddress()))
        .andExpect(jsonPath("$.email").value(newAuthorEntity.getEmail()))
        .andExpect(jsonPath("$.isDeleted").value(false));
  }

  @Test
  public void delete_Author_with_wrong_id_should_return_not_found() throws Exception {

    this.mockMvc.perform(delete(AUTHORS_ENDPOINT + "/" + NOT_EXIST_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.userMessage").value(ErrorEnum.ENTITY_NOT_FOUND_EXCEPTION.getUserMessage()));
  }

  @Test
  public void delete_Author_with_correct_id_should_successful() throws Exception {

    this.mockMvc.perform(delete(AUTHORS_ENDPOINT + "/" + authorEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }
}