package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.password())
                .checkMainPageIsOpened()
                .goToFriendsList()
                .checkFriendExistsInList(user.friend());

    }
    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.password())
                .checkMainPageIsOpened()
                .goToFriendsList()
                .checkNoFriendsInList();
    }
    @Test
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.password())
                .checkMainPageIsOpened()
                .goToFriendsList()
                .checkFriendRequest(user.income());
    }
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.password())
                .checkMainPageIsOpened()
                .goToAllPeopleList()
                .checkOutcomeRequestToUser(user.outcome());
    }
}