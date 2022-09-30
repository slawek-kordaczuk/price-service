package org.example;

import org.apache.commons.io.IOUtils;
import org.example.controller.MessageController;
import org.example.controller.PriceResponse;
import org.example.message.BuildPriceException;
import org.example.repository.InMemoryPriceRepository;
import org.example.message.CsvReader;
import org.example.message.MessageReader;
import org.example.repository.PriceRepository;
import org.example.service.CommissionPriceService;
import org.example.service.PriceNotFoundException;
import org.example.service.PriceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PriceServiceIntegrationTest {

    private static MessageController messageController;

    private static MessageReader messageReader;

    @BeforeAll
    static void setData(){
        PriceRepository inMemoryPriceRepository = new InMemoryPriceRepository();
        PriceService priceService = new CommissionPriceService(inMemoryPriceRepository);
        messageReader = new CsvReader(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss:SSS"), priceService);
        messageController = new MessageController(priceService);
    }

    @Test
    void getLatestPriceByInstrumentTest() throws IOException {
        // given
        String instrument = "GBP/USD";
        String message = readFile("prices.csv");
        // when
        messageReader.onMessage(message);
        PriceResponse response = messageController.getNewestPrice(instrument);
        // then
        assertEquals("1.6760", response.getBid().toString());
        assertEquals("1.9009", response.getAsk().toString());
        assertEquals(instrument, response.getInstrument());
    }

    @Test
    void notCorrectDataTest(){
        // given
        String message = "not correct data";
        // when
        BuildPriceException buildPriceException = assertThrows(BuildPriceException.class, () -> messageReader.onMessage(message));
        // then
        assertEquals("Can't build price from message: " + message, buildPriceException.getMessage());
    }

    @Test
    void notFoundInstrumentTest() throws IOException {
        // given
        String instrument = "PLN/USD";
        String message = readFile("prices.csv");
        // when
        messageReader.onMessage(message);
        PriceNotFoundException priceNotFoundException = assertThrows(PriceNotFoundException.class, () -> messageController.getNewestPrice(instrument));
        // then
        assertEquals("There are not any prices for this instrument " + instrument, priceNotFoundException.getMessage());
    }

    private String readFile(String path) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            return IOUtils.toString(Objects.requireNonNull(is), StandardCharsets.UTF_8);
        }
    }
}
