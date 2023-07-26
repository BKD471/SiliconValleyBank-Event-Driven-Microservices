package com.siliconvalley.accountsservices.controller.Impl;

import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.dto.tokenDtos.JwtRequest;
import com.siliconvalley.accountsservices.dto.tokenDtos.JwtResponse;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.helpers.JwtHelper;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import static com.siliconvalley.accountsservices.helpers.MapperHelper.mapToCustomer;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.mapToCustomerDto;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager manager;
    private final UserDetailsService userDetailsService;
    private final JwtHelper jwtHelper;
    private final ModelMapper modelMapper;
    private final String googleClientId;
    private final String newPassword;
    private final ICustomerRepository customerRepository;

    AuthController(AuthenticationManager manager,UserDetailsService userDetailsService,
                   JwtHelper jwtHelper,ModelMapper modelMapper,
                   ICustomerRepository customerRepository,@Value("${googleClientId}") String googleClientId,
                   @Value("${newPassword}") String newPassword){
        this.manager=manager;
        this.userDetailsService=userDetailsService;
        this.jwtHelper=jwtHelper;
        this.modelMapper=modelMapper;
        this.customerRepository=customerRepository;
        this.googleClientId=googleClientId;
        this.newPassword=newPassword;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody final JwtRequest jwtRequest) {
        this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        final String token = jwtHelper.generateToken(userDetails);
        final CustomerDto customerDto = modelMapper.map(userDetails, CustomerDto.class);
        final JwtResponse jwtResponse = JwtResponse.builder()
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
        final String methodName = "doAuthenticate(String,String) in AuthController";
        final UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(auth);
        } catch (BadCredentialsException e) {
            throw new BadApiRequestException(BadApiRequestException.class, "Invalid Credentials", methodName);
        }
    }

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException, IOException {


        //get the id token from request
        String idToken = data.get("idToken").toString();
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));
        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        log.info("Payload : {}", payload);
        String email = payload.getEmail();
        Customer customer = null;
        customer = customerRepository.findCustomerByEmail(email).orElse(null);

        if (Objects.isNull(customer)) {
            //create new user
            customer = this.saveUser(email, data.get("name").toString(), data.get("photoUrl").toString());
        }
        ResponseEntity<JwtResponse> jwtResponseResponseEntity
                = this.login(JwtRequest.builder().email(customer.getEmail()).password(newPassword).build());
        return jwtResponseResponseEntity;
    }

    private Customer saveUser(String email, String name, String photoUrl) {
        CustomerDto newUser = CustomerDto.builder()
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
