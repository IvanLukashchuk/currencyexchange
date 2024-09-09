package com.test.currencyexchange.external;

import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeRateProvider {
    Map<String, BigDecimal> fetchRates(String currencyCode);
}
