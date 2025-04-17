package co.com.calc.api.controller.request;

import co.com.calc.model.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalculationRequest {

    @NotNull(message = "Operation type is required")
    private OperationType operation;

    @NotNull(message = "Operand A is required")
    private Double operandA;

    private Double operandB; // Optional for SQUARE_ROOT
}
