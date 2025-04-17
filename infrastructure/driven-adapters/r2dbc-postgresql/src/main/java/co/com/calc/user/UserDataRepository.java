package co.com.calc.user;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface UserDataRepository extends ReactiveCrudRepository<UserData, UUID>, ReactiveQueryByExampleExecutor<UserData> {

    UserData findByusername(String username);
    UserData findByEmail(String email);
}

