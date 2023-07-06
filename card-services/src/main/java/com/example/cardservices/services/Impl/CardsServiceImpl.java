package com.example.cardservices.services.Impl;

import com.example.cardservices.exception.CardsException;
import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.exception.TenureException;
import com.example.cardservices.mapper.CardsMapper;
import com.example.cardservices.model.Cards;
import com.example.cardservices.repository.ICardsRepository;
import com.example.cardservices.services.ICardsService;
import com.example.cardservices.services.IValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static com.example.cardservices.helpers.AllConstantHelpers.*;
import static com.example.cardservices.helpers.RateOfInterestHelper.getRateOfInterest;
import static com.example.cardservices.mapper.CardsMapper.mapToCards;
import static com.example.cardservices.mapper.CardsMapper.mapToCardsDto;

@Service("cardsServicePrimary")
public class CardsServiceImpl implements ICardsService {

    private final IValidationService validationService;
    private final ICardsRepository cardsRepository;
    private final int MONTHS_IN_YEAR=12;
    CardsServiceImpl(IValidationService validationService,ICardsRepository cardsRepository){
        this.validationService=validationService;
        this.cardsRepository=cardsRepository;
    }

    private Cards processCardInformation(Cards cards){
        double AVAILABLE_LIMIT=40000.0d;
        long BILL_GENERATION_IN_DAYS=20;
        long DUE_IN=10;
        LocalDateTime ISSUED_DATE=cards.getIssuedDate();
        LocalDate BILL_GEN_DATE=ISSUED_DATE.plusDays(BILL_GENERATION_IN_DAYS).toLocalDate();
        LocalDate DUE_DATE=BILL_GEN_DATE.plusDays(DUE_IN);
        String cardNumber= UUID.randomUUID().toString();

        return Cards.builder()
                .cardNumber(cardNumber)
                .cardNetwork(cards.getCardNetwork())
                .cardType(cards.getCardType())
                .availableLimit(AVAILABLE_LIMIT)
                .billGenerationDate(BILL_GEN_DATE)
                .dueDate(DUE_DATE)
                .currentOutStanding(0.0d)
                .minimumDue(0.0d)
                .statementDue(0.0d)
                .lastPaidDate(null)
                .totalLimit(AVAILABLE_LIMIT)
                .rewardPoints(0)
                .amountPaid(0.0d)
                .unBilledOutstanding(0.0d).build();
    }
    /**
     * @param cardsDto
     * @return
     */
    @Override
    public CardsDto issueCard(CardsDto cardsDto) {
        //validating the unhappy path
        validationService.cardsValidator(cardsDto,null,ISSUE_CARD);
        Cards card=mapToCards(cardsDto);
        Cards savedCards=cardsRepository.save(card);
        Cards processedCardsInfo=processCardInformation(savedCards);
        Cards savedAndProcessedCardInfo=cardsRepository.save(processedCardsInfo);
        return mapToCardsDto(savedAndProcessedCardInfo);
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public List<CardsDto> getAllCardsByCustomerId(String customerId) {
        String methodName="getAllCardsByCustomerId(Long) in CardsServiceImpl";
        CardsDto cardsDto=CardsDto.builder().customerId(customerId).build();
        validationService.cardsValidator(cardsDto,null,GET_ALL_CARDS);

        Optional<List<Cards>> listOfCards=cardsRepository.findAllByCustomerId(customerId);
        if(listOfCards.isEmpty()) throw new CardsException(CardsException.class,
                "No cards are linked to this account",
                methodName);
        return listOfCards.get().stream().map(CardsMapper::mapToCardsDto).toList();
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public int generateCreditScore(String customerId) {
        Optional<List<Cards>> listOfAllCards=cardsRepository.findAllByCustomerId(customerId);
        validationService.cardsValidator(null,null,GENERATE_CREDIT_SCORE);

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
        String methodName="requestForRevisedCreditLimitForACard(String) CardsServiceImpl";
        Optional<Cards> loadCard=cardsRepository.findByCardNumber(cardNumber);
        if(loadCard.isEmpty()) throw new CardsException(CardsException.class,
                String.format("No cards exist with card Number %s",cardNumber),methodName);

        validationService.cardsValidator(null,loadCard.get(),REQUEST_FOR_REVISED_CREDIT_LIMIT);

        //to be done with loans
        return mapToCardsDto(loadCard.get()) ;
    }

    private long calculateEmi(Long loanAmount, int tenure) throws TenureException {
        String methodName="calculateEmi(Long,int) in LoanServiceImpl";
        Double rate_of_interest = getRateOfInterest(tenure);
        if(rate_of_interest==null) throw new TenureException(TenureException.class,
                String.format("Tenure %s is not available",tenure),methodName);

        int PERCENTAGE = 100;
        Double magic_coeff = ((rate_of_interest / PERCENTAGE) / MONTHS_IN_YEAR);
        long interest = (long) (loanAmount * magic_coeff);

        double numerator = Math.pow(1 + magic_coeff, tenure * MONTHS_IN_YEAR);
        double denominator = numerator - 1;
        double emi_coeff = (numerator / denominator);

        return (long) (interest * emi_coeff);
    }

    /**
     * @param cardNumber
     * @return
     */
    @Override
    public CardsDto convertToFlexiPay(String cardNumber) {
        String methodName="convertToFlexiPay(String) CardsServiceImpl";
        Optional<Cards> loadCard=cardsRepository.findByCardNumber(cardNumber);
        if(loadCard.isEmpty()) throw new CardsException(CardsException.class,"Invalid cardNumber",methodName);
        validationService.cardsValidator(null,loadCard.get(),FLEXI_PAY);

        //to be
        return null;
    }
}