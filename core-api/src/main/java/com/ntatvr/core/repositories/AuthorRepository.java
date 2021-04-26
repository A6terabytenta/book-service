package com.ntatvr.core.repositories;

import org.springframework.stereotype.Repository;

import com.ntatvr.domain.entities.author.AuthorEntity;

@Repository
public interface AuthorRepository extends BaseRepository<AuthorEntity, String> {

}
