package com.sgiem.ms.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableKafka
//@EnableDiscoveryClient
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

}
