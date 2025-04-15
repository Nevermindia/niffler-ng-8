package guru.qa.niffler.test.web;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;


public class JdbcTest {
    @Test
    void daoTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "test-cat-name-2",
                                "nevermindia",
                                false
                        ),
                        CurrencyValues.RUB,
                        100.0,
                        "tets desc",
                        "nevermindia"
                )
        );
        System.out.println(spend);
    }

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "test-cat-tx",
                                "nevermindia",
                                false
                        ),
                        CurrencyValues.RUB,
                        100.0,
                        "test-cat-tx",
                        "nevermindia"
                )
        );
        System.out.println(spend);
    }

    @Test
    void authSpringJdbcTest() {
        AuthUserDbClient authUserDbClient = new AuthUserDbClient();
        UserJson user = authUserDbClient.createUserSpringJdbc(
                new UserJson(
                        null,
                        "username13",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void createSpendJdbcTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson spendJson = spendDbClient.createSpendSpringJdbc(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "category-test",
                                "nevermindia",
                                false
                        ),
                        CurrencyValues.RUB,
                        1111.00,
                        "test",
                        "nevermindia"
                )
        );
        System.out.println(spendJson);
    }
}
