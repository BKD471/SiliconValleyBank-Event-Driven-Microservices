package com.siliconvalley.accountsservices.externalservice.service.input_validation_services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

@FeignClient(name="emailValidation",url = "https://emailvalidation.abstractapi.com/")
public interface IEmailValidationService {
    String API_KEY="30a9b81b45664b94bc390e513b287c23";
    @GetMapping("/v1")
    Map<String, Object> validateEmail(@RequestHeader(name = API_KEY) String api_key,
                                String email);
}
