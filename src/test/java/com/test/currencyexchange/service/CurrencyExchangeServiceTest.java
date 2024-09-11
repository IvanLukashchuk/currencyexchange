package com.test.currencyexchange.service;

import com.test.currencyexchange.entity.Currency;
import com.test.currencyexchange.entity.ExchangeRate;
import com.test.currencyexchange.external.ExchangeRateProvider;
import com.test.currencyexchange.repository.CurrencyRepository;
import com.test.currencyexchange.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyExchangeServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private ExchangeRateProvider exchangeRateProvider;

    @InjectMocks
    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyExchangeService = new CurrencyExchangeService(currencyRepository, exchangeRateRepository, exchangeRateProvider);
    }

    @Test
    void shouldAddNewCurrencyAndFetchRates() {
        String currencyCode = "USD";
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", BigDecimal.valueOf(0.89));

        when(currencyRepository.findByCurrencyCode(currencyCode)).thenReturn(Optional.empty(), Optional.of(new Currency(currencyCode)));
        when(currencyRepository.save(any(Currency.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(exchangeRateProvider.fetchRates(currencyCode)).thenReturn(rates);

        currencyExchangeService.addNewCurrency(currencyCode);

        verify(currencyRepository).save(any(Currency.class));
        verify(exchangeRateProvider).fetchRates(currencyCode);
        verify(exchangeRateRepository, times(1)).saveAll(any());

        Map<String, BigDecimal> cachedRates = currencyExchangeService.getExchangeRatesForCurrency(currencyCode);
        assertEquals(BigDecimal.valueOf(0.89), cachedRates.get("EUR"));
    }

    @Test
    void shouldReturnEmptyRatesWhenCurrencyNotInCache() {
        String currencyCode = "USD";
        Map<String, BigDecimal> cachedRates = currencyExchangeService.getExchangeRatesForCurrency(currencyCode);

        assertTrue(cachedRates.isEmpty());
    }

    @Test
    void shouldReturnExchangeRatesFromCache() {
        String currencyCode = "USD";
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", BigDecimal.valueOf(0.89));

        when(exchangeRateProvider.fetchRates(currencyCode)).thenReturn(rates);
        when(currencyRepository.findByCurrencyCode(currencyCode)).thenReturn(Optional.of(new Currency(currencyCode)));

        currencyExchangeService.addNewCurrency(currencyCode);

        Map<String, BigDecimal> cachedRates = currencyExchangeService.getExchangeRatesForCurrency(currencyCode);

        assertNotNull(cachedRates);
        assertEquals(BigDecimal.valueOf(0.89), cachedRates.get("EUR"));
    }

    @Test
    void shouldHandleApiFailureGracefully() {
        String currencyCode = "USD";

        when(currencyRepository.findByCurrencyCode(currencyCode)).thenReturn(Optional.of(new Currency(currencyCode)));
        when(exchangeRateProvider.fetchRates(currencyCode)).thenReturn(new HashMap<>());

        currencyExchangeService.addNewCurrency(currencyCode);

        Map<String, BigDecimal> cachedRates = currencyExchangeService.getExchangeRatesForCurrency(currencyCode);

        assertTrue(cachedRates.isEmpty());
    }

    @Test
    void shouldLoadCacheFromDatabaseOnStartup() {
        Currency usd = new Currency(1L, "USD", null);
        List<Currency> currencies = List.of(usd);

        List<ExchangeRate> exchangeRates = List.of(
                new ExchangeRate(1L, usd, BigDecimal.valueOf(1.0), "EUR", null),
                new ExchangeRate(2L, usd, BigDecimal.valueOf(0.89), "GBP", null)
        );

        when(currencyRepository.findAll()).thenReturn(currencies);
        when(exchangeRateRepository.findLatestRatesByCurrencyCode("USD")).thenReturn(exchangeRates);

        currencyExchangeService.loadCacheFromDatabase();

        Map<String, BigDecimal> cachedRates = currencyExchangeService.getExchangeRatesForCurrency("USD");

        assertNotNull(cachedRates);
        assertEquals(BigDecimal.valueOf(1.0), cachedRates.get("EUR"));
        assertEquals(BigDecimal.valueOf(0.89), cachedRates.get("GBP"));
        verify(currencyRepository, times(1)).findAll();
        verify(exchangeRateRepository, times(1)).findLatestRatesByCurrencyCode("USD");
    }
}
