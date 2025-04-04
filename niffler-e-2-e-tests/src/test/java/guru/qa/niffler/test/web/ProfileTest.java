package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {
    private static final Config CFG = Config.getInstance();
    String username = "nevermindia";
    String password = "nevermindia";

    @User(
            username = "nevermindia",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(username, password)
                .checkMainPageIsOpened()
                .goToProfile()
                .clickOnArchivedCategorySwitch()
                .checkCategoryExists(category.name());
    }

    @User(
            username = "nevermindia",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(username, password)
                .checkMainPageIsOpened()
                .goToProfile()
                .checkCategoryExists(category.name());
    }
}
