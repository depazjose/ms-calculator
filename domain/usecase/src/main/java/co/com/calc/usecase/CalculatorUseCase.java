package co.com.calc.usecase;

import co.com.calc.model.Operation;
import co.com.calc.model.enums.OperationType;
import co.com.calc.model.gateway.OperationRepository;
import co.com.calc.usecase.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class CalculatorUseCase {

    private final OperationRepository operationRepository;

    public Mono<Operation> calculate(UUID userId, OperationType operationType, double operandA, Double operandB) {
        BigDecimal operandABigDecimal = BigDecimal.valueOf(operandA);
        BigDecimal operandBBigDecimal = operandB != null ? BigDecimal.valueOf(operandB) : null;
        BigDecimal result = null;

        if (operandABigDecimal.compareTo(new BigDecimal("-1000000")) < 0 || operandABigDecimal.compareTo(new BigDecimal("1000000")) > 0 ||
                (operandBBigDecimal != null && (operandBBigDecimal.compareTo(new BigDecimal("-1000000")) < 0 || operandBBigDecimal.compareTo(new BigDecimal("1000000")) > 0))) {
            return Mono.error(new InvalidOperationException("Operands must be within the range of -1000000 to 1000000"));
        }

        switch (operationType) {
            case ADDITION:
                result = operandABigDecimal.add(operandBBigDecimal).setScale(2, RoundingMode.HALF_UP);
                break;
            case SUBTRACTION:
                result = operandABigDecimal.subtract(operandBBigDecimal).setScale(2, RoundingMode.HALF_UP);
                break;
            case MULTIPLICATION:
                result = operandABigDecimal.multiply(operandBBigDecimal).setScale(2, RoundingMode.HALF_UP);
                break;
            case DIVISION:
                if (operandBBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
                    return Mono.error(new InvalidOperationException("Division by zero is not allowed"));
                }
                result = operandABigDecimal.divide(operandBBigDecimal, 2, RoundingMode.HALF_UP);
                break;
            case POWER:
                result = BigDecimal.valueOf(Math.pow(operandABigDecimal.doubleValue(), operandBBigDecimal.doubleValue())).setScale(2, RoundingMode.HALF_UP);
                break;
            case SQUARE_ROOT:
                if (operandABigDecimal.compareTo(BigDecimal.ZERO) < 0) {
                    return Mono.error(new InvalidOperationException("Cannot calculate square root of a negative number"));
                }
                result = BigDecimal.valueOf(Math.sqrt(operandABigDecimal.doubleValue())).setScale(2, RoundingMode.HALF_UP);
                break;
            default:
                return Mono.error(new InvalidOperationException("Invalid operation type"));
        }

        Operation operation = new Operation();
        operation.setUserId(userId);
        operation.setOperation(operationType);
        operation.setOperandA(operandABigDecimal);
        operation.setOperandB(operandBBigDecimal);
        operation.setResult(result);
        operation.setTimestamp(LocalDateTime.now());

        return operationRepository.save(operation);
    }
}
