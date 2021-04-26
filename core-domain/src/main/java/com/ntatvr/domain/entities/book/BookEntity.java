package com.ntatvr.domain.entities.book;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ntatvr.domain.entities.BaseEntity;

@Document(collection = BookEntity.COLLECTION_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity extends BaseEntity {

  public static final String COLLECTION_NAME = "books";
  public static final String ENTITY_TYPE = "book";
  public static final String AUTHORS_FULL_NAME = "authors.fullName";

  @NotBlank
  @Size(max = 100)
  private String title;

  @NotBlank
  @Size(max = 100)
  private String subTitle;

  @NotBlank
  @Size(max = 1000)
  private String description;

  @NotBlank
  private String thumbnailImage;

  @NotBlank
  private String backgroundImage;

  @NotEmpty
  @UniqueElements
  private List<@Valid AssignedAuthor> authors;

  @Override
  public String getEntityType() {
    return ENTITY_TYPE;
  }
}
