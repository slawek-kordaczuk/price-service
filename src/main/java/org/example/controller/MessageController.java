package org.example.controller;

import org.example.service.Price;
import org.example.service.PriceService;

public class MessageController {

    private final PriceService priceService;
    public MessageController(PriceService priceService) {
        this.priceService = priceService;
    }

    public PriceResponse getNewestPrice(String instrument){
        Price instrumentPrice = priceService.getNewestPriceByInstrument(instrument);
        return PriceResponse
                .builder()
                .instrument(instrumentPrice.getInstrument())
                .bid(instrumentPrice.getBid())
                .ask(instrumentPrice.getAsk())
                .build();
    }
}
