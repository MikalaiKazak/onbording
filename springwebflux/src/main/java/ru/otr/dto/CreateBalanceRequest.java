package ru.otr.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateBalanceRequest {

    private Long memberId;

    private BigDecimal balance;
}