  README - Servicio de Catálogo de Precios

Servicio de Catálogo de Precios
===============================

Descripción General
-------------------

El Servicio de Catálogo de Precios es una aplicación Spring Boot diseñada para gestionar y recuperar información de precios de productos basada en varios criterios como fecha, hora, marca e ID del producto. El servicio soporta APIs RESTful para consultar precios e integra una base de datos para almacenamiento persistente.

Características
---------------

*   Recuperar precios aplicables para productos basados en fecha y hora.
*   Soporte para múltiples marcas y productos.
*   Integración con una base de datos relacional.
*   Pruebas unitarias y de integración completas.
*   Pruebas de aceptación usando Cucumber.

Prerrequisitos
--------------

*   Java 17 o superior
*   Maven 3.6.0 o superior
*   Docker (opcional, para ejecutar la base de datos)

Comenzando
----------

### Clonar el Repositorio

    git clone https://github.com/tuusuario/price-catalog-service.git
    cd price-catalog-service
    

### Construir el Proyecto

    mvn clean install
    

### Ejecutar la Aplicación

    mvn spring-boot:run
    

### Ejecutar Pruebas

#### Pruebas Unitarias y de Integración

    mvn test
    

#### Pruebas de Aceptación

    mvn verify -Pacceptance-tests
    

Estructura del Proyecto
-----------------------

    price-catalog-service/
    ├── price-catalog-domain/          # Capa de dominio
    ├── price-catalog-infra-input-rest/ # Capa de API REST
    ├── price-catalog-infra-output-h2/  # Capa de integración con la base de datos
    ├── price-catalog-boot/             # Aplicación Spring Boot
    ├── price-catalog-acceptance-tests/ # Pruebas de aceptación
    └── README.md                       # Documentación del proyecto
    

Componentes Clave
-----------------

### Capa de Dominio

Define la lógica de negocio y las entidades del dominio.

#### Ejemplo: `DateRange.java`

    package com.arnoldmanuel.pricecatalogservice.domain;
    
    import com.arnoldmanuel.pricecatalogservice.exceptions.DomainException;
    import jakarta.validation.constraints.NotNull;
    
    import java.time.LocalDateTime;
    
    public record DateRange(@NotNull LocalDateTime startDate, @NotNull LocalDateTime endDate) {
        public DateRange {
            if (startDate.isAfter(endDate)) {
                throw new DomainException("startDate debe ser antes o igual a endDate");
            }
        }
    }
    

### Capa de API REST

Maneja las solicitudes y respuestas HTTP.

#### Ejemplo: `PriceApiDelegateImpl.java`

    package com.arnoldmanuel.pricecatalogservice.infrastructure.api;
    
    import com.arnoldmanuel.pricecatalogservice.infrastructure.api.api.PriceApiDelegate;
    import com.arnoldmanuel.pricecatalogservice.infrastructure.api.model.PriceDTO;
    import com.arnoldmanuel.pricecatalogservice.infrastructure.exceptions.PriceNotFoundException;
    import com.arnoldmanuel.pricecatalogservice.infrastructure.mapper.PriceDtoMapper;
    import com.arnoldmanuel.pricecatalogservice.usecase.GetPriceUseCase;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Component;
    
    @Component
    public class PriceApiDelegateImpl implements PriceApiDelegate {
    
        private final GetPriceUseCase getPriceUseCase;
        private final PriceDtoMapper priceDtoMapper;
    
        public PriceApiDelegateImpl(GetPriceUseCase getPriceUseCase, PriceDtoMapper priceDtoMapper) {
            this.getPriceUseCase = getPriceUseCase;
            this.priceDtoMapper = priceDtoMapper;
        }
    
        @Override
        public ResponseEntity getApplicablePrice(Long brandId, Long productId, String date) {
            return ResponseEntity.ok(
                    priceDtoMapper.fromDomain(
                            getPriceUseCase.getPrice(priceDtoMapper.toDomain(brandId, productId, date))
                            .orElseThrow(() -> new PriceNotFoundException("No se encontró precio aplicable"))
                    )
            );
        }
    }
    

### Capa de Integración con la Base de Datos

Gestiona las interacciones con la base de datos usando JPA.

#### Ejemplo: `PriceRepositoryAdapterIT.java`

    package com.arnoldmanuel.pricecatalogservice.infrastructure.persistence;
    
    import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.context.SpringBootTest;
    
    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.Optional;
    
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertTrue;
    
    @SpringBootTest
    class PriceRepositoryAdapterIT {
    
        @Autowired
        private PriceRepositoryAdapter priceRepository;
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
        @Test
        void whenFindApplicablePriceCalledWithExistingDataThenPriceIsReturned() {
            long productId = 35455L;
            long brandId = 1L;
            LocalDateTime appliedDate = LocalDateTime.parse("2020-06-14 10:00:00", formatter);
    
            Optional result = priceRepository.findApplicablePrice(productId, brandId, appliedDate);
            assertTrue(result.isPresent());
            assertEquals(brandId, result.get().brand().id());
            assertEquals(productId, result.get().product().id());
            assertEquals(new BigDecimal("35.50"), result.get().priceRate().price());
        }
    
        @Test
        void whenFindApplicablePriceCalledWithNonExistingDataThenNoPriceIsReturned() {
            long productId = 35455L;
            long brandId = 1L;
            LocalDateTime appliedDate = LocalDateTime.parse("2021-01-01 10:00:00", formatter);
    
            Optional result = priceRepository.findApplicablePrice(productId, brandId, appliedDate);
    
            assertTrue(result.isEmpty());
        }
    }
    

