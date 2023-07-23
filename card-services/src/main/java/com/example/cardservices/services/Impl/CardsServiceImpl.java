package com.example.cardservices.services.Impl;

import com.example.cardservices.exception.CardsException;
import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.exception.TenureException;
import com.example.cardservices.helpers.CardsMapperHelper;
import com.example.cardservices.model.Cards;
import com.example.cardservices.repository.ICardsRepository;
import com.example.cardservices.services.ICardsService;
import com.example.cardservices.services.IValidationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static com.example.cardservices.helpers.AllConstantHelpers.*;
import static com.example.cardservices.helpers.RateOfInterestHelper.getRateOfInterest;
import static com.example.cardservices.helpers.CardsMapperHelper.mapToCards;
import static com.example.cardservices.helpers.CardsMapperHelper.mapToCardsDto;

@Service("cardsServicePrimary")
public class CardsServiceImpl implements ICardsService {
    private final IValidationService validationService;
    private final ICardsRepository cardsRepository;

    CardsServiceImpl(IValidationService validationService, ICardsRepository cardsRepository) {
        this.validationService = validationService;
        this.cardsRepository = cardsRepository;
    }

    private BigDecimal issuedCreditLimit(Cards cards) {
        final BigDecimal CREDIT_LIMIT_FRACTION = BigDecimal.valueOf(0.1192);
        return new BigDecimal(String.valueOf(cards.getNetIncomePA())).multiply(CREDIT_LIMIT_FRACTION);
    }

    private Cards processCardInformation(Cards cards) {
        final BigDecimal ISSUED_CREDIT_LIMIT = issuedCreditLimit(cards);
        final long BILL_GENERATION_IN_DAYS = 20;
        final long DUE_IN = 10;
        LocalDateTime ISSUED_DATE = cards.getIssuedDate();
        LocalDate BILL_GEN_DATE = ISSUED_DATE.plusDays(BILL_GENERATION_IN_DAYS).toLocalDate();
        LocalDate DUE_DATE = BILL_GEN_DATE.plusDays(DUE_IN);
        String cardNumber = UUID.randomUUID().toString();

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
    public CardsDto issueCard(CardsDto cardsDto) {
        //validating the unhappy path
        validationService.cardsValidator(cardsDto, null, ISSUE_CARD);
        Cards card = mapToCards(cardsDto);
        Cards savedCards = cardsRepository.save(card);
        Cards processedCardsInfo = processCardInformation(savedCards);
        Cards savedAndProcessedCardInfo = cardsRepository.save(processedCardsInfo);
        return mapToCardsDto(savedAndProcessedCardInfo);
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public List<CardsDto> getAllCardsByCustomerId(String customerId) {
        String methodName = "getAllCardsByCustomerId(Long) in CardsServiceImpl";
        CardsDto cardsDto = CardsDto.builder().customerId(customerId).build();
        validationService.cardsValidator(cardsDto, null, GET_ALL_CARDS);

        Optional<List<Cards>> listOfCards = cardsRepository.findAllByCustomerId(customerId);
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
    public CardsDto payBills(CardsDto cardsDto) {
        return null;
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<CardsDto> getPastTransactions(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public int generateCreditScore(String customerId) {
        Optional<List<Cards>> listOfAllCards = cardsRepository.findAllByCustomerId(customerId);
        validationService.cardsValidator(null, null, GENERATE_CREDIT_SCORE);

        //to be done
        //fetch all pending loans, based in this generate credit score
        return 0;
    }

    /**
     * @param cardNumber
     * @return
     */
    @Override
    public CardsDto requestForRevisedCreditLimitForACard(String cardNumber) {
        String methodName = "requestForRevisedCreditLimitForACard(String) CardsServiceImpl";
        Optional<Cards> loadCard = cardsRepository.findByCardNumber(cardNumber);
        if (loadCard.isEmpty()) throw new CardsException(CardsException.class,
                String.format("No cards exist with card Number %s", cardNumber), methodName);

        validationService.cardsValidator(null, loadCard.get(), REQUEST_FOR_REVISED_CREDIT_LIMIT);

        //to be done with loans
        return mapToCardsDto(loadCard.get());
    }

    private long calculateEmi(Long loanAmount, int tenure) throws TenureException {
        String methodName = "calculateEmi(Long,int) in LoanServiceImpl";
        Double rate_of_interest = getRateOfInterest(tenure);
        if (rate_of_interest == null) throw new TenureException(TenureException.class,
                String.format("Tenure %s is not available", tenure), methodName);

        double magic_co_eff = ((rate_of_interest / 100) / 12);
        long interest = (long) (loanAmount * magic_co_eff);
        double numerator = Math.pow(1 + magic_co_eff, tenure * 12);
        double denominator = numerator - 1;
        double emi_co_eff = (numerator / denominator);
        return (long) (interest * emi_co_eff);
    }

    /**
     * @param cardNumber
     * @return
     */
    @Override
    public CardsDto convertToFlexiPay(String cardNumber) {
        String methodName = "convertToFlexiPay(String) CardsServiceImpl";
        Optional<Cards> loadCard = cardsRepository.findByCardNumber(cardNumber);
        if (loadCard.isEmpty()) throw new CardsException(CardsException.class, "Invalid cardNumber", methodName);
        validationService.cardsValidator(null, loadCard.get(), FLEXI_PAY);

        //to be
        return null;
    }
}