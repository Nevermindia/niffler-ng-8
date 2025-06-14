package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.*;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebTest
public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User(
            spendings = @Spending(
                    category = "Еда",
                    description = "Сырки Б Ю Александров",
                    amount = 1250.00,
                    currency = CurrencyValues.RUB
            ))

    @Test
    void spendingDescriptionShouldBeUpdatedByTableAction(UserJson user) {
        final String newDescription = "Обучение Niffler NG";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getSpendingTable()
                .editSpending(user.testData().spendings().getFirst().description())
                .editDescription(newDescription)
                .getSpendingTable()
                .checkTableContains(newDescription);
    }

    @User(
            spendings = {@Spending(
                    category = "Еда",
                    description = "Сырки Б Ю Александров",
                    amount = 1250.00,
                    currency = CurrencyValues.RUB
            ), @Spending(
                    category = "Обучение",
                    description = "Английский язык",
                    amount = 1300.00,
                    currency = CurrencyValues.RUB
            )})
    @ScreenShotTest(value = "img/expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expected) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkStatBubblesInAnyOrder(new Bubble(Color.green, "Еда 1250 ₽"), new Bubble(Color.yellow, "Обучение 1300 ₽"))
                .checkStatisticDiagram(expected);
    }

    @User(
            spendings = {@Spending(
                    category = "Еда",
                    description = "Сырки Б Ю Александров",
                    amount = 1250.00,
                    currency = CurrencyValues.RUB
            ), @Spending(
                    category = "Обучение",
                    description = "Английский язык",
                    amount = 1300.00,
                    currency = CurrencyValues.RUB
            )})
    @Test
    void checkSpendingTableTest(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getSpendingTable()
                .checkSpendTable(user.testData().spendings().toArray(SpendJson[]::new));
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Медицина",
                            description = "Аспирин",
                            amount = 150.00,
                            currency = CurrencyValues.RUB
                    )
            })
    @ScreenShotTest(value = "img/expected-stat-edit.png")
    void checkStatComponentAfterEditingTest(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getSpendingTable()
                .editSpending(user.testData().spendings().getFirst().description())
                .editSum("175")
                .checkStatisticDiagramInfo(List.of("Медицина 175 ₽"))
                .checkStatisticDiagram(expected);

    }

    @User(
            spendings = @Spending(
                    category = "Ремонт",
                    description = "Обои",
                    amount = 15250.00,
                    currency = CurrencyValues.RUB
            ))
    @ScreenShotTest(value = "img/expected-stat-delete.png")
    void checkStatComponentAfterDeletingSpendTest(UserJson user, BufferedImage expected) throws IOException {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getSpendingTable()
                .deleteSpending(user.testData().spendings().getFirst().description())
                .checkStatisticDiagram(expected);
    }

    @User(
            categories = {
                    @Category(
                            name = "Развлечения",
                            archived = true
                    ),
                    @Category(name = "Ремонт")
            },
            spendings = {
                    @Spending(
                            category = "Ремонт",
                            description = "Обои",
                            amount = 15250.00,
                            currency = CurrencyValues.RUB
                    ),
                    @Spending(
                            category = "Развлечения",
                            description = "Боулинг",
                            amount = 1050.00,
                            currency = CurrencyValues.RUB
                    )
            })
    @ScreenShotTest(value = "img/expected-stat-archived.png")
    void checkStatComponentWithArchiveSpendTest(UserJson user, BufferedImage expected) throws IOException {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkStatisticDiagramInfo(List.of("Archived 15250 ₽", "Боулинг 1050 ₽"))
                .checkStatisticDiagram(expected);
    }

    @User()
    @Test
    void addNewSpending(UserJson user) {
        SpendJson spend = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                        null,
                        "test-cat-name-3",
                        user.username(),
                        false
                ),
                CurrencyValues.RUB,
                100.0,
                "description",
                user.username()
        );

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .goToAddSpending()
                .addNewSpending(spend)
                .checkAlertMessage("New spending is successfully created")
                .getSpendingTable()
                .checkSpendTable(spend);
    }
}
