package ru.otr.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {

    private Long fromId;
    private Long toId;
    private BigDecimal amount;
}
