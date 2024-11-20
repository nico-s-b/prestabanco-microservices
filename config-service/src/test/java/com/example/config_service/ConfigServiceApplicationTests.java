package com.example.config_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.config.import=optional:configserver:",
		"spring.cloud.config.enabled=false"
})
class ConfigServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
