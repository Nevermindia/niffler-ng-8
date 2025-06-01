package guru.qa.niffler.data.repository.impl.spring;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.extractor.SpendEntityExtractor;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendRepositorySpringJdbc implements SpendRepository {
    private static final Config CFG = Config.getInstance();
    private final SpendDao spendDao = new SpendDaoSpringJdbc();
    private final CategoryDao categoryDao = new CategoryDaoSpringJdbc();

    @Nonnull
    @Override
    public SpendEntity create(SpendEntity spend) {
        if (spend.getCategory().getId() == null
            && categoryDao.findCategoryById(spend.getCategory().getId()).isEmpty()) {
            categoryDao.create(spend.getCategory());
        }
        return spendDao.create(spend);
    }

    @Nonnull
    @Override
    public SpendEntity update(SpendEntity spend) {
        categoryDao.update(spend.getCategory());
        return spendDao.update(spend);
    }

    @Nonnull
    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Nonnull
    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    @Nonnull
    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, name);
    }

    @Nonnull
    @Override
    public Optional<SpendEntity> findById(UUID id) {
        return spendDao.findSpendById(id);
    }

    @Nonnull
    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        return spendDao.findByUsernameAndSpendDescription(username, description);
    }

    @Override
    public void remove(SpendEntity spend) {
        spendDao.deleteSpend(spend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }

    @Nonnull
    public Optional<SpendEntity> findSpendById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        """
                                   SELECT s.id, s.username, s.spend_date, s.currency, s.amount, s.description, c.id as category_id,
                                   c.name as category_name, c.archived as category_archived
                                   FROM spend s JOIN category c on s.category_id = c.id
                                   WHERE s.id = ?
                                """,
                        SpendEntityExtractor.instance,
                        id
                )
        );
    }
}
