package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

@WebTest
public class RegistrationTest {
    Faker faker = new Faker();

    @Test
    void shouldRegisterNewUser() {
        String username = faker.name().username();
        String password = faker.lorem().word();
        Selenide.open(RegisterPage.URL, RegisterPage.class)
                .doRegister(username, password, password)
                .doLogin(username, password)
                .checkMainPageIsOpened();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String username = "nevermindia";
        String password = faker.lorem().word();
        String errorText = "Username `" + username + "` already exists";
        Selenide.open(RegisterPage.URL, RegisterPage.class)
                .setUserName(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkErrorMessage(errorText);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String username = faker.name().username();
        String password = faker.lorem().word();
        String submitPassword = faker.lorem().word();
        String errorText = "Passwords should be equal";
        Selenide.open(RegisterPage.URL, RegisterPage.class)
                .setUserName(username)
                .setPassword(password)
                .setPasswordSubmit(submitPassword)
                .submitRegistration()
                .checkErrorMessage(errorText);
    }
}