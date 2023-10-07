package com.siliconvalley.accountsservices;

import com.siliconvalley.accountsservices.model.Role;
import com.siliconvalley.accountsservices.repository.IRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@SpringBootApplication
public class AccountsServicesApplication implements  CommandLineRunner{
	public static void main(String[] args) {
		SpringApplication.run(AccountsServicesApplication.class, args);
	}
	private static final String PATH_TO_PROPERTIES_FILE="accounts-services/src/main/java/com/siliconvalley/accountsservices/properties/app_properties/AccountsServiceApplication.properties";
	private static final Properties properties=new Properties();
	private static final String CLASS_NAME=AccountsServicesApplication.class.getSimpleName();
	private final String role_normal_id;
	private final String role_admin_id;
	@Autowired
	private IRoleRepository roleRepository;


	static {
		try{
			properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
		}catch (IOException e){
			log.error("Error while reading {}'s properties file {}",CLASS_NAME,e.getMessage());
		}
	}
	AccountsServicesApplication(){
		this.role_normal_id=properties.getProperty("normal.role.id");
		this.role_admin_id= properties.getProperty("admin.role.id");
	}

	/**
	 * @param args incoming main method arguments
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		try{
			Role role_admin=Role.builder().roleId(role_normal_id).roleName("ROLE_ADMIN").build();
			Role role_normal=Role.builder().roleId(role_admin_id).roleName("ROLE_NORMAL").build();
			roleRepository.save(role_admin);roleRepository.save(role_normal);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
