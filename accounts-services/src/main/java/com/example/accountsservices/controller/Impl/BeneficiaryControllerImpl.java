package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.controller.IBeneficiaryController;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import com.example.accountsservices.service.IBeneficiaryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeneficiaryControllerImpl implements IBeneficiaryController {
    private final IBeneficiaryService beneficiaryService;

    BeneficiaryControllerImpl(@Qualifier("beneficiaryServicePrimary") IBeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    /**
     * @param getInputRequestDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> getRequestBenForChange(final GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        final OutputDto responseBody = beneficiaryService.getRequestBenExecutor(getInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * @param postInputRequestDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> postRequestBenForChange(final PostInputRequestDto postInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        final OutputDto responseBody = beneficiaryService.postRequestBenExecutor(postInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    /**
     * @param putInputRequestDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> putRequestBenForChange(final PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        final OutputDto responseBody = beneficiaryService.putRequestBenExecutor(putInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }

    /**
     * @param deleteInputRequestDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> deleteRequestBenForChange(final DeleteInputRequestDto deleteInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        final OutputDto responseBody = beneficiaryService.deleteRequestBenExecutor(deleteInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }
}
