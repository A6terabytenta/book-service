package com.ntatvr.domain.entities;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

  public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String LAST_MODIFIED_FIELD = "lastModified";
  @Id
  private String id;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long version = 0L;

  @CreatedDate
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = DATE_TIME_PATTERN)
  private Date created;

  @CreatedBy
  private String createdBy;

  @LastModifiedDate
  @JsonFormat(pattern = DATE_TIME_PATTERN)
  private Date lastModified;

  @LastModifiedBy
  private String lastModifiedBy;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Boolean isDeleted = Boolean.FALSE;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String entityType;

  public void setLastModified() {
    this.setLastModified(new Date());
  }
}
