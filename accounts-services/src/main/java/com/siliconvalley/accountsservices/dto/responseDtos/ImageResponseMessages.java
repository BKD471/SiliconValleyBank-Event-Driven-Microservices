package com.siliconvalley.accountsservices.dto.responseDtos;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;


public record ImageResponseMessages(String imageName,String message,boolean success,HttpStatus status){

    public static final class Builder{
        private String imageName;
        private String message;
        private boolean success;
        private HttpStatus status;

        public Builder imageName(String imageName){
            this.imageName=imageName;
            return this;
        }
        public Builder message(String message){
            this.message=message;
            return this;
        }
        public Builder success(boolean success){
            this.success=success;
            return this;
        }
        public Builder status(HttpStatus status){
            this.status=status;
            return this;
        }

        public ImageResponseMessages build(){
            return new ImageResponseMessages(imageName,message,success,status);
        }
    }
}

