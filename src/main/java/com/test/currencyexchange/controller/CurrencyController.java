package com.test.currencyexchange.controller;

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
    public List<String> getCurrencies() {
        return currencyExchangeService.getCurrencies();
    }

    @GetMapping("/rates/{currencyCode}")
    public Map<String, BigDecimal> getExchangeRates(@PathVariable String currencyCode) {
        return currencyExchangeService.getExchangeRatesForCurrency(currencyCode);
    }

    @PostMapping("/add")
    public void addCurrency(@RequestParam String currencyCode) {
        currencyExchangeService.addNewCurrency(currencyCode);
    }
}
