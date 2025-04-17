package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;


import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;
import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

public class SpendDbClient {
    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                        .create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(
                    new SpendDaoJdbc(connection).create(spendEntity)
            );
        }, CFG.spendJdbcUrl(), TRANSACTION_READ_COMMITTED);
    }

    public SpendJson createSpendSpringJdbc(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                    .create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(
                new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                        .create(spendEntity)
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(
                    new CategoryDaoJdbc(connection).create(categoryEntity));
        }, CFG.spendJdbcUrl(), TRANSACTION_READ_COMMITTED);
    }
}
