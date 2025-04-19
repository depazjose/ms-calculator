package co.com.calc.operation;


import co.com.calc.model.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Table("tb_operation")
@AllArgsConstructor
@NoArgsConstructor
public class OperationData {
    @Id
    private UUID id;
    @Column("user_id")
    private UUID userId;
    @Column("operation")
    private OperationType operation;
    @Column("operand_a")
    private BigDecimal operandA;
    @Column("operand_b")
    private BigDecimal operandB;
    @Column("result")
    private BigDecimal result;
    @Column("created_at")
    private LocalDateTime timestamp;
}