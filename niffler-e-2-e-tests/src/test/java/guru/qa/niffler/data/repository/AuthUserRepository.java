package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userAuth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {
    AuthUserEntity createUser(AuthUserEntity user);

    Optional<AuthUserEntity> findById(UUID id);

    Optional<AuthUserEntity> findByUsername(String username);

    public List<AuthUserEntity> findAll();
}
