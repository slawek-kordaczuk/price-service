package org.example.service;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Price {
    String id;
    String instrument;
    BigDecimal bid;
    BigDecimal ask;
    LocalDateTime timestamp;
}
