package co.com.calc.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("user")
public class UserData {
    @Id
    private UUID id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
}
