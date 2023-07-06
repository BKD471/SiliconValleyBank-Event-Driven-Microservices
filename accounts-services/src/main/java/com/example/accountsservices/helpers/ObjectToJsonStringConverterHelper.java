package com.example.accountsservices.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ObjectToJsonStringConverterHelper {
    public static String convertObjToJsonString(final Object dto) {
        try{
            final ObjectWriter ow=new ObjectMapper().writer().withDefaultPrettyPrinter();
            return ow.writeValueAsString(ow);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
