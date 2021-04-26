package com.ntatvr.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class BookServiceApplication {

  public static void main(final String[] args) {
    SpringApplication.run(BookServiceApplication.class, args);
  }

}
