package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.test.web.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class ProfilePage {

    public static String url = Config.getInstance().frontUrl() + "profile";

    private final SelenideElement avatar = $(".MuiAvatar-img");
    private final SelenideElement pictureInput = $("input[type='file']");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement saveChangesBtn = $("button[type='submit']");
    private final SelenideElement categoryInput = $("input[name='category']");
    private final SelenideElement showArchivedSwitch = $("input[type='checkbox']");
    private final ElementsCollection categories = $$(".MuiChip-label");

    @Step("Click on archived category switch")
    @Nonnull
    public ProfilePage clickOnArchivedCategorySwitch() {
        showArchivedSwitch.click();
        return new ProfilePage();
    }

    @Step("Check category '{0}' exists")
    @Nonnull
    public ProfilePage checkCategoryExists(String category) {
        categories.find(text(category)).shouldBe(visible);
        return new ProfilePage();
    }


    @Step("Check profile avatar")
    @Nonnull
    public ProfilePage checkAvatar(BufferedImage expected) throws IOException {
        Selenide.sleep(3000);
        BufferedImage actual = ImageIO.read(avatar.screenshot());
        assertFalse(new ScreenDiffResult(expected, actual));
        return this;
    }

    @Step("Upload avatar")
    @Nonnull
    public ProfilePage uploadAvatar(String path) {
        pictureInput.uploadFromClasspath(path);
        return this;
    }

    @Step("Change name")
    @Nonnull
    public ProfilePage changeName(String name) {
        nameInput.clear();
        nameInput.setValue(name).pressEnter();
        saveChangesBtn.click();
        return this;
    }

    @Step("Check name")
    @Nonnull
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }
}
