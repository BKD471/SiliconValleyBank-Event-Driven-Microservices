package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.controller.AbstractParentController;
import com.example.accountsservices.controller.IBeneficiaryController;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import com.example.accountsservices.service.IBeneficiaryService;
import com.example.accountsservices.service.impl.BeneficiaryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeneficiaryControllerImpl extends AbstractParentController implements IBeneficiaryController{

    private  final IBeneficiaryService beneficiaryService;

    BeneficiaryControllerImpl(BeneficiaryServiceImpl beneficiaryService){
        this.beneficiaryService=beneficiaryService;
    }



//    private ResponseEntity<OutputDto> commonResponseBuilder(InputDto inputDto, ResponseTypes.ResponsesType responsesType) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
//        String methodName="commonResponseBuilder(InputDto) in AccountsServiceImpl";
//
//        OutputDto responseBody=null;
//        switch (responsesType){
//            case GET ->{
//                responseBody=beneficiaryService.getRequestBenExecutor(inputDto);
//                return new ResponseEntity<>(responseBody, HttpStatus.OK);
//            }
//            case POST -> {
//                responseBody=beneficiaryService.postRequestBenExecutor(inputDto);
//                return new ResponseEntity<>(responseBody,HttpStatus.CREATED);
//            }
//            case PUT -> {
//                responseBody=beneficiaryService.putRequestBenExecutor(inputDto);
//                return new ResponseEntity<>(responseBody,HttpStatus.ACCEPTED);
//            }
//            case DELETE -> {
//                responseBody=beneficiaryService.deleteRequestBenExecutor(inputDto);
//                return new ResponseEntity<>(responseBody,HttpStatus.ACCEPTED);
//            }
//            default -> throw new ResponseException(ResponseException.class,String.format("No such response pf" +
//                    " this type %s is valid",responsesType),methodName);
//        }
//    }
    /**
     * @param getInputRequestDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> getRequestBenForChange(GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        OutputDto responseBody=beneficiaryService.getRequestBenExecutor(getInputRequestDto);
        return new ResponseEntity<>(responseBody,HttpStatus.OK);
        //return commonResponseBuilder(inputDto, ResponseTypes.GET);
    }

    /**
     * @param postInputRequestDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> postRequestBenForChange(PostInputRequestDto postInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        OutputDto responseBody=beneficiaryService.postRequestBenExecutor(postInputRequestDto);
        return new ResponseEntity<>(responseBody,HttpStatus.CREATED);
        //return commonResponseBuilder(inputDto,ResponseTypes.POST);
    }

    /**
     * @param putInputRequestDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> putRequestBenForChange(PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
        OutputDto responseBody=beneficiaryService.putRequestBenExecutor(putInputRequestDto);
        return  new ResponseEntity<>(responseBody,HttpStatus.ACCEPTED);
        //return commonResponseBuilder(inputDto,ResponseTypes.PUT);
    }

    /**
     * @param postInputDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     */
    @Override
    public ResponseEntity<OutputDto> deleteRequestBenForChange(DeleteInputRequestDto deleteInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException {
         OutputDto responseBody=beneficiaryService.deleteRequestBenExecutor(deleteInputRequestDto);
         return new ResponseEntity<>(responseBody,HttpStatus.ACCEPTED);
        //return commonResponseBuilder(inputDto,ResponseTypes.DELETE);
    }
}
