package co.com.calc.model.gateway;

import co.com.calc.model.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
    Mono<User> save(User user);
    Mono<User> findByUsername(String username);
    Mono<User> findByEmail(String email);
    Mono<User> findById(UUID id);
}
