package co.com.calc.usecase;

import co.com.calc.model.User;
import co.com.calc.model.gateway.UserRepository;
import co.com.calc.usecase.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserUseCase.class);

    public Mono<User> register(String username, String password, String email) {
        return userRepository.findByUsername(username)
                .flatMap(existingUser -> Mono.<User>error(new UserAlreadyExistsException("Username already exists")))
                .switchIfEmpty(userRepository.findByEmail(email)
                        .flatMap(existingUser -> Mono.<User>error(new UserAlreadyExistsException("Email already exists")))
                        .switchIfEmpty(Mono.defer(() -> {
                            User user = new User();
                            user.setUsername(username);
                            user.setPassword(encodePassword(password));
                            user.setEmail(email);
                            user.setCreatedAt(LocalDateTime.now());
                            return userRepository.save(user)
                                    .doOnError(e -> logger.error("Error saving user with username: {} and email: {}", username, email, e));
                        })));
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

}
