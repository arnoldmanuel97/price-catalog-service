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
