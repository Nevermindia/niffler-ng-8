package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface CategoryDao {
    @Nonnull
    CategoryEntity create(CategoryEntity category);

    @Nonnull
    public CategoryEntity update(CategoryEntity category);

    @Nonnull
    Optional<CategoryEntity> findCategoryById(UUID id);

    @Nonnull
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name);

    @Nonnull
    List<CategoryEntity> findAllByUsername(String username);

    @Nonnull
    public List<CategoryEntity> findAll();

    void deleteCategory(CategoryEntity category);
}
