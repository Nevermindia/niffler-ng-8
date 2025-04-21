package guru.qa.niffler.test.web;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;


public class JdbcTest {
    //spend test
    @Test
    void daoTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "test-cat-name-3",
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
                                "test-cat-tx1",
                                "nevermindia",
                                false
                        ),
                        CurrencyValues.RUB,
                        100.0,
                        "test-cat-tx1",
                        "nevermindia"
                )
        );
        System.out.println(spend);
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

    //auth user tests

    /*
        jdbc с транзакциями
        - позитивный тест -> в БД auth и userdata создан пользователь, можно авторизоваться с username/password
        - негативный тест -> юзера нет в БД auth и userdata
     */
    @Test
    void xaCreateUserJdbcTest() {
        UserDbClient authUserDbClient = new UserDbClient();
        UserJson user = authUserDbClient.xaCreateUserJdbc(
                new UserJson(
                        null,
                        "xaCreateUserJdbcTest4",
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

    /*
        jdbc без транзакций
        - позитивный тест -> в БД auth и userdata есть пользователь, можно авторизоваться с username/password
        - негативный тест (user_name == null при создании authority) -> в таблице user в niffler-auth значение есть,
         в authority нет, в userdata юзера нет
     */

    @Test
    void createUserJdbcTest() {
        UserDbClient authUserDbClient = new UserDbClient();
        UserJson user = authUserDbClient.createUserJdbc(
                new UserJson(
                        null,
                        "createUserJdbcTest1",
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

    /*
    spring-jdbc с транзакциями
    - позитивный тест -> в БД auth и userdata есть пользователь, можно авторизоваться с username/password
    - негативный тест (username == null в userdata) -> юзер не создан в userdata и в auth
     */
    @Test
    void xaCreateUserSpringJdbcTest() {
        UserDbClient authUserDbClient = new UserDbClient();
        UserJson user = authUserDbClient.xaCreateUserSpringJdbc(
                new UserJson(
                        null,
                        "xaCreateUserSpringJdbcTest1",
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

    /*
    spring-jdbc без транзакции
    - позитивный тест -> в БД auth и userdata есть пользователь, можно авторизоваться с username/password
    - негативный тест (user_name == null при создании authority) -> в таблице user в niffler-auth значение есть,
    в authority нет, в userdata юзера нет
     */
    @Test
    void createUserSpringJdbcTest() {
        UserDbClient authUserDbClient = new UserDbClient();
        UserJson user = authUserDbClient.createUserSpringJdbc(
                new UserJson(
                        null,
                        "createUserSpringJdbcTest2",
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

    /*
    ChainedTransactionManager
    - позитивный тест -> в БД auth и userdata есть пользователь, можно авторизоваться с username/password
    - негативный тест (username == null в userdata) -> в таблицах user и authority бд niffler-auth юзер создан,
    в user бд niffler-userdata пользователь не создан. Созданные данные в таблицах user и authority бд niffler-auth
    не были откачены после ошибки создания юзера в userdata
     */
    @Test
    void createUserChainedTxManagerTest() {
        UserDbClient authUserDbClient = new UserDbClient();
        UserJson user = authUserDbClient.createUserChainedTxManager(
                new UserJson(
                        null,
                        "createUserChainedTxManagerTest1",
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


    /*
    проверка метода xaCreateUserRepository
     */

    @Test
    void createUserWithRepositoryTest() {
        UserDbClient authUserDbClient = new UserDbClient();
        UserJson user = authUserDbClient.xaCreateUserRepository(
                new UserJson(
                        null,
                        "xaCreateUserRepository1",
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
}
