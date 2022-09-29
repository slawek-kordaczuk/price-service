package org.example.repository;

import org.example.service.Price;

import java.util.List;

public interface PriceRepository {
    void savePrice(Price price);

    List<Price> getPricesByInstrument(String instrument);
}
