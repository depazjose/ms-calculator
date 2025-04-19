package co.com.calc.api.controller;

import co.com.calc.api.controller.request.LoginRequest;
import co.com.calc.api.controller.request.RegisterRequest;
import co.com.calc.auth.AuthService;
import co.com.calc.model.User;
import co.com.calc.usecase.UserUseCase;
import co.com.calc.usecase.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRegister {
    private final UserUseCase userUseCase;
    private final AuthService authRegister;

    @PostMapping("/register")
    public Mono<ResponseEntity<User>> register(@Valid @RequestBody RegisterRequest request) {
        return register(request.getUsername(), request.getPassword(), request.getEmail())
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user))
                .onErrorResume(e -> {
                    if (e instanceof UserAlreadyExistsException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    public Mono<User> register(String username, String password, String email) {
        return userUseCase.register(username, password, email);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, String>>> login(@Valid @RequestBody LoginRequest request) {
        return login(request.getUsername(), request.getPassword())
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    public Mono<Map<String, String>> login(String username, String password) {
        return authRegister.login(username, password);
    }
}
