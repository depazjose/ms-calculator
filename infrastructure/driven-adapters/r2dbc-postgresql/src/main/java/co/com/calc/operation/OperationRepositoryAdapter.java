package co.com.calc.operation;

import co.com.calc.model.Operation;
import co.com.calc.model.enums.OperationType;
import co.com.calc.model.gateway.OperationRepository;
import co.com.calc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class OperationRepositoryAdapter extends ReactiveAdapterOperations<
        Operation,
        OperationData,
        UUID,
        OperationDataRepository
> implements OperationRepository {

    public OperationRepositoryAdapter(OperationDataRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Operation.class));
    }

    @Override
    public Flux<Operation> findByUserId(UUID userId, int limit, long offset, String sortField, String sortDirection, OperationType operationType, LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public Mono<Operation> findByIdAndUserId(UUID id, UUID userId) {
        return null;
    }

    @Override
    public Mono<Void> deleteByIdAndUserId(UUID id, UUID userId) {
        return null;
    }
}
