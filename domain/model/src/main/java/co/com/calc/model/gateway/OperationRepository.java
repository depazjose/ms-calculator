package co.com.calc.model.gateway;

import co.com.calc.model.enums.OperationType;
import co.com.calc.model.Operation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OperationRepository {
    Mono<Operation> save(Operation operation);
    Flux<Operation> findByUserId(UUID userId, int limit, long offset, String sortField, String sortDirection, OperationType operationType, LocalDateTime startDate, LocalDateTime endDate);
    Mono<Operation> findByIdAndUserId(UUID id, UUID userId);
    Mono<Void> deleteByIdAndUserId(UUID id, UUID userId);
}