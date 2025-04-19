package co.com.calc.api.controller;

import co.com.calc.api.config.TestSecurityConfig;
import co.com.calc.api.controller.request.LoginRequest;
import co.com.calc.api.controller.request.RegisterRequest;
import co.com.calc.auth.AuthService;
import co.com.calc.model.User;
import co.com.calc.usecase.UserUseCase;
import co.com.calc.usecase.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.Mockito.when;

@WebFluxTest(AuthRegister.class)
@ContextConfiguration(classes = {AuthRegister.class, UserUseCase.class})
@Import(TestSecurityConfig.class)
class AuthRegisterWebFluxTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private AuthService authService;

    @Test
    void register_successful() {

        RegisterRequest request = new RegisterRequest("testuser", "password", "test@example.com");
        User registeredUser = new User();
        registeredUser.setUsername("testuser");
        registeredUser.setPassword("encodedPassword");
        registeredUser.setEmail("test@example.com");
        when(userUseCase.register("testuser", "password", "test@example.com")).thenReturn(Mono.just(registeredUser));


        webTestClient.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RegisterRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class).isEqualTo(registeredUser);
    }

    @Test
    void register_userAlreadyExists() {

        RegisterRequest request = new RegisterRequest("existinguser", "password", "existing@example.com");
        when(userUseCase.register("existinguser", "password", "existing@example.com"))
                .thenReturn(Mono.error(new UserAlreadyExistsException("User already exists")));


        webTestClient.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RegisterRequest.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void register_internalServerError() {

        RegisterRequest request = new RegisterRequest("erroruser", "password", "error@example.com");
        when(userUseCase.register("erroruser", "password", "error@example.com"))
                .thenReturn(Mono.error(new RuntimeException("Database error")));


        webTestClient.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RegisterRequest.class)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void login_successful() {

        LoginRequest request = new LoginRequest("testuser", "password");
        Map<String, String> tokenResponse = Map.of("token", "mockedToken");
        when(authService.login("testuser", "password")).thenReturn(Mono.just(tokenResponse));


        webTestClient.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), LoginRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class).isEqualTo(tokenResponse);
    }

    @Test
    void login_unauthorized() {

        LoginRequest request = new LoginRequest("wronguser", "wrongpassword");
        when(authService.login("wronguser", "wrongpassword")).thenReturn(Mono.error(new RuntimeException("Authentication failed")));


        webTestClient.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), LoginRequest.class)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}

