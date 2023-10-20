package com.siliconvalley.accountsservices.controller.Impl;

import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.dto.tokenDtos.JwtRequest;
import com.siliconvalley.accountsservices.dto.tokenDtos.JwtResponse;
import com.siliconvalley.accountsservices.helpers.JwtHelper;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import static com.siliconvalley.accountsservices.helpers.MapperHelper.mapToCustomer;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.mapToCustomerDto;
import static java.util.Objects.isNull;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "AuthController",description = "Api for Authentication")
public class AuthController {
    private final AuthenticationManager manager;
    private final UserDetailsService userDetailsService;
    private final ICustomerRepository customerRepository;
    private final JwtHelper jwtHelper;
    private final ModelMapper modelMapper;
    private final String googleClientId;
    private final String newPassword;

    public AuthController(AuthenticationManager manager,UserDetailsService userDetailsService,
                   JwtHelper jwtHelper,ModelMapper modelMapper,
                   ICustomerRepository customerRepository, @Value("${path.controller.auth}") String path_auth_controller_properties){

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path_auth_controller_properties));
        } catch (IOException e) {
            log.error("Error while reading {}'s properties file {}",this.getClass().getSimpleName(),e.getMessage());
        }
        this.manager=manager;
        this.userDetailsService=userDetailsService;
        this.jwtHelper=jwtHelper;
        this.modelMapper=modelMapper;
        this.customerRepository=customerRepository;
        this.googleClientId= properties.getProperty("googleClientId");;
        this.newPassword= properties.getProperty("newPassword");
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody final JwtRequest jwtRequest) {
        this.doAuthenticate( jwtRequest.email(), jwtRequest.password());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.email());
        final String token = jwtHelper.generateToken(userDetails);

        final CustomerDto customerDto = new CustomerDto.Builder()
                .customerName(userDetails.getUsername())
                .password(userDetails.getPassword())
                .build();
        final JwtResponse jwtResponse =new JwtResponse.Builder()
                .jwtToken(token)
                .customer(customerDto).build();

        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    @GetMapping("/current")
    public ResponseEntity<CustomerDto> getCurrentUser(final Principal principal) {
        final String userName = principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(userName)
                , CustomerDto.class)
                , HttpStatus.OK);
    }

    private void doAuthenticate(final String email,final String password) {
        final UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(email, password);
        manager.authenticate(auth);
    }

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException {
        //get the id token from request
        String idToken = data.get("idToken").toString();
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));
        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        log.info("Payload : {}", payload);
        String email = payload.getEmail();
        Customer customer = customerRepository.findCustomerByEmail(email).orElse(null);

        if (isNull(customer)) customer = this.saveUser(email, data.get("name").toString(), data.get("photoUrl").toString());
        return this.login(new JwtRequest.Builder().email(customer.getEmail()).password(newPassword).build());
    }

    private Customer saveUser(String email, String name, String photoUrl) {
        CustomerDto newUser =new CustomerDto.Builder()
                .customerName(name)
                .email(email)
                .password(newPassword)
                .imageName(photoUrl)
                .roles(new HashSet<>())
                .build();

        CustomerDto customer =createUser(newUser);
        return this.modelMapper.map(customer, Customer.class);
    }

    private  CustomerDto createUser(CustomerDto customerDto){
        Customer saveCustomer= mapToCustomer(customerDto);
        return mapToCustomerDto(customerRepository.save(saveCustomer));
    }
}
