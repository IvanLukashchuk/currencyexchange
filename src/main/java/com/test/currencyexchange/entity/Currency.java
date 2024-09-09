package com.test.currencyexchange.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Currency() {}

    public Currency(String currencyCode) {
        this.currencyCode = currencyCode;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
