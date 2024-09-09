package com.test.currencyexchange.scheduler;

import com.test.currencyexchange.service.CurrencyExchangeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateScheduler {

    private final CurrencyExchangeService currencyExchangeService;

    public ExchangeRateScheduler(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @Scheduled(cron = "${currency.fetch.cron}")
    public void updateExchangeRates() {
        currencyExchangeService.getCurrencies().forEach(currencyExchangeService::fetchAndUpdateRates);
    }
}
