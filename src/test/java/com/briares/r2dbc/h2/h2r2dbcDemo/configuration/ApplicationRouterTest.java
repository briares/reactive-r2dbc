package com.briares.r2dbc.h2.h2r2dbcDemo.configuration;

import com.briares.r2dbc.h2.h2r2dbcDemo.configuration.ApplicationRouter;

import com.briares.r2dbc.h2.h2r2dbcDemo.model.UserEntity;
import com.briares.r2dbc.h2.h2r2dbcDemo.repository.UserRepository;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
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
        .bindToRouterFunction(router.getEmployeeByIdRoute())
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
}
