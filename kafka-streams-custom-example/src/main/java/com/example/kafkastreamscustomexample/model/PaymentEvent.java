package com.example.kafkastreamscustomexample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentEvent {
    
    private String paymentId;

    private Long amount;

    private String currency;

    private String fromAccount;

    private String toAccount;

    private String mode;
}
