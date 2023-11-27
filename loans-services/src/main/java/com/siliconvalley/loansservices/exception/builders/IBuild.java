package com.siliconvalley.loansservices.exception.builders;

import com.siliconvalley.loansservices.helpers.AllConstantsHelper;

public interface IBuild {
    Exception build(AllConstantsHelper.ExceptionCodes exceptionCodes);
}
