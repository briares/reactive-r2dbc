package com.briares.r2dbc.h2.h2r2dbcDemo.configuration;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.briares.r2dbc.h2.h2r2dbcDemo.model.UserEntity;
import com.briares.r2dbc.h2.h2r2dbcDemo.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@AllArgsConstructor
public class ApplicationRouter {
  private final UserRepository userRepository;

  @Bean
  RouterFunction<ServerResponse> getUserByIdRoute() {
    return route(GET("/user/{id}"),
        req -> ok().body(
            userRepository.findById(Long.parseLong(req.pathVariable("id"))), UserEntity.class));
  }

  @Bean
  RouterFunction<ServerResponse> getAllUsersRoute() {
    return route(GET("/users"),
        req -> ok().body(
            userRepository.findAll(), UserEntity.class));
  }

  @Bean
  RouterFunction<ServerResponse> updateUserRoute() {
    return route(POST("/users/update"),
        req -> req.body(toMono(UserEntity.class))
            .doOnNext(userRepository::save)
            .then(ok().build()));
  }


  @Bean
  RouterFunction<ServerResponse> composedRoutes() {
    return
        route(GET("/composed/users"),
            req -> ok().body(
                userRepository.findAll(), UserEntity.class))

            .and(route(GET("/composed/user/{id}"),
                req -> ok().body(
                    userRepository.findById(Long.parseLong(req.pathVariable("id"))), UserEntity.class)))

            .and(route(POST("/composed/users/update"),
                req -> req.body(toMono(UserEntity.class))
                    .doOnNext(userRepository::save)
                    .then(ok().build())));
  }
}
