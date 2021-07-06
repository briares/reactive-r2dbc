package com.briares.r2dbc.h2.h2r2dbcDemo;

import com.briares.r2dbc.h2.h2r2dbcDemo.model.UserEntity;
import com.briares.r2dbc.h2.h2r2dbcDemo.repository.UserRepository;
import io.r2dbc.spi.ConnectionFactory;
import java.time.Duration;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@EnableR2dbcRepositories
@SpringBootApplication
@Slf4j
public class H2r2dbcDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(H2r2dbcDemoApplication.class, args);
	}

  @Bean
  public ConnectionFactoryInitializer connectionFactoryInitializer(ConnectionFactory connectionFactory) {
    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);
    initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
    return initializer;
  }

  @Bean
  public CommandLineRunner runner(UserRepository repository) {

    return (args) -> {
      // save a few customers
      repository.saveAll(Arrays.asList(new UserEntity("aaa@aaa.com", "Bauer"),
          new UserEntity("bbb@bbb.com", "O'Brian"),
          new UserEntity("ccc@ccc.com", "Bauer"),
          new UserEntity("ddd@ddd.com", "Palmer"),
          new UserEntity("eee@eee.com", "Dessler")))
          .blockLast(Duration.ofSeconds(10));

      // fetch all customers
      log.info("Customers found with findAll():");
      log.info("-------------------------------");
      repository.findAll().doOnNext(customer -> {
        log.info(customer.toString());
      }).blockLast(Duration.ofSeconds(10));

      log.info("");

      // fetch an individual customer by ID
      repository.findById(1L).doOnNext(customer -> {
        log.info("Customer found with findById(1L):");
        log.info("--------------------------------");
        log.info(customer.toString());
        log.info("");
      }).block(Duration.ofSeconds(10));


      // fetch customers by last name
      log.info("Customer found with findByLastName('Bauer'):");
      log.info("--------------------------------------------");
      repository.findByName("Bauer").doOnNext(bauer -> {
        log.info(bauer.toString());
      }).blockLast(Duration.ofSeconds(10));;
      log.info("");
    };
  }
}
