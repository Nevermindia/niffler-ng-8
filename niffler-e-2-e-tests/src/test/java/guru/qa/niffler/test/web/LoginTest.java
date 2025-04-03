package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.TestMethodContextExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

@WebTest
public class LoginTest {
    String username = "nevermindia";
    String correctPassword = "nevermindia";

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        ExtensionContext ctx = TestMethodContextExtension.context();
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(username, correctPassword)
                .checkMainPageIsOpened();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .setUsername(username)
                .setPassword(correctPassword + "12")
                .clickLoginBtn()
                .checkError("Неверные учетные данные пользователя");
    }
}
