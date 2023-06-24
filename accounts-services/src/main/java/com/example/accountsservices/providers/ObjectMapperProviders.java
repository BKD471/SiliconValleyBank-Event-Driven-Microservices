package com.example.accountsservices.providers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Produces(MediaType.APPLICATION_JSON)
@Provider
public class ObjectMapperProviders implements ContextResolver<ObjectMapper> {

    ObjectMapper mapper;

    public void ObjectMapperProvider(){
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    /**
     * @param aClass
     * @return
     */
    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return mapper;
    }
}
