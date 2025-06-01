package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Header {

    private final SelenideElement self = $("#root header");
    private final SelenideElement title = self.$("a[href=\"/main\"]");
    private final SelenideElement newSpendingBtn = self.$("a[href=\"/spending\"]");
    private final SelenideElement menuBtn = self.$("button[aria-label=\"Menu\"]");
    private final SelenideElement menu = $("ul[role=\"menu\"]");
    private final ElementsCollection menuItems = menu.$$("li[role=\"menuitem\"]");

    @Step("Check header title text")
    public void checkHeaderText(){
       title.shouldHave(text("Niffler"));
    }

    @Step("Go to Friends page")
    @Nonnull
    public FriendsPage toFriendsPage() {
        menuBtn.click();
        menu.shouldBe(visible);
        menuItems.findBy(text("Friends")).click();
        return new FriendsPage();
    }

    @Step("Go to People page")
    @Nonnull
    public AllPeoplePage toAllPeoplesPage() {
        menuBtn.click();
        menu.shouldBe(visible);
        menuItems.findBy(text("All People")).click();
        return new AllPeoplePage();
    }

    @Step("Go to Profile page")
    @Nonnull
    public ProfilePage toProfilePage() {
        menuBtn.click();
        menu.shouldBe(visible);
        menuItems.findBy(text("Profile")).click();
        return new ProfilePage();
    }

    @Step("Sign out")
    @Nonnull
    public LoginPage signOut() {
        menuBtn.click();
        menu.shouldBe(visible);
        menuItems.findBy(text("Sign out")).click();
        return new LoginPage();
    }

    @Step("Go to add spending page")
    @Nonnull
    public EditSpendingPage goToAddSpending() {
        newSpendingBtn.click();
        return new EditSpendingPage();
    }

    @Step("Go to Main page")
    @Nonnull
    public MainPage toMainPage() {
        title.click();
        return new MainPage();
    }
}
