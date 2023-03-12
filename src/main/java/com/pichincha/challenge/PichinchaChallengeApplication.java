package com.pichincha.challenge;


import javax.sql.DataSource;
//import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PichinchaChallengeApplication {

  public static void main(String[] args) {
    SpringApplication.run(PichinchaChallengeApplication.class, args);
  }

  /*@Bean
  public Flyway flyway(DataSource dataSource) {
    Flyway flyway = Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:/db/migration")
        .load();
    flyway.migrate();
    return flyway;
  }*/
}