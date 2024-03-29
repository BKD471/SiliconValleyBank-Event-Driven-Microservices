package com.siliconvalley.accountsservices;

import com.siliconvalley.accountsservices.helpers.CodeRetrieverHelper;
import com.siliconvalley.accountsservices.repository.IRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@SpringBootApplication
@EnableWebMvc
@EnableFeignClients
public class AccountsServicesApplication{
	public static void main(String[] args) {
		SpringApplication.run(AccountsServicesApplication.class, args);
	}
	private final String role_normal_id;
	private final String role_admin_id;
	private final IRoleRepository roleRepository;
	private final CodeRetrieverHelper codeRetrieverHelper;


	AccountsServicesApplication(@Value("${path.project.properties}") String path_to_accounts_main_properties,IRoleRepository roleRepository, CodeRetrieverHelper codeRetrieverHelper){
		this.roleRepository=roleRepository;
		this.codeRetrieverHelper = codeRetrieverHelper;
		Properties properties = new Properties();
		try{
			properties.load(new FileInputStream(path_to_accounts_main_properties));
		}catch (IOException e){
			log.error("Error while reading {}'s properties file {}",AccountsServicesApplication.class.getSimpleName(),e.getMessage());
		}
		this.role_normal_id= properties.getProperty("normal.role.id");
		this.role_admin_id= properties.getProperty("admin.role.id");
	}
}
