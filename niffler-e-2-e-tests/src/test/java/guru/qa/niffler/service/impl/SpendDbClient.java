package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.hibernate.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.jdbc.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.spring.SpendRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;


public class SpendDbClient implements SpendClient {
    private static final Config CFG = Config.getInstance();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    private final SpendRepository spendRepositoryJdbc = new SpendRepositoryJdbc();
    private final SpendRepository spendRepositorySpring = new SpendRepositorySpringJdbc();
    private final SpendRepository spendRepositoryHibernate = new SpendRepositoryHibernate();

    public SpendJson createSpendJdbc(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);

                    return SpendJson.fromEntity(
                            spendRepositoryJdbc.create(spendEntity));
                }
        );
    }

    public SpendJson createSpendSpring(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);

                    return SpendJson.fromEntity(
                            spendRepositorySpring.create(spendEntity));
                }
        );
    }

    public SpendJson createSpendHibernate(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);

                    return SpendJson.fromEntity(
                            spendRepositoryHibernate.create(spendEntity));
                }
        );
    }
}
