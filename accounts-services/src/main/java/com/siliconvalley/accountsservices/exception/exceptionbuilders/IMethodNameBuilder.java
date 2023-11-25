package com.siliconvalley.accountsservices.exception.exceptionbuilders;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;

public interface IMethodNameBuilder {
    IMethodNameBuilder methodName(String methodName);
    Exception build(AllConstantHelpers.ExceptionCodes exceptionCodes);
}
