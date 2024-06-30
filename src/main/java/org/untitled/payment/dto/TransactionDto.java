package org.untitled.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TransactionDto {

    private String from;
    private String to;
    private BigDecimal amount;
    private LocalDateTime date;

}
