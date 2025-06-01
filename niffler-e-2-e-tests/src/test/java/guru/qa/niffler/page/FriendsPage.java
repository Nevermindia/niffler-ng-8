package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class FriendsPage extends BasePage<FriendsPage> {
    public static final String URL = Config.getInstance().authUrl() + "people/friends";
    private final SearchField searchField = new SearchField();
    private final SelenideElement friendTable = $("#friends");
    private final SelenideElement requestTable = $("#requests");
    private final SelenideElement lonelyNifflerImg = $("img[alt='Lonely niffler']");
    private final ElementsCollection buttonsInDialog = $$("div[role=\"dialog\"] button");

    @Nonnull
    public SearchField getSearchField() {
        return searchField;
    }
    @Step("Check friend with name {0} in \"My friends\" list")
    @Nonnull
    public FriendsPage checkFriendExistsInList(String username) {
        friendTable.$$("tr").find(text(username)).shouldBe(visible);
        return this;
    }

    @Step("Check no friends in \"My friends\" list")
    @Nonnull
    public FriendsPage checkNoFriendsInList() {
        friendTable.$$("tr").shouldHave(size(0));
        lonelyNifflerImg.shouldBe(visible);
        return this;
    }

    @Step("Check no friends requests")
    @Nonnull
    public FriendsPage checkNoFriendsRequests() {
        requestTable.shouldNot(Condition.exist);
        return this;
    }

    @Step("Check friend request from user '{0}'")
    @Nonnull
    public FriendsPage checkFriendRequest(String username) {
        requestTable.$(byText((username))).shouldBe(visible);
        return this;
    }

    @Step("Accept friend request")
    @Nonnull
    public FriendsPage acceptFriendRequest(String username) {
        SelenideElement friendRow = requestTable.$$("tr").find(text(username));
        friendRow.$$("button").get(0).click();
        return this;
    }

    @Step("Decline friend request")
    @Nonnull
    public FriendsPage declineFriendRequest(String username) {
        SelenideElement friendRow = requestTable.$$("tr").find(text(username));
        friendRow.$$("button").get(1).click();
        buttonsInDialog.findBy(text("Decline")).click();
        return this;
    }

    @Step("Search friend")
    @Nonnull
    public FriendsPage searchFriend(String spend) {
        getSearchField().search(spend);
        return this;
    }
}