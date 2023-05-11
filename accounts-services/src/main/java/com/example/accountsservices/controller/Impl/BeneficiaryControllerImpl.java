package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.controller.AbstractParentController;
import com.example.accountsservices.controller.IBeneficiaryController;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import com.example.accountsservices.helpers.ResponseTypes;
import com.example.accountsservices.service.IBeneficiaryService;
import com.example.accountsservices.service.impl.BeneficiaryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BeneficiaryControllerImpl extends AbstractParentController implements IBeneficiaryController{

    private  final IBeneficiaryService beneficiaryService;

    BeneficiaryControllerImpl(BeneficiaryServiceImpl beneficiaryService){
        this.beneficiaryService=beneficiaryService;
    }



    private ResponseEntity<OutputDto> commonResponseBuilder(InputDto inputDto, ResponseTypes.ResponsesType responsesType) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        String methodName="commonResponseBuilder(InputDto) in AccountsServiceImpl";

        OutputDto responseBody=null;
        switch (responsesType){
            case GET ->{
                responseBody=beneficiaryService.getRequestBenExecutor(inputDto);
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            }
            case POST -> {
                responseBody=beneficiaryService.postRequestBenExecutor(inputDto);
                return new ResponseEntity<>(responseBody,HttpStatus.CREATED);
            }
            case PUT -> {
                responseBody=beneficiaryService.putRequestBenExecutor(inputDto);
                return new ResponseEntity<>(responseBody,HttpStatus.ACCEPTED);
            }
            case DELETE -> {
                responseBody=beneficiaryService.deleteRequestBenExecutor(inputDto);
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
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> getRequestBenForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        return commonResponseBuilder(inputDto, ResponseTypes.GET);
    }

    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> postRequestBenForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        return commonResponseBuilder(inputDto,ResponseTypes.POST);
    }

    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> putRequestBenForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        return commonResponseBuilder(inputDto,ResponseTypes.PUT);
    }

    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> deleteRequestBenForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        return commonResponseBuilder(inputDto,ResponseTypes.DELETE);
    }
}
