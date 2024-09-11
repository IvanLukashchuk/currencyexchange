package com.test.currencyexchange.repository;

import com.test.currencyexchange.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    @Query(value = "SELECT er.* FROM exchange_rate er " +
                   "INNER JOIN (" +
                   "    SELECT base_currency_code, MAX(timestamp) AS latest_timestamp " +
                   "    FROM exchange_rate " +
                   "    WHERE currency_code = :currencyCode " +
                   "    GROUP BY base_currency_code" +
                   ") er_max ON er.base_currency_code = er_max.base_currency_code " +
                   "AND er.timestamp = er_max.latest_timestamp " +
                   "WHERE er.currency_code = :currencyCode",
            nativeQuery = true)
    List<ExchangeRate> findLatestRatesByCurrencyCode(@Param("currencyCode") String currencyCode);
}
