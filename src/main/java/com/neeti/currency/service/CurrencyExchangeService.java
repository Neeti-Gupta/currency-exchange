package com.neeti.currency.service;

import com.neeti.currency.exception.CurrencyNotFoundException;
import com.neeti.currency.model.CurrencyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class CurrencyExchangeService {

    public static final String url = "https://v6.exchangerate-api.com/v6/%s/latest/%s";

    @Value("${exchange.api.key}")
    private String apiKey;

    // Cache the result of this method
    @Cacheable(value = "exchangeRates", key ="#baseCurrency + '_' + #targetCurrency")
    public double getExchangeRates(String baseCurrency, String targetCurrency) {
        log.info("Fetching exchange rates from {} to {}", baseCurrency, targetCurrency);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CurrencyResponse> currencyResponse = restTemplate
                .getForEntity(String.format(url, apiKey, baseCurrency), CurrencyResponse.class);
        if (currencyResponse.getStatusCode().is2xxSuccessful() && Objects.nonNull(currencyResponse.getBody())) {
            log.debug("Received response from currency exchange API: {}", currencyResponse.getBody());
            Map<String,Double> conversionRates = currencyResponse.getBody().getConversionRates();
            if (!conversionRates.containsKey(targetCurrency)){
                log.error("Currency {} not found in response", targetCurrency);
                throw new CurrencyNotFoundException("Currency " + targetCurrency + " not found.");
            }
            else {
                Double exchangeRate = conversionRates.get(targetCurrency);
                log.info("Exchange rate for {} to {} is {}", baseCurrency, targetCurrency, exchangeRate);
                return exchangeRate;
            }
        }
        else {
            log.error("Error fetching exchange rate. Status: {}", currencyResponse.getStatusCode());
            throw new RuntimeException("Failed to fetch exchange rates");
        }
    }

}
