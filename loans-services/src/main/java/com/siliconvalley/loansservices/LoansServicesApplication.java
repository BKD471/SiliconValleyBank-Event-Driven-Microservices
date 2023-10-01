package com.siliconvalley.loansservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class LoansServicesApplication {
	public static void main(String[] args) {
		SpringApplication.run(LoansServicesApplication.class, args);
	}
}
