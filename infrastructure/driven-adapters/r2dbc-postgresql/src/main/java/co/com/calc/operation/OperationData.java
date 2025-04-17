package co.com.calc.operation;


import co.com.calc.model.enums.OperationType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Table("operation")
public class OperationData {
    @Id
    private UUID id;
    private UUID userId;
    private OperationType operation;
    private BigDecimal operandA;
    private BigDecimal operandB;
    private BigDecimal result;
    private LocalDateTime timestamp;
}