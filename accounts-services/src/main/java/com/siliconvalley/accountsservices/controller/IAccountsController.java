package com.siliconvalley.accountsservices.controller;

import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.ExternalServiceRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.responseDtos.ImageResponseMessages;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.CustomerException;
import com.siliconvalley.accountsservices.exception.ResponseException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequestMapping("/api/acnt")
public interface IAccountsController {
    @GetMapping("/accounts/v1/get")
    ResponseEntity<OutputDto> getRequestForChange(@Valid @RequestBody final GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException, IOException;
    @GetMapping("/accounts/v1/serve/image/{customerId}")
    void serveUserImage(@PathVariable final String customerId,final HttpServletResponse response) throws IOException;
    @PostMapping("/accounts/v1/post")
    ResponseEntity<OutputDto> postRequestForChange(@Valid @RequestBody final PostInputRequestDto postInputDto) throws AccountsException, ResponseException, CustomerException, IOException;
    @PostMapping("/accounts/v1/external")
    ResponseEntity<OutputDto> externalRequestForChange(@RequestBody final ExternalServiceRequestDto externalServiceRequestDto) throws AccountsException, ResponseException, CustomerException, IOException;
    @PostMapping("/accounts/v1/create")
    ResponseEntity<OutputDto> createAccount(@Valid @RequestBody final PostInputRequestDto postInputDto) throws AccountsException, ResponseException, CustomerException, IOException;
    @PutMapping("/accounts/v1/put")
    ResponseEntity<OutputDto> putRequestForChange(@Valid @RequestBody final PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException, IOException;
    @PutMapping("/accounts/v1/upload/image/{customerId}")
    ResponseEntity<ImageResponseMessages> uploadCustomerImage(@RequestParam("customerImage")final MultipartFile image, @PathVariable final String customerId) throws IOException;
    @DeleteMapping("/accounts/v1/delete")
    ResponseEntity<OutputDto> deleteRequestForChange(@Valid @RequestBody final DeleteInputRequestDto deleteInputRequestDto) throws AccountsException, ResponseException, CustomerException;
    @DeleteMapping("/accounts/v1/delete/customer")
    //@PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<OutputDto> deleteCustomer(@Valid @RequestBody final DeleteInputRequestDto deleteInputRequestDto);
}
