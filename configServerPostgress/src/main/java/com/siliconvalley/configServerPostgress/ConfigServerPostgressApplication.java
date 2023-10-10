package com.siliconvalley.configServerPostgress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerPostgressApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerPostgressApplication.class, args);
	}

}
