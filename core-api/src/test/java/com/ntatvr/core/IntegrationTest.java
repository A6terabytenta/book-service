package com.ntatvr.core;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = BookServiceApplication.class)
@ContextConfiguration(initializers = MongoInitializer.class)
@AutoConfigureMockMvc
public abstract class IntegrationTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected MongoTemplate mongoTemplate;

  protected String buildJsonPathMessage(final String fieldName) {
    return "$.errors[?(@.property=='" + fieldName + "')].message";
  }
}
