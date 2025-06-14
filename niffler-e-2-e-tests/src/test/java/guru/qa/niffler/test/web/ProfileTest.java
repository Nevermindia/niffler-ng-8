package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.test.web.utils.RandomDataUtils;
import guru.qa.niffler.test.web.utils.SelenideUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

@WebTest
public class ProfileTest {

    @Test
    @User(
            categories = @Category(
                    archived = true
            )
    )
    @ApiLogin
    void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
        final CategoryJson archivedCategory = user.testData().categories().getFirst();
        new MainPage().getHeader()
                .toProfilePage()
                .clickOnArchivedCategorySwitch()
                .checkCategoryExists(archivedCategory.name());
    }

    @Test
    @User(
            categories = @Category()
    )

    @ApiLogin
    void activeCategoryShouldPresentInCategoriesList(UserJson user) {
        new MainPage().getHeader()
                .toProfilePage()
                .checkCategoryExists(user.testData().categories().getFirst().name());
    }


    @ScreenShotTest(value = "img/expected-avatar.png")
    @User
    @ApiLogin
    void checkProfileImageTest(UserJson user, BufferedImage expectedProfileImage) throws IOException {
        new MainPage()
                .getHeader()
                .toProfilePage()
                .uploadAvatar("img/avatar.png")
                .checkAvatar(expectedProfileImage);
    }

    @Test
    @User()
    @ApiLogin
    void editProfile(UserJson user) {
        String editName = RandomDataUtils.randomName();
       new MainPage()
                .getHeader()
                .toProfilePage()
                .changeName(editName)
                .checkAlertMessage("Profile successfully updated");

        Selenide.refresh();
        new ProfilePage().checkName(editName);
    }
}
