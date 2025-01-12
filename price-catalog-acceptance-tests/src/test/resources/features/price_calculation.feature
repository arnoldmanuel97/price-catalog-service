Feature: Price Calculation

  Scenario Outline: Calculate price for a product at a specific date and time
    Given a product with id <productId> from brand <brandId>
    When I request the price for date and time <dateTime>
    Then a price should be found
    And the price list applied should be <priceList>
    And the final price should be <finalPrice>
    And the price should be valid from <startDateTime> to <endDateTime>

    Examples:
      | productId | brandId | dateTime            | priceList | finalPrice | startDateTime       | endDateTime         |
      | 35455     | 1       | 2020-06-14 10:00:00 | 1         | 35.50      | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 |
      | 35455     | 1       | 2020-06-14 16:00:00 | 2         | 25.45      | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 |
      | 35455     | 1       | 2020-06-14 21:00:00 | 1         | 35.50      | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 |
      | 35455     | 1       | 2020-06-15 10:00:00 | 3         | 30.50      | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 |
      | 35455     | 1       | 2020-06-16 21:00:00 | 4         | 38.95      | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 |

  Scenario: Request price for a non-existent product
    Given a product with id 99999 from brand 1
    When I request the price for date and time 2020-06-14 10:00:00
    Then no price should be found