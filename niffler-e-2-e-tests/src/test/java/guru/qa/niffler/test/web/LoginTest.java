package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class LoginTest {
    String username = "nevermindia";
    String correctPassword = "nevermindia";

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
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
