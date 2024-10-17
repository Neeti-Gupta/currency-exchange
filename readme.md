# Currency Exchange and Discount Calculation Application

This project is a Spring Boot application that integrates with a third-party currency exchange API to retrieve real-time exchange rates and calculate the total payable amount for a bill in a specified currency after applying applicable discounts.
Additionally, caching is implemented to reduce redundant API calls to the third-party service.

### Features
* Currency Exchange API Integration: Fetches real-time exchange rates from a third-party API (ExchangeRate -API).
* Discount Calculation: Applies various discount rules based on user type, tenure, and bill structure.
* Caching: Uses Spring’s caching mechanism to cache exchange rates and avoid repeated API calls.
* RESTful API: Provides an API endpoint to accept bill details, user details, and currency information to calculate the final payable amount.

### Technologies Used
* Java 8: The project is built using Java 8.
* Spring Boot: To simplify the application configuration and development.
* Spring Cache: For caching the exchange rates to optimize performance.
* RestTemplate: To make HTTP calls to the third-party currency exchange API.
* JUnit 5 & Mockito: For unit testing and mocking dependencies.

###  How It Works
The application works in two major steps:
1. Fetch Exchange Rate: The application fetches real-time currency exchange rates from a third-party API based on the provided base and target currencies.
2. Calculate Payable Amount: It calculates the final payable amount for a bill after applying applicable discounts based on user type, customer tenure, and bill amount.

### Discount Rules
The following discount rules are applied:
**User Type Discounts:**
* Employee: `30%` discount.
* Affiliate: `10%` discount.
* Loyal Customer: `5%` discount (if the customer has been with the store for more than 2 years).
* Flat Discounts: For every `$100` on the bill, a flat `$5` discount is applied.
* Grocery Exclusion: Percentage-based discounts do not apply to groceries.
* A user can only receive one of the percentage-based discounts.


### Caching

To optimize performance and reduce redundant API calls to the third-party currency exchange service,
Spring Cache is used to cache exchange rates.
This way, subsequent requests for the same currency pair (base and target currency) will use the cached rate instead of making another API call.

### API Endpoint
The application exposes a REST API to calculate the payable amount:
* Endpoint: /api/calculate
* Method: POST
* Request Body:
{
```
"items": [
    {
        "name": "item1",
        "category": "non-grocery",
        "price": 100.0
    }
],
"totalAmount": 100.0,
"originalCurrency": "USD",
"targetCurrency": "EUR",
"user": {
    "userType": "affiliate",
    "customerSince": "2015-01-01"
}
}
```

• Response:
```
{
"payableAmount": 95.0
}
```

###  How to Run the Application
1. Clone the repository:git clone [GitHub](https://github.com/Neeti-Gupta/currency-exchange)
2. Navigate to the project directory:cd your-repository
3. Run the application:./mvnw spring-boot:run
4. Access the API: Once the application is running, you can access the API via POST requests to /api/calculate.

### Code Structure
Here’s a breakdown of the important components:
* BillController: Handles the REST API requests for calculating the payable amount.
* CurrencyExchangeService: Fetches the exchange rates from the third-party API and caches the result.
* DiscountService: Applies the applicable discounts to the bill amount.
* CurrencyResponse: The model used to map the response from the third-party currency exchange API.
  
### Class Diagram
[Class Diagram](https://github.com/Neeti-Gupta/currency-exchange/blob/main/classdiagram.md)
