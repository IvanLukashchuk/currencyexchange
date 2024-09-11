package com.test.currencyexchange.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Currency(String currencyCode) {
        this.currencyCode = currencyCode;
        this.createdAt = LocalDateTime.now();
    }
}