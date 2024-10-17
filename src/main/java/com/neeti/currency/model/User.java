package com.neeti.currency.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class User {
    private String userType;
    private LocalDate customerSince;
}
