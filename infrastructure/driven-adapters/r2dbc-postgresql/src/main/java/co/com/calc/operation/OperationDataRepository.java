package co.com.calc.operation;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface OperationDataRepository extends ReactiveCrudRepository<OperationData, UUID>, ReactiveQueryByExampleExecutor<OperationData> {

}
