package org.example.service;


public interface PriceService {
    void addPrice(Price price);
    Price getNewestPriceByInstrument(String instrument);
}
