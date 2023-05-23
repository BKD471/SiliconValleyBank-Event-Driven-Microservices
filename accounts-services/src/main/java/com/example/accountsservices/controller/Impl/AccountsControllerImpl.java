package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.controller.AbstractParentController;
import com.example.accountsservices.controller.IAccountsController;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import com.example.accountsservices.service.IAccountsService;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsControllerImpl extends AbstractParentController implements IAccountsController {
    private final IAccountsService accountsService;

    AccountsControllerImpl(AccountsServiceImpl accountsService) {
        this.accountsService = accountsService;
    }

    /**
     * @param getInputRequestDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> getRequestForChange(GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException {
        OutputDto responseBody = accountsService.getRequestExecutor(getInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * @param postInputDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> postRequestForChange(PostInputRequestDto postInputDto) throws AccountsException, ResponseException, CustomerException {
        OutputDto responseBody = accountsService.postRequestExecutor(postInputDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    /**
     * @param putInputRequestDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> putRequestForChange(PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException {
        OutputDto responseBody = accountsService.putRequestExecutor(putInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }

    /**
     * @param deleteInputRequestDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> deleteRequestForChange(DeleteInputRequestDto deleteInputRequestDto) throws AccountsException, ResponseException, CustomerException {
        OutputDto responseBody = accountsService.deleteRequestExecutor(deleteInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }
}
