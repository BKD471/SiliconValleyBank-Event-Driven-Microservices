package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.controller.AbstractParentController;
import com.example.accountsservices.controller.IBeneficiaryController;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import com.example.accountsservices.service.IBeneficiaryService;
import com.example.accountsservices.service.impl.BeneficiaryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BeneficiaryControllerImpl extends AbstractParentController implements IBeneficiaryController{

    private  final IBeneficiaryService beneficiaryService;

    BeneficiaryControllerImpl(BeneficiaryServiceImpl beneficiaryService){
        this.beneficiaryService=beneficiaryService;
    }



    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> getRequestForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException {
        return null;
    }

    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> postRequestForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException {
        return null;
    }

    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> putRequestForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException {
        return null;
    }

    /**
     * @param inputDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> deleteRequestForChange(InputDto inputDto) throws AccountsException, ResponseException, CustomerException {
        return null;
    }
}
