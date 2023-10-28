package com.siliconvalley.accountsservices.externalservice.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siliconvalley.accountsservices.externalservice.exception.ServiceDownException;
import com.siliconvalley.accountsservices.externalservice.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    /**
     * @param methodKey {@link feign.Feign#configKey} of the java method that invoked the request. ex.
     *                  {@code IAM#getUser()}
     * @param response  HTTP response where {@link Response#status() status} is greater than or equal
     *                  to {@code 300}.
     * @return
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("::{}", response.request().url());
        log.info("::{}", response.request().body());

        try {
            ErrorResponse errorResponse=objectMapper.
                    readValue(response.body().asInputStream(),
                            ErrorResponse.class);
            return new ServiceDownException(errorResponse.getErrorMessage(), errorResponse.getErrorCode(),response.status());
        } catch (IOException e) {
            throw new ServiceDownException("Internal Server Error","INTERNAL_SERVER_ERROR",500);
        }
    }
}
