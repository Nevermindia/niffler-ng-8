package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.impl.hibernate.UserDataRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.jdbc.UserDataRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.spring.UserDataRepositorySpringJdbc;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserDataRepository {

    @Nonnull
    static UserDataRepository getInstance() {
        return switch (System.getProperty("repository.impl", "jpa")) {
            case "jpa" -> new UserDataRepositoryHibernate();
            case "jdbc" -> new UserDataRepositoryJdbc();
            case "sjdbc" -> new UserDataRepositorySpringJdbc();
            default -> throw new IllegalStateException("Unexpected value: " + System.getProperty("repository.impl"));
        };
    }

    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    UserEntity update(UserEntity user);

    void addFriend(UserEntity requester, UserEntity addressee);

    void addOutcomeRequest(UserEntity requester, UserEntity addressee);

    void addIncomeRequest(UserEntity requester, UserEntity addressee);

    void remove(UserEntity user);
}
