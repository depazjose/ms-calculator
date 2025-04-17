package co.com.calc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class User {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
}
