package com.siliconvalley.configServerMysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerMysqlApplication.class, args);
	}

}
