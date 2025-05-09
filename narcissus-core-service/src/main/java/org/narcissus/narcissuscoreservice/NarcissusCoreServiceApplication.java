package org.narcissus.narcissuscoreservice;

import org.narcissus.narcissuscoreservice.config.AppConfig;
import org.narcissus.narcissuscoreservice.config.SpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableConfigurationProperties({AppConfig.class, SpringConfig.class})
@SpringBootApplication
@EnableMongoRepositories
public class NarcissusCoreServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(NarcissusCoreServiceApplication.class, args);
	}
}
