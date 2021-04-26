package com.ntatvr.core.controllers.converter;


import org.mapstruct.Mapper;

import com.ntatvr.core.api.model.BookEntityRequest;
import com.ntatvr.core.api.model.BookEntityResponse;
import com.ntatvr.domain.entities.book.BookEntity;

@Mapper(componentModel = "spring")
public interface BookMapper {

  BookEntity toEntity(BookEntityRequest request);

  BookEntityResponse toResponse(BookEntity entity);
}
