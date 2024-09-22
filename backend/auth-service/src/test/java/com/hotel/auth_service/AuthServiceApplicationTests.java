package com.hotel.auth_service;

import com.hotel.auth_service.config.RsaKeyConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@SpringBootTest
class AuthServiceApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		assertNotNull(context);
	}

	@Test
	void testRsaKeyConfigPropertiesLoaded() {
		assertNotNull(context.getBean(RsaKeyConfigProperties.class),
				"RsaKeyConfigProperties bean should be loaded in the context");
	}

	@Test
	void testMain() {
		assertDoesNotThrow(() -> AuthServiceApplication.main(new String[] {}), "Application failed to start");
	}
}
