package com.test.currencyexchange.controller;

import com.test.currencyexchange.dto.CurrencyListDTO;
import com.test.currencyexchange.dto.ExchangeRateDTO;
import com.test.currencyexchange.service.CurrencyExchangeService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyExchangeService currencyExchangeService;

    public CurrencyController(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @GetMapping("/list")
    public CurrencyListDTO getCurrencies() {
        List<String> currencies = currencyExchangeService.getCurrencies();
        return new CurrencyListDTO(currencies);
    }

    @GetMapping("/rates/{currencyCode}")
    public ExchangeRateDTO getExchangeRates(@PathVariable String currencyCode) {
        Map<String, BigDecimal> rates = currencyExchangeService.getExchangeRatesForCurrency(currencyCode);
        return new ExchangeRateDTO(currencyCode, rates);
    }

    @PostMapping("/add")
    public void addCurrency(@RequestParam String currencyCode) {
        currencyExchangeService.addNewCurrency(currencyCode);
    }
}
