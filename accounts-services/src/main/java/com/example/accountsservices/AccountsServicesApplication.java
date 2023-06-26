package com.example.accountsservices;

import com.example.accountsservices.model.Role;
import com.example.accountsservices.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountsServicesApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(AccountsServicesApplication.class, args);
	}

	@Value("${normal.role.id}")
    private String role_normal_id;

	@Value("${admin.role.id}")
	private String role_admin_id;

	@Autowired
	private RoleRepository roleRepository;


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
