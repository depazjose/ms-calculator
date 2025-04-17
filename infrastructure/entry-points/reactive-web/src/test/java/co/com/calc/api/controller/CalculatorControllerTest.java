package co.com.calc.api.controller;

import co.com.calc.api.config.TestSecurityConfig;
import co.com.calc.api.controller.request.CalculationRequest;
import co.com.calc.api.controller.response.CalculationResponse;
import co.com.calc.model.Operation;
import co.com.calc.model.enums.OperationType;
import co.com.calc.usecase.CalculatorUseCase;
import co.com.calc.usecase.exception.InvalidOperationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest
@ContextConfiguration(classes = {Calculator.class, CalculatorUseCase.class}) // If CalculatorUseCase has no external dependencies for this test
@Import(TestSecurityConfig.class)
class CalculatorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CalculatorUseCase calculatorUseCase;

    private final UUID testUserId = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");

    private void mockAuthentication(UUID userId) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId.toString(), "password", List.of());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    @WithMockUser(username = "a1b2c3d4-e5f6-7890-1234-567890abcdef", roles = "USER")
    void calculate_validRequest_returnsOkAndCalculationResponse() {
        // Arrange
        CalculationRequest request = new CalculationRequest();
        request.setOperation(OperationType.ADDITION);
        request.setOperandA(5.0);
        request.setOperandB(3.0);

        Operation mockOperation = new Operation();
        mockOperation.setId(UUID.randomUUID());
        mockOperation.setOperation(OperationType.ADDITION);
        mockOperation.setOperandA(BigDecimal.valueOf(request.getOperandA()));
        mockOperation.setOperandB(BigDecimal.valueOf(request.getOperandB() != null ? request.getOperandB().doubleValue() : null));
        mockOperation.setResult(BigDecimal.valueOf(8.0));
        mockOperation.setTimestamp(LocalDateTime.now());
        mockOperation.setUserId(testUserId);

        when(calculatorUseCase.calculate(testUserId, request.getOperation(), request.getOperandA(), request.getOperandB()))
                .thenReturn(Mono.just(mockOperation));

        // Act & Assert
        webTestClient.mutateWith(csrf()) // If CSRF is enabled
                .post().uri("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CalculationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CalculationResponse.class)
                .consumeWith(response -> {
                    CalculationResponse calculationResponse = response.getResponseBody();
                    assert calculationResponse != null;
                    assert calculationResponse.getOperation() == OperationType.ADDITION;
                    assert calculationResponse.getOperandA().equals(5.0);
                    assert calculationResponse.getOperandB().equals(3.0);
                    assert calculationResponse.getResult().equals(8.0);
                    assert calculationResponse.getUserId().equals(testUserId);
                });

        Mockito.verify(calculatorUseCase, Mockito.times(1))
                .calculate(eq(testUserId), eq(request.getOperation()), eq(request.getOperandA()), eq(request.getOperandB()));
    }

    @Test
    @WithMockUser(username = "a1b2c3d4-e5f6-7890-1234-567890abcdef", roles = "USER")
    void calculate_invalidOperation_returnsBadRequestAndCalculationResponseWithError() {
        // Arrange
        CalculationRequest request = new CalculationRequest();
        request.setOperation(OperationType.DIVISION);
        request.setOperandA(10.0);
        request.setOperandB(0.0);

        String errorMessage = "Division by zero is not allowed";
        when(calculatorUseCase.calculate(eq(testUserId), eq(request.getOperation()), eq(request.getOperandA()), eq(request.getOperandB())))
                .thenReturn(Mono.error(new InvalidOperationException(errorMessage)));

        // Act & Assert
        webTestClient.mutateWith(csrf()) // If CSRF is enabled
                .post().uri("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CalculationRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CalculationResponse.class)
                .consumeWith(response -> {
                    CalculationResponse calculationResponse = response.getResponseBody();
                    assert calculationResponse != null;
                    assert calculationResponse.getError() != null;
                    assert calculationResponse.getError().getMessage().equals(errorMessage);
                    assert calculationResponse.getError().getStatus() == HttpStatus.BAD_REQUEST.value();
                });

        Mockito.verify(calculatorUseCase, Mockito.times(1))
                .calculate(eq(testUserId), eq(request.getOperation()), eq(request.getOperandA()), eq(request.getOperandB()));
    }

    @Test
    @WithMockUser(username = "a1b2c3d4-e5f6-7890-1234-567890abcdef", roles = "USER")
    void calculate_internalServerError_returnsInternalServerErrorAndCalculationResponseWithError() {
        // Arrange
        CalculationRequest request = new CalculationRequest();
        request.setOperation(OperationType.POWER);
        request.setOperandA(2.0);
        request.setOperandB(10.0);

        String errorMessage = "Simulated internal server error";
        when(calculatorUseCase.calculate(eq(testUserId), eq(request.getOperation()), eq(request.getOperandA()), eq(request.getOperandB())))
                .thenReturn(Mono.error(new RuntimeException(errorMessage)));

        // Act & Assert
        webTestClient.mutateWith(csrf()) // If CSRF is enabled
                .post().uri("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CalculationRequest.class)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CalculationResponse.class)
                .consumeWith(response -> {
                    CalculationResponse calculationResponse = response.getResponseBody();
                    assert calculationResponse != null;
                    assert calculationResponse.getError() != null;
                    assert calculationResponse.getError().getMessage().equals("Internal Server Error");
                    assert calculationResponse.getError().getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value();
                });

        Mockito.verify(calculatorUseCase, Mockito.times(1))
                .calculate(eq(testUserId), eq(request.getOperation()), eq(request.getOperandA()), eq(request.getOperandB()));
    }
}