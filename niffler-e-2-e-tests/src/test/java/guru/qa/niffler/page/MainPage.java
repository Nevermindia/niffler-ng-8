package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.test.web.AllPeoplePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    private final ElementsCollection tableRows = $$("#spendings tbody tr");
    private final ElementsCollection headerElements = $$(".MuiTypography-h5");
    private final SelenideElement contextMenuInAvatarBtn = $("button[aria-label='Menu']");
    private final ElementsCollection contextMenuElements = $$(".MuiList-padding li");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription))
                .$$("td")
                .get(5)
                .click();
        return new EditSpendingPage();
    }

    @Step("Check that table contains description {0}")
    public MainPage checkThatTableContains(String spendingDescription) {
        tableRows.find(text(spendingDescription))
                .should(visible);
        return this;
    }

    @Step("Check Main page is opened")
    public MainPage checkMainPageIsOpened() {
        headerElements.find(text("Statistics")).shouldBe(visible);
        headerElements.find(text("History of Spendings")).shouldBe(visible);
        headerElements.find(text("Niffler")).shouldBe(visible);
        return this;
    }

    @Step("Go to Profile")
    public ProfilePage goToProfile() {
        contextMenuInAvatarBtn.click();
        contextMenuElements.find(text("Profile")).click();
        return new ProfilePage();
    }

    @Step("Go to Friends")
    public FriendsPage goToFriendsList() {
        contextMenuInAvatarBtn.click();
        contextMenuElements.find(text("Friends")).click();
        return new FriendsPage();
    }

    @Step("Go to All People")
    public AllPeoplePage goToAllPeopleList() {
        contextMenuInAvatarBtn.click();
        contextMenuElements.find(text("All People")).click();
        return new AllPeoplePage();
    }
}
