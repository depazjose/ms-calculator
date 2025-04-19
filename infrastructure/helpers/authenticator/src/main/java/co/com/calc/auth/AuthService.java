package co.com.calc.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ReactiveAuthenticationManager authenticationManager;
    private final ReactiveUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public Mono<Map<String, String>> login(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        ).flatMap(auth -> userDetailsService.findByUsername(username)
                .map(userDetails -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("token", jwtUtil.generateToken(userDetails));
                    return response;
                }));
    }
}
