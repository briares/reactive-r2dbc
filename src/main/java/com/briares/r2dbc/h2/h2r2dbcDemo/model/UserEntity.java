package com.briares.r2dbc.h2.h2r2dbcDemo.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Table(name = "USER_ENTITY")
public class UserEntity {
  public UserEntity(String email, String name) {
    this.email = email;
    this.name = name;
  }
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  private String email;
  private String name;
}