package com.siliconvalley.loansservices.exception.builders;

import com.siliconvalley.loansservices.exception.*;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import org.apache.commons.configuration.plist.ParseException;

public class ExceptionBuilder implements IClassName, IReason, IMethodName, IBuild {
    private Object className;
    private String reason;
    private String methodName;

    private ExceptionBuilder() {
    }

    public static ExceptionBuilder builder() {
        return new ExceptionBuilder();
    }

    /**
     * @param className
     * @return
     */
    @Override
    public IReason className(Object className) {
        this.className = className;
        return this;
    }

    /**
     * @param methodName
     * @return
     */
    @Override
    public IBuild methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    /**
     * @param reason
     * @return
     */
    @Override
    public IMethodName reason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * @return
     */
    @Override
    public Exception build(AllConstantsHelper.ExceptionCodes exceptionCodes) {
        switch (exceptionCodes) {
            case LOAN_EXEC -> {
                return new LoansException(className, reason, methodName);
            }
            case TENURE_EXEC -> {
                return new TenureException(className, reason, methodName);
            }
            case BAD_API_EXEC -> {
                return new BadApiRequestException(className, reason, methodName);
            }
            case PAYMENT_EXEC -> {
                return new PaymentException(className, reason, methodName);
            }
            case INSTALLMENT_EXEC -> {
                return new InstallmentsException(className, reason, methodName);
            }
            case VALIDATION_EXEC -> {
                return new ValidationException(className, reason, methodName);
            }
            default -> throw new RuntimeException("Wrong Exception type");
        }
    }

}
