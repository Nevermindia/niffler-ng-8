package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkMainPageIsOpened();
        new MainPage().getHeader()
                .toFriendsPage()
                .searchFriend(user.testData().friends().get(0).username())
                .checkFriendExistsInList(user.testData().friends().get(0).username());
    }

    @User()
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkMainPageIsOpened();
        new MainPage().getHeader()
                .toFriendsPage()
                .checkNoFriendsInList();
    }

    @User(
            incomeInvitations = 1
    )
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkMainPageIsOpened();
        new MainPage().getHeader()
                .toFriendsPage()
                .checkFriendRequest(user.testData().incomeInvitations().get(0).username());
    }

    @User(
            outcomeInvitations = 1
    )
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkMainPageIsOpened();
        new MainPage().getHeader()
                .toAllPeoplesPage()
                .checkOutcomeRequestToUser(user.testData().outcomeInvitations().get(0).username());
    }


    @User(
            incomeInvitations = 1
    )
    @Test
    void acceptInvitation(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkMainPageIsOpened();
        new MainPage().getHeader()
                .toFriendsPage()
                .checkFriendRequest(user.testData().incomeInvitations().get(0).username())
                .acceptFriendRequest(user.testData().incomeInvitations().get(0).username());

        Selenide.sleep(3000);
        new FriendsPage()
                .checkNoFriendsRequests()
                .checkFriendExistsInList(user.testData().incomeInvitations().get(0).username());
    }

    @User(
            incomeInvitations = 1
    )
    @Test
    void declineInvitation(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkMainPageIsOpened();
        new MainPage().getHeader()
                .toFriendsPage()
                .checkFriendRequest(user.testData().incomeInvitations().get(0).username())
                .declineFriendRequest(user.testData().incomeInvitations().get(0).username());

        Selenide.sleep(3000);
        new FriendsPage().checkNoFriendsInList();
    }
}