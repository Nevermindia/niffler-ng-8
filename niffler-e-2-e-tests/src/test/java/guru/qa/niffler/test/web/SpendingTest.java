package guru.qa.niffler.test.web;

import guru.qa.niffler.condition.Color;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.*;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

@WebTest
public class SpendingTest {

    @Test
    @User(
            spendings = @Spending(
                    category = "Еда",
                    description = "Сырки Б Ю Александров",
                    amount = 1250.00,
                    currency = CurrencyValues.RUB
            ))
    @ApiLogin
    void spendingDescriptionShouldBeUpdatedByTableAction(UserJson user) {
        final String newDescription = "Обучение Niffler NG";

        new MainPage()
                .getSpendingTable()
                .editSpending(user.testData().spendings().getFirst().description())
                .editDescription(newDescription)
                .getSpendingTable()
                .checkTableContains(newDescription);
    }

    @ScreenShotTest(value = "img/expected-stat.png")
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
    @ApiLogin
    void checkStatComponentTest(BufferedImage expected) {
        new MainPage()
                .checkStatBubblesInAnyOrder(new Bubble(Color.green, "Еда 1250 ₽"), new Bubble(Color.yellow, "Обучение 1300 ₽"))
                .checkStatisticDiagram(expected);
    }

    @Test
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
    @ApiLogin
    void checkSpendingTableTest(UserJson user) {
        new MainPage()
                .getSpendingTable()
                .checkSpendTable(user.testData().spendings().toArray(SpendJson[]::new));
    }

    @ScreenShotTest(value = "img/expected-stat-edit.png")
    @User(
            spendings = {
                    @Spending(
                            category = "Медицина",
                            description = "Аспирин",
                            amount = 150.00,
                            currency = CurrencyValues.RUB
                    )
            })
    @ApiLogin
    void checkStatComponentAfterEditingTest(UserJson user, BufferedImage expected) {
        new MainPage()
                .getSpendingTable()
                .editSpending(user.testData().spendings().getFirst().description())
                .editSum("175")
                .checkStatisticDiagramInfo(List.of("Медицина 175 ₽"))
                .checkStatisticDiagram(expected);

    }

    @ScreenShotTest(value = "img/expected-stat-delete.png")
    @User(
            spendings = @Spending(
                    category = "Ремонт",
                    description = "Обои",
                    amount = 15250.00,
                    currency = CurrencyValues.RUB
            ))
    @ApiLogin
    void checkStatComponentAfterDeletingSpendTest(UserJson user, BufferedImage expected) {

        new MainPage()
                .getSpendingTable()
                .deleteSpending(user.testData().spendings().getFirst().description())
                .checkStatisticDiagram(expected);
    }

    @ScreenShotTest(value = "img/expected-stat-archived.png")
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
    @ApiLogin
    void checkStatComponentWithArchiveSpendTest(BufferedImage expected) {

        new MainPage()
                .checkStatisticDiagramInfo(List.of("Archived 15250 ₽", "Боулинг 1050 ₽"))
                .checkStatisticDiagram(expected);
    }

    @Test
    @User
    @ApiLogin
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

        new MainPage()
                .getHeader()
                .goToAddSpending()
                .addNewSpending(spend)
                .checkAlertMessage("New spending is successfully created")
                .getSpendingTable()
                .checkSpendTable(spend);
    }
}
