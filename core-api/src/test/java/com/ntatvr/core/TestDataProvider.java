package com.ntatvr.core;

import java.util.List;

import com.ntatvr.domain.entities.author.AuthorEntity;
import com.ntatvr.domain.entities.book.AssignedAuthor;
import com.ntatvr.domain.entities.book.BookEntity;

public class TestDataProvider {

  public static AuthorEntity buildAuthorEntity() {
    final AuthorEntity authorEntity = AuthorEntity.builder()
        .firstName("Anh")
        .lastName("Nguyen Tuan")
        .email("ntatvr@gmail.com")
        .address("Ho Chi Minh city")
        .build();
    return authorEntity;
  }

  public static AuthorEntity buildNewAuthorEntity() {
    final AuthorEntity authorEntity = AuthorEntity.builder()
        .firstName("Tuan")
        .lastName("Nguyen Van")
        .email("abc-it@gmail.com")
        .address("Ha Noi city")
        .build();
    return authorEntity;
  }

  public static BookEntity buildBookEntity(final AuthorEntity author) {
    return BookEntity.builder()
        .title("Clean Code")
        .subTitle("Clean Code: A Handbook of Agile Software")
        .description("Even bad code can function. But if code isn’t clean, " +
            "it can bring a development organization to its knees.")
        .thumbnailImage("/images/thumbnailImage.png")
        .backgroundImage("/images/backgroundImage.png")
        .authors(List.of(AssignedAuthor.builder().id(author.getId()).fullName(author.getFullName()).build()))
        .build();
  }

  public static BookEntity buildNewBookEntity(final AuthorEntity author) {
    return BookEntity.builder()
        .title("Clean Code II")
        .subTitle("Clean Code II: A Handbook of Agile Software")
        .description("Even bad code can function. But if code isn’t clean, " +
            "it can bring a development organization to its knees.")
        .thumbnailImage("/images/thumbnailImage-II.png")
        .backgroundImage("/images/backgroundImage-II.png")
        .authors(List.of(AssignedAuthor.builder().id(author.getId()).fullName(author.getFullName()).build()))
        .build();
  }
}
