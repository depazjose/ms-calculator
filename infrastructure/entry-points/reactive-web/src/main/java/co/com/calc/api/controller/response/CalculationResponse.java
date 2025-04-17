package co.com.calc.api.controller.response;

import co.com.calc.model.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculationResponse {

    private UUID id;
    private OperationType operation;
    private Double operandA;
    private Double operandB;
    private Double result;
    private LocalDateTime timestamp;
    private UUID userId;
    private ErrorInfo error; // Field to hold error information

    // Constructor for successful response
    public CalculationResponse(UUID id, OperationType operation, Double operandA, Double operandB, Double result, LocalDateTime timestamp, UUID userId) {
        this.id = id;
        this.operation = operation;
        this.operandA = operandA;
        this.operandB = operandB;
        this.result = result;
        this.timestamp = timestamp;
        this.userId = userId;
        this.error = null; // No error in a successful response
    }

    // Constructor for error response
    public CalculationResponse(String message, int status) {
        this.id = null;
        this.operation = null;
        this.operandA = null;
        this.operandB = null;
        this.result = null;
        this.timestamp = null;
        this.userId = null;
        this.error = new ErrorInfo(message, status, Collections.emptyList());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorInfo {
        private String message;
        private int status;
        private List<String> details; // Optional details for the error

    }

    public void setErrorDetails(List<String> details) {
        if (this.error != null) {
            this.error.setDetails(details);
        }
    }
}