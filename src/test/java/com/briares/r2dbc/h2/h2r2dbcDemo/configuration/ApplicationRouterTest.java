package com.briares.r2dbc.h2.h2r2dbcDemo.configuration;


import com.briares.r2dbc.h2.h2r2dbcDemo.model.UserEntity;
import com.briares.r2dbc.h2.h2r2dbcDemo.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationRouterTest {
  @Autowired
  private ApplicationRouter router;

  @MockBean
  private UserRepository userRepository;

  @Test
  public void givenUserId_whenGetUserById_thenCorrectUser() {
    WebTestClient client = WebTestClient
        .bindToRouterFunction(router.getUserByIdRoute())
        .build();

    UserEntity userEntity = new UserEntity("zzz@zzz.com", "User 1");

    given(userRepository.findById(1L)).willReturn(Mono.just(userEntity));

    client.get()
        .uri("/user/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(UserEntity.class)
        .isEqualTo(userEntity);
  }

  @Test
  public void whenGetAllEmployees_thenCorrectEmployees() {
    WebTestClient client = WebTestClient
        .bindToRouterFunction(router.getAllUsersRoute())
        .build();

    List<UserEntity> employees = Arrays.asList(
        new UserEntity("1", "Employee 1"),
        new UserEntity("2", "Employee 2"));

    Flux<UserEntity> userFlux = Flux.fromIterable(employees);
    given(userRepository.findAll()).willReturn(userFlux);

    client.get()
        .uri("/users")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(UserEntity.class)
        .isEqualTo(employees);
  }

  @Test
  public void whenUpdateEmployee_thenEmployeeUpdated() {
    WebTestClient client = WebTestClient
        .bindToRouterFunction(router.updateUserRoute())
        .build();

    UserEntity userEntity = new UserEntity("1", "User 1 Updated");

    client.post()
        .uri("/users/update")
        .body(Mono.just(userEntity), UserEntity.class)
        .exchange()
        .expectStatus()
        .isOk();

    verify(userRepository).save(userEntity);
  }

}
