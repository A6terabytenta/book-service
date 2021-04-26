package com.ntatvr.domain.entities.author;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ntatvr.domain.entities.BaseEntity;

@Document(collection = AuthorEntity.COLLECTION_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorEntity extends BaseEntity {

  public static final String COLLECTION_NAME = "authors";
  public static final String ENTITY_TYPE = "book";

  @NotBlank
  @Size(max = 50)
  private String firstName;

  @NotBlank
  @Size(max = 50)
  private String lastName;

  private String fullName;

  @NotBlank
  @Size(max = 1000)
  private String address;

  @NotBlank
  @Size(max = 100)
  private String email;

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }

  @Override
  public String getEntityType() {
    return ENTITY_TYPE;
  }
}
