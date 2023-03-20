package com.example.accountsservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;

@SpringBootApplication
public class AccountsServicesApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountsServicesApplication.class, args);
	}
}
