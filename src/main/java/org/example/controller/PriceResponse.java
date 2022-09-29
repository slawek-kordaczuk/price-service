package org.example.controller;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class PriceResponse {
    String instrument;
    BigDecimal bid;
    BigDecimal ask;
}
