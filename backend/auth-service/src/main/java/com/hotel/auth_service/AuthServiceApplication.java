package com.hotel.auth_service;

import com.hotel.auth_service.config.RsaKeyConfigProperties;
import com.hotel.auth_service.entity.Role;
import com.hotel.auth_service.entity.User;
import com.hotel.auth_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableConfigurationProperties(RsaKeyConfigProperties.class)
@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeUser(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			User user = new User();
			user.setEmail("admin@example.com");
			user.setPassword(passwordEncoder.encode("admin"));
			user.setFullName("Admin");
			user.setRole(Role.ADMIN);

			userRepository.save(user);
		};
	}
}