### Pruebas de Aceptación

Usa Cucumber para definir y ejecutar pruebas de aceptación.

#### Ejemplo: `price_calculation.feature`

    Feature: Price Calculation
    
      Scenario Outline: Calculate price for a product at a specific date and time
        Given a product with id  from brand 
        When I request the price for date and time 
        Then a price should be found
        And the price list applied should be 
        And the final price should be 
        And the price should be valid from  to 
    
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
    

#### Ejemplo: `PriceCalculationSteps.java`

    package com.arnoldmanuel.pricecatalogservice.acceptance.steps;
    
    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.Optional;
    import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertFalse;
    import static org.junit.jupiter.api.Assertions.assertTrue;
    import org.springframework.beans.factory.annotation.Autowired;
    
    import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;
    import com.arnoldmanuel.pricecatalogservice.domain.Brand;
    import com.arnoldmanuel.pricecatalogservice.domain.PriceCalculation;
    import com.arnoldmanuel.pricecatalogservice.domain.Product;
    import com.arnoldmanuel.pricecatalogservice.usecase.GetPriceUseCase;
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import io.cucumber.java.en.Given;
    import io.cucumber.java.en.Then;
    import io.cucumber.java.en.When;
    public class PriceCalculationSteps {
    public class PriceCalculationSteps {
        @Autowired
        @Autowired
        private GetPriceUseCase getPriceUseCase;
        private Long productId;
        private Long productId;
        private Long brandId;
        private LocalDateTime requestDateTime;
        private Optional resultPrice;
        @Given("a product with id {long} from brand {long}")
        @Given("a product with id {long} from brand {long}")
        public void aProductWithIdFromBrand(Long productId, Long brandId) {
            this.productId = productId;
            this.brandId = brandId;
        }
        @When("I request the price for date {string} and time {string}")
        @When("^I request the price for date and time (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$")
        public void iRequestThePriceForDateAndTime(String dateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            requestDateTime = LocalDateTime.parse(dateTime, formatter);
            var priceCalculation = new PriceCalculation(requestDateTime, new Brand(brandId), new Product(productId));
            resultPrice = getPriceUseCase.getPrice(priceCalculation);
        }
        @Then("a price should be found")
        @Then("a price should be found")
        public void aPriceShouldBeFound() {
            assertTrue(resultPrice.isPresent(), "Expected to find a price, but none was found");
        }
        @Then("no price should be found")
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
        @Then("the final price should be {bigdecimal}")
        public void theFinalPriceShouldBe(BigDecimal finalPrice) {
            assertTrue(resultPrice.isPresent(), "No price was found");
            assertEquals(finalPrice, resultPrice.get().priceRate().price());
        }
        @Then("the price should be valid from {string} to {string}")
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
    

Endpoints de la API
-------------------

### Obtener Precio Aplicable

*   **URL:** `/v1/brands/{brandId}/products/{productId}/prices`
*   **Método:** `GET`
*   **Parámetros:**
    *   `brandId` (path) - ID de la marca
    *   `productId` (path) - ID del producto
    *   `date` (query) - Fecha y hora para la cual se solicita el precio (formato: `yyyy-MM-dd HH:mm:ss`)
*   **Respuesta:**
    *   `200 OK` - Devuelve el precio aplicable
    *   `404 Not Found` - Si no se encuentra un precio aplicable
    *   `400 Bad Request` - Si el formato de la fecha es inválido

#### Ejemplo de Solicitud

    curl -X GET "http://localhost:8080/v1/brands/1/products/35455/prices?date=2020-06-14%2010:00:00" -H "accept: application/json"
    

Ejecutar con Docker
-------------------

Puedes ejecutar la aplicación junto con una base de datos usando Docker.

### Crear Imágenes Docker

    docker-compose build
    

### Iniciar los Servicios

    docker-compose up
    

### Detener los Servicios

    docker-compose down
    

### Dockerfile

    FROM eclipse-temurin:21-jdk-alpine AS build
    
    COPY .mvn/ .mvn/
    COPY mvnw pom.xml ./
    
    RUN chmod +x mvnw
    
    COPY price-catalog-domain/ ./price-catalog-domain/
    COPY price-catalog-application/ ./price-catalog-application/
    COPY price-catalog-infra-output-h2/ ./price-catalog-infra-output-h2/
    COPY price-catalog-infra-input-rest/ ./price-catalog-infra-input-rest/
    COPY price-catalog-acceptance-tests/ ./price-catalog-acceptance-tests/
    COPY price-catalog-boot/ ./price-catalog-boot/
    
    RUN ./mvnw clean package -DskipTests --debug
    
    FROM eclipse-temurin:21-jre-alpine
    WORKDIR /app
    COPY --from=build /price-catalog-boot/target/*.jar app.jar
    EXPOSE 8080
    ENTRYPOINT ["java", "-jar", "/app/app.jar"]
    

### docker-compose.yml

    version: '3.8'
    services:
      app:
        build: .
        ports:
          - "8080:8080"
        environment:
          - SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
          - SPRING_DATASOURCE_DRIVER-CLASS-NAME=org.h2.Driver
          - SPRING_DATASOURCE_USERNAME=sa
          - SPRING_DATASOURCE_PASSWORD=password
        healthcheck:
          test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
          interval: 30s
          timeout: 10s
          retries: 3
      db:
        image: eclipse-temurin:21-jre-alpine
        command: java -cp /h2/h2-2.1.214.jar org.h2.tools.Server -tcp -ifNotExists
        volumes:
          - ./h