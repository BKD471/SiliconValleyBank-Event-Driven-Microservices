package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.controller.AbstractParentController;
import com.example.accountsservices.controller.IAccountsController;
import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.service.IAccountsService;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import com.example.accountsservices.service.impl.FIleServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RestController
public class AccountsControllerImpl extends AbstractParentController implements IAccountsController {
    private final IAccountsService accountsService;
    private final CustomerRepository customerRepository;
    private final FIleServiceImpl fIleService;

    @Value("${customer.profile.images.path}")
    private String IMAGE_PATH;

    AccountsControllerImpl(AccountsServiceImpl accountsService,
                           CustomerRepository customerRepository,
                           FIleServiceImpl fIleService) {
        this.accountsService = accountsService;
        this.customerRepository = customerRepository;
        this.fIleService = fIleService;
    }

    /**
     * @param getInputRequestDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> getRequestForChange(GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException, IOException {
        OutputDto responseBody = accountsService.getRequestExecutor(getInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * @param customerId
     * @param response
     * @return
     */
    @Override
    public void serveUserImage(Long customerId, HttpServletResponse response) throws IOException {
        String methodName = "serveUserImage(Long,HttpServlet) in AccountsControllerImpl";
        Optional<Customer> fetchedCustomer = customerRepository.findById(customerId);
        if (fetchedCustomer.isEmpty())
            throw new CustomerException(CustomerException.class, String.format("No such customer with id:%s", customerId), methodName);
        InputStream resource = fIleService.getResource(IMAGE_PATH, fetchedCustomer.get().getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    /**
     * @param postInputDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> postRequestForChange(PostInputRequestDto postInputDto) throws AccountsException, ResponseException, CustomerException, IOException {
        OutputDto responseBody = accountsService.postRequestExecutor(postInputDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    /**
     * @param putInputRequestDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> putRequestForChange(PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException, IOException {
        OutputDto responseBody = accountsService.putRequestExecutor(putInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }

    /**
     * @param image
     * @param customerId
     * @return
     */
    @Override
    public ResponseEntity<OutputDto> uploadCustomerImage(MultipartFile image, Long customerId) throws IOException {
        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .updateRequest(AccountsDto.UpdateRequest.UPLOAD_CUSTOMER_IMAGE)
                .customerId(customerId)
                .customerImage(image)
                .build();
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
