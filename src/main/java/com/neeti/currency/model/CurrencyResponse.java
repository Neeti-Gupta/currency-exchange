package com.neeti.currency.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CurrencyResponse {
    @JsonProperty("conversion_rates")
    private Map<String,Double> conversionRates;
}
