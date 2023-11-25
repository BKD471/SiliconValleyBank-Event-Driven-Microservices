package com.siliconvalley.accountsservices.exception.exceptionbuilders;

import com.siliconvalley.accountsservices.exception.*;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;

public class ExceptionBuilder implements IClassNameBuilder, IMethodNameBuilder, IReasonBuilder {
    private Object classsName;
    private String reason;
    private String methodName;
    private ExceptionBuilder(){}
    public static IClassNameBuilder builder(){
        return new ExceptionBuilder();
    }

    /**
     * @param className
     * @return
     */
    @Override
    public IReasonBuilder className(Object className) {
        this.classsName=className;
        return this;
    }

    /**
     * @param reason
     * @return
     */
    @Override
    public IMethodNameBuilder reason(String reason) {
        this.reason=reason;
        return this;
    }

    /**
     * @param methodName
     * @return
     */
    @Override
    public IMethodNameBuilder methodName(String methodName) {
        this.methodName=methodName;
        return this;
    }


    public Exception build(AllConstantHelpers.ExceptionCodes exceptionCodes){
        switch (exceptionCodes){
            case ACC_EXC -> {
                return new AccountsException(classsName,reason,methodName);
            }
            case BEN_EXC -> {
                return new BeneficiaryException(classsName,reason,methodName);
            }
            case RES_EXC -> {
                return new ResponseException(classsName,reason,methodName);
            }
            case CUST_EXC -> {
                return new CustomerException(classsName,reason,methodName);
            }
            case TRAN_EXC -> {
                return new TransactionException(classsName,reason,methodName);
            }
            case ROLE_EXC -> {
                return new RolesException(classsName,reason,methodName);
            }
            case BAD_API_EXC -> {
                return new BadApiRequestException(classsName,reason,methodName);
            }
            default -> throw new RuntimeException("Invalid Exception Codes");
        }
    }
}
