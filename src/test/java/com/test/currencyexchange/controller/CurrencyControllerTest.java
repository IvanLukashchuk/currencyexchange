package com.test.currencyexchange.controller;

import com.test.currencyexchange.service.CurrencyExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnListOfCurrencies() throws Exception {
        List<String> currencies = List.of("USD", "EUR");
        when(currencyExchangeService.getCurrencies()).thenReturn(currencies);

        mockMvc.perform(get("/api/currency/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("USD"))
                .andExpect(jsonPath("$[1]").value("EUR"));
    }

    @Test
    void shouldReturnExchangeRatesForCurrency() throws Exception {
        Map<String, BigDecimal> rates = Map.of("EUR", BigDecimal.valueOf(0.89));
        when(currencyExchangeService.getExchangeRatesForCurrency("USD")).thenReturn(rates);

        mockMvc.perform(get("/api/currency/rates/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.EUR").value(0.89));
    }

    @Test
    void shouldAddNewCurrency() throws Exception {
        mockMvc.perform(post("/api/currency/add?currencyCode=USD"))
                .andExpect(status().isOk());

        verify(currencyExchangeService, times(1)).addNewCurrency("USD");
    }
}
