package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.controller.AbstractParentController;
import com.example.accountsservices.controller.IAccountsController;
import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import com.example.accountsservices.service.IAccountsService;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountsControllerImpl extends AbstractParentController implements IAccountsController{
    private final IAccountsService accountsService;

    private enum ResponsesType{
        GET,POST,PUT,DELETE
    }

    private final ResponsesType GET=ResponsesType.GET;
    private final ResponsesType POST=ResponsesType.POST;
    private final ResponsesType PUT=ResponsesType.PUT;
    private final ResponsesType DELETE=ResponsesType.DELETE;

    AccountsControllerImpl(AccountsServiceImpl accountsService) {
        this.accountsService = accountsService;
    }


    private ResponseEntity<OutputDto> commonResponseBuilder(InputDto inputDto,ResponsesType responsesType) throws AccountsException, ResponseException, CustomerException {
        String methodName="commonResponseBuilder(InputDto) in AccountsServiceImpl";

        OutputDto responseBody=null;
        switch (responsesType){
            case GET ->{
                responseBody=accountsService.getRequestExecutor(inputDto);
                return new ResponseEntity<>(responseBody,HttpStatus.OK);
            }
            case POST -> {
                responseBody=accountsService.postRequestExecutor(inputDto);
                return new ResponseEntity<>(responseBody,HttpStatus.CREATED);
            }
            case PUT -> {
                responseBody=accountsService.putRequestExecutor(inputDto);
                return new ResponseEntity<>(responseBody,HttpStatus.ACCEPTED);
            }
            case DELETE -> {
                responseBody=accountsService.deleteRequestExecutor(inputDto);
                return new ResponseEntity<>(responseBody,HttpStatus.ACCEPTED);
            }
            default -> throw new ResponseException(ResponseException.class,String.format("No such response pf" +
                    " this type %s is valid",responsesType),methodName);
        }
    }

    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> getRequestForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException {
        return commonResponseBuilder(inputDto,ResponsesType.GET);
    }

    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> postRequestForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException {
        return  commonResponseBuilder(inputDto,ResponsesType.POST);
    }

    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> putRequestForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException {
        return commonResponseBuilder(inputDto,ResponsesType.PUT);
    }

    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> deleteRequestForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException {
        return commonResponseBuilder(inputDto,ResponsesType.DELETE);
    }
}
