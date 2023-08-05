package com.siliconvalley.accountsservices.controller.Impl;

import com.siliconvalley.accountsservices.controller.IAccountsController;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.dto.responseDtos.ImageResponseMessages;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.CustomerException;
import com.siliconvalley.accountsservices.exception.ResponseException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.service.IAccountsService;
import com.siliconvalley.accountsservices.service.IImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RestController
public class AccountsControllerImpl implements IAccountsController {
    private final IAccountsService accountsService;
    private final ICustomerRepository customerRepository;
    private final IImageService fIleService;
    private final String IMAGE_PATH;

    AccountsControllerImpl(@Qualifier("accountsServicePrimary") IAccountsService accountsService,
                           ICustomerRepository customerRepository,
                           @Qualifier("fileServicePrimary") IImageService fIleService,
                           @Value("${customer.profile.images.path}") String IMAGE_PATH) {
        this.accountsService = accountsService;
        this.customerRepository = customerRepository;
        this.fIleService = fIleService;
        this.IMAGE_PATH=IMAGE_PATH;
    }


    /**
     * @param getInputRequestDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     * @throws IOException
     */
    @Override
    public ResponseEntity<OutputDto> getRequestForChange(GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException, IOException {
            final OutputDto responseBody = accountsService.getRequestExecutor(getInputRequestDto);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * @param customerId
     * @param response
     */
    @Override
    public void serveUserImage(final String customerId,final HttpServletResponse response) throws IOException {
        final String methodName = "serveUserImage(Long,HttpServlet) in AccountsControllerImpl";
        final Optional<Customer> fetchedCustomer = customerRepository.findById(customerId);
        if (fetchedCustomer.isEmpty())
            throw new CustomerException(CustomerException.class, String.format("No such customer with id:%s", customerId), methodName);
        final InputStream resource = fIleService.getResource(IMAGE_PATH, fetchedCustomer.get().getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    /**
     * @param postInputDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> postRequestForChange(final PostInputRequestDto postInputDto) throws AccountsException, ResponseException, CustomerException, IOException {
        final OutputDto responseBody = accountsService.postRequestExecutor(postInputDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    /**
     * @param postInputDto
     * @return
     * @throws AccountsException
     * @throws ResponseException
     * @throws CustomerException
     * @throws IOException
     */
    @Override
    public ResponseEntity<OutputDto> createAccount(final PostInputRequestDto postInputDto) throws AccountsException, ResponseException, CustomerException {
        final OutputDto responseBody = accountsService.accountSetUp(postInputDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    /**
     * @param putInputRequestDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> putRequestForChange(final PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException, IOException {
        final OutputDto responseBody = accountsService.putRequestExecutor(putInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }

    /**
     * @param image
     * @param customerId
     * @return
     */
    @Override
    public ResponseEntity<ImageResponseMessages> uploadCustomerImage(final MultipartFile image, final String customerId) throws IOException {
        final PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .updateRequest(AllConstantHelpers.UpdateRequest.UPLOAD_CUSTOMER_IMAGE)
                .customerId(customerId)
                .customerImage(image)
                .build();
        final OutputDto responseBody = accountsService.putRequestExecutor(putInputRequestDto);
        final ImageResponseMessages imgResponseMessages= ImageResponseMessages.builder()
                .message(responseBody.getDefaultMessage())
                .imageName(responseBody.getCustomer().getImageName())
                .status(HttpStatus.CREATED)
                .success(true)
                .build();
        return new ResponseEntity<>(imgResponseMessages, HttpStatus.CREATED);
    }

    /**
     * @param deleteInputRequestDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> deleteRequestForChange(final DeleteInputRequestDto deleteInputRequestDto) throws AccountsException, ResponseException, CustomerException {
        final OutputDto responseBody = accountsService.deleteRequestExecutor(deleteInputRequestDto);
        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }

    /**
     * @param deleteInputRequestDto
     * @return
     */
    @Override
    public ResponseEntity<OutputDto> deleteCustomer(final DeleteInputRequestDto deleteInputRequestDto) {
        final OutputDto responseBody=accountsService.deleteCustomer(deleteInputRequestDto);
        return new ResponseEntity<>(responseBody,HttpStatus.ACCEPTED);
    }
}
