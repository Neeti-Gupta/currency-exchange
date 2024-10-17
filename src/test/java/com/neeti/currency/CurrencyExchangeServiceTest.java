package com.neeti.currency;

import com.neeti.currency.service.CurrencyExchangeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CurrencyExchangeServiceTest {

    @Autowired private CurrencyExchangeService currencyExchangeService;
    @Autowired private CacheManager cacheManager;

    @Test
    public void testExchangeRateCaching() throws Exception {
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        // First call - should fetch from API and cache it
        double exchangeRateFirstCall = currencyExchangeService.getExchangeRates(baseCurrency, targetCurrency);
        // Second call - should return from cache
        double exchangeRateSecondCall = currencyExchangeService.getExchangeRates(baseCurrency, targetCurrency);
        // Assert that the values are the same (cached value)
        assertThat(exchangeRateFirstCall).isEqualTo(exchangeRateSecondCall);
        // Assert that cache contains the value
        assertThat(cacheManager.getCache("exchangeRates")
                .get(baseCurrency + "_" + targetCurrency)).isNotNull();
    }

}
