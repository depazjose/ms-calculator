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
        return Mono.fromSupplier(() -> repository.findByusername(username))
                .flatMap(Mono::just).map(this::toEntity);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return Mono.fromSupplier(() -> repository.findByEmail(email))
                .flatMap(Mono::just).map(this::toEntity);
    }
}
