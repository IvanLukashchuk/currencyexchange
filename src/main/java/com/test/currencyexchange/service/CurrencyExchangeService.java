package com.test.currencyexchange.service;

import com.test.currencyexchange.entity.Currency;
import com.test.currencyexchange.entity.ExchangeRate;
import com.test.currencyexchange.external.ExchangeRateProvider;
import com.test.currencyexchange.repository.CurrencyRepository;
import com.test.currencyexchange.repository.ExchangeRateRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CurrencyExchangeService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateProvider exchangeRateProvider;
    private final Map<String, Map<String, BigDecimal>> rateCache = new ConcurrentHashMap<>();

    public CurrencyExchangeService(CurrencyRepository currencyRepository, ExchangeRateRepository exchangeRateRepository, ExchangeRateProvider exchangeRateProvider) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateProvider = exchangeRateProvider;
    }

    @PostConstruct
    public void loadCacheFromDatabase() {
        log.info("Loading cache from database...");
        List<Currency> currencies = currencyRepository.findAll();

        for (Currency currency : currencies) {
            List<ExchangeRate> exchangeRates = exchangeRateRepository.findLatestRatesByCurrencyCode(currency.getCurrencyCode());
            if (!exchangeRates.isEmpty()) {
                Map<String, BigDecimal> rates = exchangeRates.stream()
                        .collect(Collectors.toMap(ExchangeRate::getBaseCurrencyCode, ExchangeRate::getRate));

                rateCache.put(currency.getCurrencyCode(), rates);
                log.info("Loaded rates for currency: {}", currency.getCurrencyCode());
            }
        }

        log.info("Cache loading complete.");
    }

    public List<String> getCurrencies() {
        return currencyRepository.findAll().stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toList());
    }

    public Map<String, BigDecimal> getExchangeRatesForCurrency(String currencyCode) {
        return rateCache.getOrDefault(currencyCode, Collections.emptyMap());
    }

    @Transactional
    public void addNewCurrency(String currencyCode) {
        currencyRepository.findByCurrencyCode(currencyCode)
                .orElseGet(() -> currencyRepository.save(new Currency(currencyCode)));

        log.info("Added new currency: {}", currencyCode);

        fetchAndUpdateRates(currencyCode);
    }

    @Transactional
    public void fetchAndUpdateRates(String currencyCode) {
        Map<String, BigDecimal> rates = exchangeRateProvider.fetchRates(currencyCode);

        if (!rates.isEmpty()) {
            log.info("Updating cache and database for currency: {}", currencyCode);
            rateCache.put(currencyCode, rates);

            Currency currency = currencyRepository.findByCurrencyCode(currencyCode).get();

            List<ExchangeRate> exchangeRates = rates.entrySet().stream()
                    .map(entry -> new ExchangeRate(currency, entry.getValue(), entry.getKey()))
                    .collect(Collectors.toList());

            exchangeRateRepository.saveAll(exchangeRates);
        } else {
            log.warn("No rates fetched for currency: {}", currencyCode);
        }
    }

}
