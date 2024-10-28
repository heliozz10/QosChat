package com.heliozz10.qoschat;

import com.heliozz10.qoschat.entity.Authority;
import com.heliozz10.qoschat.repository.AuthorityRepository;
import com.heliozz10.qoschat.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QoschatApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(QoschatApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(QoschatApplication.class, args);
	}

	@Bean
	public CommandLineRunner setup(UserRepository userRepo, AuthorityRepository authorityRepo) {
		return args -> {
			authorityRepo.save(new Authority("USER"));
			authorityRepo.findAll().forEach(authority -> LOGGER.info(authority.toString()));
		};
	}
}
