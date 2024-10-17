package com.neeti.currency.service;

import com.neeti.currency.model.Bill;
import com.neeti.currency.model.Item;
import com.neeti.currency.model.User;
import com.neeti.currency.model.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class DiscountService {

    public double calculateDiscount(Bill bill) {
        double totalAmount = bill.getTotalAmount();
        User user = bill.getUser();
        List<Item> items = bill.getItems();
        log.info("Calculating discount for user of type {} with total amount {}", user.getUserType(), totalAmount);
        double percentageDiscount = 0;
        if (isNonGroceryItems(items)) {
            if (UserType.employee.name().equals(user.getUserType())) {
                log.debug("Applying 30% employee discount");
                percentageDiscount = 0.30;
            } else if (UserType.affiliate.name().equals(user.getUserType())) {
                log.debug("Applying 10% affiliate discount");
                percentageDiscount = 0.10;
            } else if (isCustomerForMoreThanTwoYears(user)) {
                log.debug("Applying 5% loyalty discount for customer over 2 years");
                percentageDiscount = 0.05;
            }
        }
        else{
            log.info("No percentage discount applied as items are grocery");
        }
        double totalPercentageDiscount = totalAmount * percentageDiscount;
        double flatDiscount = (int) (totalAmount / 100) * 5;
        double finalAmount = totalAmount - totalPercentageDiscount - flatDiscount;
        log.info("Final discounted amount is {}", finalAmount);
       return finalAmount;
    }

    private boolean isNonGroceryItems(List<Item> items) {
        return items.stream().anyMatch(item -> !"grocery".equals(item.getCategory()));
    }

    private boolean isCustomerForMoreThanTwoYears(User user) {
        return user.getCustomerSince().isBefore(LocalDate.now().minusYears(2));
    }
}
