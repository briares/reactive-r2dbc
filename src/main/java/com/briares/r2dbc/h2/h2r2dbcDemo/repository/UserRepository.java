package com.briares.r2dbc.h2.h2r2dbcDemo.repository;

import com.briares.r2dbc.h2.h2r2dbcDemo.model.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, Long> {
  Flux<UserEntity> findByEmail(String email);

  @Query("SELECT * FROM USER_ENTITY WHERE name = :name")
  Flux<UserEntity> findByName(String name);
}