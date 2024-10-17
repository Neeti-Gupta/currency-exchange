package com.neeti.currency;

import com.neeti.currency.controller.BillController;
import com.neeti.currency.exception.CurrencyNotFoundException;
import com.neeti.currency.model.Bill;
import com.neeti.currency.service.CurrencyExchangeService;
import com.neeti.currency.service.DiscountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BillController.class)
class BillControllerTest {

	@Autowired private MockMvc mockMvc;
	@MockBean private CurrencyExchangeService currencyExchangeService;
	@MockBean private DiscountService discountService;

	private static final String url = "/api/calculate";

	private static final String billJson = "{"
			+ "\"items\": [{ \"name\": \"item1\", \"category\": \"non-grocery\", \"price\": 100.0 }],"
			+ "\"totalAmount\": 100.0,"
			+ "\"originalCurrency\": \"USD\","
			+ "\"targetCurrency\": \"EUR\","
			+ "\"user\": { \"userType\": \"affiliate\", \"customerSince\": \"2015-01-01\" }"
			+ "}";

		@Test
		public void testCalculatePayableAmount() throws Exception {
			// Mocking external services
			when(currencyExchangeService.getExchangeRates(any(String.class), any(String.class))).thenReturn(1.0);
			// Mock exchange rate
			when(discountService.calculateDiscount(any(Bill.class))).thenReturn(95.0);
			// Mock discount logic

			//Perform the POST request to the /api/calculate endpoint
			mockMvc.perform(post(url)
					.contentType(MediaType.APPLICATION_JSON).content(billJson))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.payableAmount").value(95.0)); // Expected payable amount
		}

	@Test public void testCurrencyNotFound() throws Exception {
		when(currencyExchangeService.getExchangeRates(anyString(), anyString()))
				.thenThrow(new CurrencyNotFoundException("Currency EUR not found."));
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
				.content(billJson)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorCode").value("CURRENCY_NOT_FOUND"))
				.andExpect(jsonPath("$.errorMessage").value("Currency EUR not found."));
	}


}
