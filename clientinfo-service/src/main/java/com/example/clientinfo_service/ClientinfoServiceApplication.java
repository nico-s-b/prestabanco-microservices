package com.example.clientinfo_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ClientinfoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientinfoServiceApplication.class, args);
	}

}
