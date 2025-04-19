package co.com.calc.user;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserDataRepository extends ReactiveCrudRepository<UserData, UUID>, ReactiveQueryByExampleExecutor<UserData> {

    //@Query("SELECT * FROM tb_user WHERE username = :username")
    Mono<UserData> findByUsername(String username);
    Mono<UserData> findByemail(String email);
}

