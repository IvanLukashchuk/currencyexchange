package com.test.currencyexchange.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
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

    public ExchangeRate() {}

    public ExchangeRate(Currency currency, BigDecimal rate, String baseCurrencyCode) {
        this.currency = currency;
        this.rate = rate;
        this.baseCurrencyCode = baseCurrencyCode;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
