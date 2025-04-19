package co.com.calc.api.controller;

import co.com.calc.api.controller.request.CalculationRequest;
import co.com.calc.api.controller.response.CalculationResponse;
import co.com.calc.model.Operation;
import co.com.calc.model.enums.OperationType;
import co.com.calc.usecase.CalculatorUseCase;
import co.com.calc.usecase.exception.InvalidOperationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/calculate")
@RequiredArgsConstructor
public class Calculator {

    private final CalculatorUseCase calculatorUseCase;

    @PostMapping
    public Mono<ResponseEntity<CalculationResponse>> calculate(
            @Valid @RequestBody CalculationRequest request,
            Mono<Authentication> authenticationMono
    ) {

        return authenticationMono
                .map(auth -> UUID.fromString(auth.getName()))
                .flatMap(userId -> calculate(userId, request.getOperation(), request.getOperandA(), request.getOperandB()))
                .map(operation -> new CalculationResponse(
                        operation.getId(),
                        operation.getOperation(),
                        operation.getOperandA().doubleValue(),
                        operation.getOperandB() != null ? operation.getOperandB().doubleValue() : null,
                        operation.getResult().doubleValue(),
                        operation.getTimestamp(),
                        operation.getUserId()
                ))
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .onErrorResume(e -> {
                    if (e instanceof InvalidOperationException) {
                        CalculationResponse errorResponse = new CalculationResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
                        errorResponse.setErrorDetails(List.of(e.getMessage()));
                        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            new CalculationResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value())
                    ));
                });
    }

    public Mono<Operation> calculate(UUID userId, OperationType operation, double operandA, Double operandB) {
        return calculatorUseCase.calculate(userId, operation, operandA, operandB);
    }
}
