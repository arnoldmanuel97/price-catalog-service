package com.arnoldmanuel.pricecatalogservice.acceptance.steps;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;

import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;
import com.arnoldmanuel.pricecatalogservice.domain.Brand;
import com.arnoldmanuel.pricecatalogservice.domain.PriceCalculation;
import com.arnoldmanuel.pricecatalogservice.domain.Product;
import com.arnoldmanuel.pricecatalogservice.usecase.GetPriceUseCase;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PriceCalculationSteps {

    @Autowired
    private GetPriceUseCase getPriceUseCase;

    private Long productId;
    private Long brandId;
    private LocalDateTime requestDateTime;
    private Optional<ApplicablePriceDomain> resultPrice;

    @Given("a product with id {long} from brand {long}")
    public void aProductWithIdFromBrand(Long productId, Long brandId) {
        this.productId = productId;
        this.brandId = brandId;
    }

    @When("^I request the price for date and time (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$")
    public void iRequestThePriceForDateAndTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        requestDateTime = LocalDateTime.parse(dateTime, formatter);
        var priceCalculation = new PriceCalculation(requestDateTime, new Brand(brandId), new Product(productId));
        resultPrice = getPriceUseCase.getPrice(priceCalculation);
    }

    @Then("a price should be found")
    public void aPriceShouldBeFound() {
        assertTrue(resultPrice.isPresent(), "Expected to find a price, but none was found");
    }

    @Then("no price should be found")
    public void noPriceShouldBeFound() {
        assertFalse(resultPrice.isPresent(), "Expected no price to be found, but one was found");
    }

    @Then("the price list applied should be {int}")
    public void thePriceListAppliedShouldBe(Integer priceList) {
        assertTrue(resultPrice.isPresent(), "No price was found");
        assertEquals(priceList, resultPrice.get().priceRate().priceList());
    }

    @Then("the final price should be {bigdecimal}")
    public void theFinalPriceShouldBe(BigDecimal finalPrice) {
        assertTrue(resultPrice.isPresent(), "No price was found");
        assertEquals(finalPrice, resultPrice.get().priceRate().price());
    }

    @Then("^the price should be valid from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$")
    public void thePriceShouldBeValidFromTo(String startDateTime, String endDateTime) {
        assertTrue(resultPrice.isPresent(), "No price was found");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);
        assertEquals(start, resultPrice.get().priceRate().dateRange().startDate());
        assertEquals(end, resultPrice.get().priceRate().dateRange().endDate());
    }
}
