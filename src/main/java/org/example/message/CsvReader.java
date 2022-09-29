package org.example.message;

import org.example.service.Price;
import org.example.service.PriceService;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvReader implements MessageReader {

    private final DateTimeFormatter formatter;

    private final PriceService priceService;

    private static final String DELIMITER = ",";

    public CsvReader(DateTimeFormatter formatter, PriceService priceService) {
        this.formatter = formatter;
        this.priceService = priceService;
    }
    @Override
    public void onMessage(String message) {
        List<String> messageData = readData(message);
        messageData.forEach(this::buildPrice);
    }

    private void buildPrice(String priceMessage){
        try {
            String[] arrayMessage = priceMessage.split(DELIMITER);
            Price price = Price.builder()
                    .id(arrayMessage[0])
                    .instrument(arrayMessage[1])
                    .bid(new BigDecimal(arrayMessage[2]))
                    .ask(new BigDecimal(arrayMessage[3]))
                    .timestamp(LocalDateTime.parse(arrayMessage[4], formatter))
                    .build();
            priceService.addPrice(price);
        } catch (Exception e){
            throw new BuildPriceException("Can't build price from message: " + priceMessage);
        }
    }

    private List<String> readData(String data){
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new StringReader(data))) {
            String line = br.readLine();
            while (line != null) {
                result.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
