package com.openclassrooms.medilabo.gatewayService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {
        "spring.cloud.gateway.routes[0].id=patient-service",
        "spring.cloud.gateway.routes[0].uri=http://localhost:8081",
        "spring.cloud.gateway.routes[0].predicates[0]=Path=/api/patients/**",
        "spring.cloud.gateway.routes[0].filters[0]=StripPrefix=1"
})
public class GatewayRoutingPatientTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldForwardRequestToPatientService() {
        webTestClient.get()
                .uri("/api/patients/1")
                .headers(headers -> headers.setBasicAuth("user", "password"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }
}
