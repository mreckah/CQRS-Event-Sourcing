package net.oussama.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperation {
    private String accountId;
    private String operationType; // CREATED, CREDITED, DEBITED
    private BigDecimal amount;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime timestamp;
}
