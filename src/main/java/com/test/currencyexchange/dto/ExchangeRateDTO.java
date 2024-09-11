package com.test.currencyexchange.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRateDTO(String currencyCode, Map<String, BigDecimal> rates) {}
