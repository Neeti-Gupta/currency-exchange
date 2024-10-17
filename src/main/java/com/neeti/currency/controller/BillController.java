package com.neeti.currency.controller;

import com.neeti.currency.model.Bill;
import com.neeti.currency.service.CurrencyExchangeService;
import com.neeti.currency.service.DiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api")
@Slf4j
public class BillController {

    @Autowired private CurrencyExchangeService currencyExchangeService;
    @Autowired private DiscountService discountService;


    @PostMapping("/calculate")
    public ResponseEntity<?> calculatePayableAmount(@RequestBody Bill bill) {
        double exchangeRate = currencyExchangeService.getExchangeRates(bill.getOriginalCurrency(), bill.getTargetCurrency());
        double discountedAmount = discountService.calculateDiscount(bill);
        double payableAmount = discountedAmount * exchangeRate;
        log.info("Payable amount in {} is {}", bill.getTargetCurrency(),payableAmount);
        return ResponseEntity.ok(Collections.singletonMap("payableAmount", payableAmount));
    }
}
