package ar.edu.utn.frba.ddsi.climalert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ClimalertApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClimalertApplication.class, args);
		log.info("====================================");
		log.info("  Climalert iniciado correctamente  ");
		log.info("====================================");
	}

}
