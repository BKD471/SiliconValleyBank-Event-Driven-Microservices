package com.siliconvalley.loansservices.service.impl;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.exception.InstallmentsException;
import com.siliconvalley.loansservices.exception.LoansException;
import com.siliconvalley.loansservices.exception.PaymentException;
import com.siliconvalley.loansservices.exception.ValidationException;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import com.siliconvalley.loansservices.model.Emi;
import com.siliconvalley.loansservices.model.Loans;
import com.siliconvalley.loansservices.repository.ILoansRepository;
import com.siliconvalley.loansservices.service.IPdfService;
import com.siliconvalley.loansservices.service.IValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.siliconvalley.loansservices.helpers.AllConstantsHelper.LoansValidateType.GEN_EMI_STMT;

@Slf4j
@Service("jasperPdfService")
public class PdfServiceJasperImpl implements IPdfService {

    private final ILoansRepository loansRepository;
    private final IValidationService validationService;

    PdfServiceJasperImpl(ILoansRepository loansRepository,IValidationService validationService){
        this.loansRepository=loansRepository;
        this.validationService=validationService;
    }

    /**
     * @param loans
     * @param emiList
     * @param startDate
     * @param endDate
     * @param formatType
     */
    @Override
    public void generateStatement(final Loans loans, final List<Emi> emiList, final LocalDate startDate, final LocalDate endDate, AllConstantsHelper.FormatType formatType) throws ValidationException, PaymentException, LoansException, InstallmentsException {
        log.debug("################# Pdf Creation Service started ###################################");
        Optional<Set<Loans>> loansSet=loansRepository.findAllByCustomerId(loans.getCustomerId());
        validationService.validator(null, LoansDto.builder().loansSet(loansSet.get()).build(),
                GEN_EMI_STMT,loansSet);




    }
}
