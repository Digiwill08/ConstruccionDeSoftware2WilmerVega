package gestiondeunbanco.wilmervega;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WilmerVegaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WilmerVegaApplication.class, args);
	}

}
