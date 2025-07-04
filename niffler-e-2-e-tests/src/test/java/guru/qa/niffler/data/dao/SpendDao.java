package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendDao {
    @Nonnull
    SpendEntity create(SpendEntity spend);

    @Nonnull
    public SpendEntity update(SpendEntity spend);

    @Nonnull
    Optional<SpendEntity> findSpendById(UUID id);

    @Nonnull
    List<SpendEntity> findAllByUsername(String username);

    @Nonnull
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

    @Nonnull
    public List<SpendEntity> findAll();

    void deleteSpend(SpendEntity spend);
}
