package org.example.repository;

import lombok.Setter;
import org.example.service.Price;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class InMemoryPriceRepository implements PriceRepository {

    private List<Price> priceList = new ArrayList<>();
    @Override
    public void savePrice(Price price){
        priceList.add(price);
    }
    @Override
    public List<Price> getPricesByInstrument(String instrument){
        return priceList
                .stream()
                .filter(price -> price.getInstrument()
                        .equals(instrument))
                .collect(Collectors.toList());
    }


}
