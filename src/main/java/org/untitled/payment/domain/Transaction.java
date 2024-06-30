package org.untitled.payment.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.untitled.payment.dto.TransactionDto;

@Data
@Entity(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "from_customer", nullable = false)
    private String from;

    @Column(name = "to_customer", nullable = false)
    private String to;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    public static Transaction from(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setFrom(transactionDto.getFrom());
        transaction.setTo(transactionDto.getTo());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(transactionDto.getDate());
        return transaction;

    }

}
