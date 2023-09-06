package com.siliconvalley.accountsservices.controller.Impl;

import com.siliconvalley.accountsservices.controller.IBeneficiaryController;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BeneficiaryException;
import com.siliconvalley.accountsservices.exception.CustomerException;
import com.siliconvalley.accountsservices.exception.ResponseException;
import com.siliconvalley.accountsservices.service.IBeneficiaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@RestController
public class BeneficiaryControllerImpl implements IBeneficiaryController {
    private static final String PATH_TO_PROPERTIES="accounts-services/src/main/java/com/siliconvalley/accountsservices/controller/properties/BeneficiaryController.properties";
    private final IBeneficiaryService beneficiaryService;
    private static final Properties properties=new Properties();

    static {
        try {
            properties.load(new FileInputStream(PATH_TO_PROPERTIES));
        }catch (IOException e){
            log.error("Error while reading properties file");
        }
    }

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
