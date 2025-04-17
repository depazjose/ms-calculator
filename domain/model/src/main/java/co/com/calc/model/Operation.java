package co.com.calc.model;

import co.com.calc.model.enums.OperationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Operation {
    private UUID id;
    private UUID userId;
    private OperationType operation;
    private BigDecimal operandA;
    private BigDecimal operandB;
    private BigDecimal result;
    private LocalDateTime timestamp;
}
