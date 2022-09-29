package org.example.service;

import org.example.repository.PriceRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MessageService implements PriceService{

    private final PriceRepository priceRepository;

    private final static BigDecimal bidCommission = BigDecimal.valueOf(0.001);
    private final static BigDecimal askCommission = BigDecimal.valueOf(0.001);

    public MessageService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public void addPrice(Price price) {
        BigDecimal bidPrice = withBidCommission(price.getBid());
        BigDecimal askPrice = withAskCommission(price.getAsk());
        price.setBid(bidPrice);
        price.setAsk(askPrice);
        priceRepository.savePrice(price);
    }

    @Override
    public Price getNewestPriceByInstrument(String instrument){
        List<Price> prices = priceRepository.getPricesByInstrument(instrument);
        Optional<Price> newestPrice = prices
                .stream()
                .max(Comparator.comparing(Price :: getTimestamp));
        if (newestPrice.isPresent()){
            return newestPrice.get();
        } else {
            throw new PriceNotFoundException("There are not any prices for this instrument " + instrument);
        }
    }

    private BigDecimal withBidCommission(BigDecimal price){
        BigDecimal commission = price.multiply(bidCommission);
        return setRound(price.subtract(commission));
    }

    private BigDecimal withAskCommission(BigDecimal price){
        BigDecimal commission = price.multiply(askCommission);
        return setRound(price.add(commission));
    }

    private BigDecimal setRound(BigDecimal price){
        return price.setScale(4, RoundingMode.HALF_UP);
    }
}
