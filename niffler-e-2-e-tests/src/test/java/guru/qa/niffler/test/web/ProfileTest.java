package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {
    private static final Config CFG = Config.getInstance();
    String username = "nevermindia";
    String password = "nevermindia";

    @Category(
            username = "nevermindia",
            archived = true
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

    @Category(
            username = "nevermindia",
            archived = false
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
