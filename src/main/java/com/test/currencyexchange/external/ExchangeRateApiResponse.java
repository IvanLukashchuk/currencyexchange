package com.test.currencyexchange.external;

import java.util.Map;

public record ExchangeRateApiResponse(
        String base,
        Map<String, Double> rates
){}