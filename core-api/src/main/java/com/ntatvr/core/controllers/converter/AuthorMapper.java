package com.ntatvr.core.controllers.converter;


import org.mapstruct.Mapper;

import com.ntatvr.core.api.model.AuthorEntityRequest;
import com.ntatvr.core.api.model.AuthorEntityResponse;
import com.ntatvr.domain.entities.author.AuthorEntity;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

  AuthorEntity toEntity(AuthorEntityRequest request);

  AuthorEntityResponse toResponse(AuthorEntity entity);
}
