package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.test.web.utils.RandomDataUtils;
import guru.qa.niffler.test.web.utils.SelenideUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

@WebTest
public class ProfileTest {
    private final SelenideDriver driver = new SelenideDriver(SelenideUtils.chromeConfig);

    @User(
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
        final CategoryJson archivedCategory = user.testData().categories().getFirst();
        driver.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkMainPageIsOpened();
        new MainPage().getHeader()
                .toProfilePage()
                .clickOnArchivedCategorySwitch()
                .checkCategoryExists(archivedCategory.name());
    }

    @User(
            categories = @Category()
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(UserJson user) {
        driver.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkMainPageIsOpened();
        new MainPage().getHeader()
                .toProfilePage()
                .checkCategoryExists(user.testData().categories().getFirst().name());
    }

    @User
    @ScreenShotTest(value = "img/expected-avatar.png")
    void checkProfileImageTest(UserJson user, BufferedImage expectedProfileImage) throws IOException {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkMainPageIsOpened()
                .getHeader()
                .toProfilePage()
                .uploadAvatar("img/avatar.png")
                .checkAvatar(expectedProfileImage);
    }

    @User()
    @Test
    void editProfile(UserJson user) {
        String editName = RandomDataUtils.randomName();
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkMainPageIsOpened()
                .getHeader()
                .toProfilePage()
                .changeName(editName);

        Selenide.refresh();
        new ProfilePage().checkName(editName);
    }
}
