package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userAuth.AuthUserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthUserRepositoryHibernate implements AuthUserRepository {
    private final Config CFG = Config.getInstance();
    private final EntityManager entityManager = EntityManagers.em(CFG.authJdbcUrl());

    @Nonnull
    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Nonnull
    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.merge(user);
        return user;
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(AuthUserEntity.class, id));
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try {
            return Optional.of(entityManager.createQuery("select u from AuthUserEntity u where u.username =: username", AuthUserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void remove(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.remove(user);
    }
}
