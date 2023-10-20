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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@RestController
@Tag(name = "BeneficiaryCOntroller",description = "Api for creation of beneficiary")
public class BeneficiaryControllerImpl implements IBeneficiaryController {
    private final IBeneficiaryService beneficiaryService;

    BeneficiaryControllerImpl(@Qualifier("beneficiaryServicePrimary") IBeneficiaryService beneficiaryService,
                              @Value("${path.controller.ben}") String path_ben_controller_properties) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(path_ben_controller_properties));
        }catch (IOException e){
            log.error("Error while reading {}'s properties file {}",
                    this.getClass().getSimpleName(),e.getMessage());
        }
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
