package com.test.currencyexchange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "currency_code", referencedColumnName = "currencyCode")
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal rate;

    @Column(nullable = false)
    private String baseCurrencyCode;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public ExchangeRate(Currency currency, BigDecimal rate, String baseCurrencyCode) {
        this.currency = currency;
        this.rate = rate;
        this.baseCurrencyCode = baseCurrencyCode;
        this.timestamp = LocalDateTime.now();
    }

}
