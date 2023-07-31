package com.siliconvalley.cardservices.services.Impl;

import com.siliconvalley.cardservices.exception.CardsException;
import com.siliconvalley.cardservices.dto.CardsDto;
import com.siliconvalley.cardservices.exception.TenureException;
import com.siliconvalley.cardservices.helpers.CardsMapperHelper;
import com.siliconvalley.cardservices.model.Cards;
import com.siliconvalley.cardservices.repository.ICardsRepository;
import com.siliconvalley.cardservices.services.ICardsService;
import com.siliconvalley.cardservices.services.IValidationService;
import com.siliconvalley.cardservices.helpers.AllConstantHelpers;
import com.siliconvalley.cardservices.helpers.RateOfInterestHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("cardsServicePrimary")
public class CardsServiceImpl implements ICardsService {
    private final IValidationService validationService;
    private final ICardsRepository cardsRepository;

    CardsServiceImpl(final IValidationService validationService,final ICardsRepository cardsRepository) {
        this.validationService = validationService;
        this.cardsRepository = cardsRepository;
    }

    private BigDecimal issuedCreditLimit(final Cards cards) {
        final BigDecimal CREDIT_LIMIT_FRACTION = BigDecimal.valueOf(0.1192);
        return new BigDecimal(String.valueOf(cards.getNetIncomePA())).multiply(CREDIT_LIMIT_FRACTION);
    }

    private Cards processCardInformation(final Cards cards) {
        final BigDecimal ISSUED_CREDIT_LIMIT = issuedCreditLimit(cards);
        final long BILL_GENERATION_IN_DAYS = 20;
        final long DUE_IN = 10;
        final LocalDateTime ISSUED_DATE = cards.getIssuedDate();
        final LocalDate BILL_GEN_DATE = ISSUED_DATE.plusDays(BILL_GENERATION_IN_DAYS).toLocalDate();
        final LocalDate DUE_DATE = BILL_GEN_DATE.plusDays(DUE_IN);
        final String cardNumber = UUID.randomUUID().toString();

        return Cards.builder()
                .cardNumber(cardNumber)
                .cardNetwork(cards.getCardNetwork())
                .cardType(cards.getCardType())
                .availableLimit(ISSUED_CREDIT_LIMIT)
                .billGenerationDate(BILL_GEN_DATE)
                .dueDate(DUE_DATE)
                .currentOutStanding(BigDecimal.valueOf(0.0d))
                .minimumDue(BigDecimal.valueOf(0.0d))
                .statementDue(BigDecimal.valueOf(0.0d))
                .lastPaidDate(null)
                .sanctionedCreditLimit(ISSUED_CREDIT_LIMIT)
                .rewardPoints(0)
                .amountPaid(BigDecimal.valueOf(0.0d))
                .unBilledOutstanding(BigDecimal.valueOf(0.0d)).build();
    }

    /**
     * @param cardsDto
     * @return
     */
    @Override
    public CardsDto issueCard(final CardsDto cardsDto) {
        //validating the unhappy path
        validationService.cardsValidator(cardsDto, null, AllConstantHelpers.ISSUE_CARD);
        final Cards card = CardsMapperHelper.mapToCards(cardsDto);
        final Cards savedCards = cardsRepository.save(card);
        final Cards processedCardsInfo = processCardInformation(savedCards);
        final Cards savedAndProcessedCardInfo = cardsRepository.save(processedCardsInfo);
        return CardsMapperHelper.mapToCardsDto(savedAndProcessedCardInfo);
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public List<CardsDto> getAllCardsByCustomerId(final String customerId) {
        final String methodName = "getAllCardsByCustomerId(Long) in CardsServiceImpl";
        final CardsDto cardsDto = CardsDto.builder().customerId(customerId).build();

        validationService.cardsValidator(cardsDto, null, AllConstantHelpers.GET_ALL_CARDS);

        final Optional<List<Cards>> listOfCards = cardsRepository.findAllByCustomerId(customerId);
        if (listOfCards.isEmpty()) throw new CardsException(CardsException.class,
                "No cards are linked to this account",
                methodName);
        return listOfCards.get().stream().map(CardsMapperHelper::mapToCardsDto).toList();
    }

    /**
     * @param cardsDto
     * @return
     */
    @Override
    public CardsDto payBills(final CardsDto cardsDto) {
        return null;
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<CardsDto> getPastTransactions(final LocalDate startDate,final LocalDate endDate) {
        return null;
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public int generateCreditScore(final String customerId) {
        final Optional<List<Cards>> listOfAllCards = cardsRepository.findAllByCustomerId(customerId);
        validationService.cardsValidator(null, null, AllConstantHelpers.GENERATE_CREDIT_SCORE);

        //to be done
        //fetch all pending loans, based in this generate credit score
        return 0;
    }

    /**
     * @param cardNumber
     * @return
     */
    @Override
    public CardsDto requestForRevisedCreditLimitForACard(final String cardNumber) {
        final String methodName = "requestForRevisedCreditLimitForACard(String) CardsServiceImpl";
        final Optional<Cards> loadCard = cardsRepository.findByCardNumber(cardNumber);
        if (loadCard.isEmpty()) throw new CardsException(CardsException.class,
                String.format("No cards exist with card Number %s", cardNumber), methodName);

        validationService.cardsValidator(null, loadCard.get(), AllConstantHelpers.REQUEST_FOR_REVISED_CREDIT_LIMIT);

        //to be done with loans
        return CardsMapperHelper.mapToCardsDto(loadCard.get());
    }

    private long calculateEmi(final Long loanAmount,final int tenure) throws TenureException {
        final String methodName = "calculateEmi(Long,int) in LoanServiceImpl";
        final Double rate_of_interest = RateOfInterestHelper.getRateOfInterest(tenure);
        if (rate_of_interest == null) throw new TenureException(TenureException.class,
                String.format("Tenure %s is not available", tenure), methodName);

        final double magic_co_eff = ((rate_of_interest / 100) / 12);
        final long interest = (long) (loanAmount * magic_co_eff);
        final double numerator = Math.pow(1 + magic_co_eff, tenure * 12);
        final double denominator = numerator - 1;
        final double emi_co_eff = (numerator / denominator);
        return (long) (interest * emi_co_eff);
    }

    /**
     * @param cardNumber
     * @return
     */
    @Override
    public CardsDto convertToFlexiPay(final String cardNumber) {
        final String methodName = "convertToFlexiPay(String) CardsServiceImpl";
        final Optional<Cards> loadCard = cardsRepository.findByCardNumber(cardNumber);
        if (loadCard.isEmpty()) throw new CardsException(CardsException.class, "Invalid cardNumber", methodName);
        validationService.cardsValidator(null, loadCard.get(), AllConstantHelpers.FLEXI_PAY);

        //to be
        return null;
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public CardsDto downloadCreditCardStatementsAsCsv(LocalDate startDate, LocalDate endDate) {
        return null;
    }
}