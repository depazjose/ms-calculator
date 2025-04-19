package co.com.calc.user;

import co.com.calc.helper.ReactiveAdapterOperations;
import co.com.calc.model.User;
import co.com.calc.model.gateway.UserRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserData,
        UUID,
        UserDataRepository
        > implements UserRepository {

    public UserRepositoryAdapter(UserDataRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return repository.findByUsername(username)
                .flatMap(user -> {
                    if (user != null) {
                        return Mono.just(toEntity(user));
                    } else {
                        return Mono.empty(); // Retorna un Mono vacío si no se encuentra el usuario (null)
                    }
                } ) // Si el Optional está vacío, retorna un Mono vacío
                .onErrorResume(throwable -> {
                    System.err.println("Error al buscar usuario por nombre de usuario: " + username + " - " + throwable.getMessage());
                    throwable.printStackTrace();
                    // Aquí puedes decidir retornar un Mono vacío (Mono.empty()),
                    // un Mono con un valor por defecto (Mono.just(defaultUser)),
                    // o propagar una excepción específica (Mono.error(new CustomUserNotFoundException("Usuario no encontrado", throwable)));
                    return Mono.empty(); // En este ejemplo, retornamos un Mono vacío
                });
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByemail(email)
                .flatMap(Mono::just)
                .map(this::toEntity)
                .onErrorResume(throwable -> {
                    System.err.println("Error al buscar usuario por email de usuario: " + email + " - " + throwable.getMessage());
                    throwable.printStackTrace();
                    // Aquí puedes decidir retornar un Mono vacío (Mono.empty()),
                    // un Mono con un valor por defecto (Mono.just(defaultUser)),
                    // o propagar una excepción específica (Mono.error(new CustomUserNotFoundException("Usuario no encontrado", throwable)));
                    return Mono.empty(); // En este ejemplo, retornamos un Mono vacío
                });
    }
}
