package gestiondeunbanco.wilmervega;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableScheduling
@EnableJpaRepositories(basePackages = "gestiondeunbanco.wilmervega.application.adapters.persistence.sql.repositories")
@EnableMongoRepositories(basePackages = "gestiondeunbanco.wilmervega.application.adapters.persistence.mongo")
public class WilmerVegaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WilmerVegaApplication.class, args);
    }
}
