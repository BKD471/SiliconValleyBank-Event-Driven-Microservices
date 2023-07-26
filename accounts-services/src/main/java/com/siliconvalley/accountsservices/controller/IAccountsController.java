package com.siliconvalley.accountsservices.controller;

import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.dto.responseDtos.ImageResponseMessages;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.CustomerException;
import com.siliconvalley.accountsservices.exception.ResponseException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequestMapping("/api/v1/accounts")
public interface IAccountsController {
    @GetMapping("/get")
    ResponseEntity<OutputDto> getRequestForChange(@Valid @RequestBody final GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException, IOException;
    @GetMapping("/serve/image/{customerId}")
    void serveUserImage(@PathVariable final String customerId,final HttpServletResponse response) throws IOException;
    @PostMapping("/post")
    ResponseEntity<OutputDto> postRequestForChange(@Valid @RequestBody final PostInputRequestDto postInputDto) throws AccountsException, ResponseException, CustomerException, IOException;
    @PostMapping("/create")
    ResponseEntity<OutputDto> createAccount(@Valid @RequestBody final PostInputRequestDto postInputDto) throws AccountsException, ResponseException, CustomerException, IOException;
    @PutMapping("/put")
    ResponseEntity<OutputDto> putRequestForChange(@Valid @RequestBody final PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException, IOException;
    @PutMapping("/upload/image/{customerId}")
    ResponseEntity<ImageResponseMessages> uploadCustomerImage(@RequestParam("customerImage")final MultipartFile image, @PathVariable final String customerId) throws IOException;
    @DeleteMapping("/delete")
    ResponseEntity<OutputDto> deleteRequestForChange(@Valid @RequestBody final DeleteInputRequestDto deleteInputRequestDto) throws AccountsException, ResponseException, CustomerException;
    @DeleteMapping("/delete/customer")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<OutputDto> deleteCustomer(@Valid @RequestBody final DeleteInputRequestDto deleteInputRequestDto);
}
