package com.test.currencyexchange.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ExchangeRateApiProvider implements ExchangeRateProvider {

    private final RestTemplate restTemplate;

    public ExchangeRateApiProvider() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Map<String, BigDecimal> fetchRates(String currencyCode) {
        String url = String.format("https://api.exchangerate-api.com/v4/latest/%s", currencyCode);
        try {
            ExchangeRateApiResponse response = restTemplate.getForObject(url, ExchangeRateApiResponse.class);
            Map<String, BigDecimal> rates = new HashMap<>();
            if (response != null && response.rates() != null) {
                response.rates().forEach((key, value) -> rates.put(key, BigDecimal.valueOf(value)));
                log.info("Successfully fetched rates for currency: {}", currencyCode);
            }
            return rates;
        } catch (HttpStatusCodeException e) {
            log.error("Failed to fetch rates for currency: {}. Status code: {}", currencyCode, e.getStatusCode());
        } catch (Exception e) {
            log.error("Error occurred while fetching rates for currency: {}", currencyCode, e.getMessage());
        }
        return new HashMap<>();
    }
}
