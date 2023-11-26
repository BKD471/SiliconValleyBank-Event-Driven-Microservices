package com.siliconvalley.accountsservices.exception.builders;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;

public interface IBuild {
    Exception build(AllConstantHelpers.ExceptionCodes exceptionCodes);
}
