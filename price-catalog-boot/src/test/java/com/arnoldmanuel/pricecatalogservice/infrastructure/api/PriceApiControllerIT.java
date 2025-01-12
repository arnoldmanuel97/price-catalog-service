package com.arnoldmanuel.pricecatalogservice.infrastructure.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PriceApiControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetPriceAt10June14ThenReturnsPriceDTO() throws Exception {
        long productId = 35455L;
        long brandId = 1L;
        String appliedDate = "2020-06-14 10:00:00";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(brandId))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.priceListId").value(1))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$.applicablePrice").value(35.50));
    }

    @Test
    void whenGetPriceAt16June14ThenReturnsPriceDTO() throws Exception {
        long productId = 35455L;
        long brandId = 1L;
        String appliedDate = "2020-06-14 16:00:00";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(brandId))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.priceListId").value(2))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-14T18:30:00"))
                .andExpect(jsonPath("$.applicablePrice").value(25.45));
    }

    @Test
    void whenGetPriceAt21June14ThenReturnsPriceDTO() throws Exception {
        long productId = 35455L;
        long brandId = 1L;
        String appliedDate = "2020-06-14 21:00:00";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(brandId))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.priceListId").value(1))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$.applicablePrice").value(35.50));
    }

    @Test
    void whenGetPriceAt10June15ThenReturnsPriceDTO() throws Exception {
        long productId = 35455L;
        long brandId = 1L;
        String appliedDate = "2020-06-15 10:00:00";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(brandId))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.priceListId").value(3))
                .andExpect(jsonPath("$.startDate").value("2020-06-15T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-15T11:00:00"))
                .andExpect(jsonPath("$.applicablePrice").value(30.50));
    }

    @Test
    void whenGetPriceAt21June16ThenReturnsPriceDTO() throws Exception {
        long productId = 35455L;
        long brandId = 1L;
        String appliedDate = "2020-06-16 21:00:00";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(brandId))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.priceListId").value(4))
                .andExpect(jsonPath("$.startDate").value("2020-06-15T16:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$.applicablePrice").value(38.95));
    }

    @Test
    void whenGetPriceWithDateOutOfRangeThenReturnsNotFound() throws Exception {
        long productId = 35455L;
        long brandId = 1L;
        String appliedDate = "2023-06-16 21:00:00";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    void whenGetPriceBrandIdNotExistsThenReturnsNotFound() throws Exception {
        long productId = 35455L;
        long brandId = 99L;
        String appliedDate = "2020-06-14 10:00:00";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .param("date", appliedDate))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    void whenGetPriceWithInvalidDateFormatThenReturnsBadRequest() throws Exception {
        long productId = 35455L;
        long brandId = 1L;
        String appliedDate = "fakeString";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @Test
    void whenGetPriceWithInvalidProductIdFormatThenReturnsBadRequest() throws Exception {
        long brandId = 1L;
        String appliedDate = "2020-06-14 10:00:00";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + "productId" + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @Test
    void whenGetPriceWithInvalidBrandIdFormatThenReturnsBadRequest() throws Exception {
        long productId = 35455L;
        String appliedDate = "2020-06-14 10:00:00";
        mockMvc.perform(get("/v1/brands/" + "brandId" + "/products/" + productId + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @Test
    void whenGetPriceWithInvalidBrandIdThenReturnsBadRequest() throws Exception {
        String productId = "35455";
        String brandId = "a";
        String appliedDate = "2020-06-14 10:00:00";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @Test
    void whenGetPriceWithInvalidDateThenReturnsBadRequest() throws Exception {
        long productId = 35455L;
        long brandId = 1L;
        String appliedDate = "22020-06-14%2010:00:00";
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .param("date", appliedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @Test
    void whenGetPriceWithoutDateThenReturnsBadRequest() throws Exception {
        long productId = 35455L;
        long brandId = 1L;
        mockMvc.perform(get("/v1/brands/" + brandId + "/products/" + productId + "/prices")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }
}
