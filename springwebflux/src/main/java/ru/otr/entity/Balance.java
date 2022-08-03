package ru.otr.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
@Data
@Builder
public class Balance {

    @Id
    private Long id;

    private Long memberId;

    private BigDecimal balance;
}