package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class AllPeoplePage extends BasePage<AllPeoplePage> {
    public static final String URL = Config.getInstance().authUrl() + "people/all";

    ElementsCollection friendRows = $$("tr");

    @Step("Check outcome request to '{0}' exists")
    @Nonnull
    public AllPeoplePage checkOutcomeRequestToUser(String username) {
        SelenideElement friendRow = friendRows.find(text(username));
        friendRow.find(byText("Waiting...")).shouldBe(visible);
        return this;
    }
}