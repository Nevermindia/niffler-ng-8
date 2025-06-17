package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {

    @Test
    @User(friends = 1)
    @ApiLogin
    void friendShouldBePresentInFriendsTable(UserJson user) {
        new MainPage().getHeader()
                .toFriendsPage()
                .searchFriend(user.testData().friends().get(0).username())
                .checkFriendExistsInList(user.testData().friends().get(0).username());
    }

    @Test
    @User
    @ApiLogin
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        new MainPage().getHeader()
                .toFriendsPage()
                .checkNoFriendsInList();
    }

    @Test
    @User(incomeInvitations = 1)
    @ApiLogin
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        new MainPage().getHeader()
                .toFriendsPage()
                .checkFriendRequest(user.testData().incomeInvitations().get(0).username());
    }

    @Test
    @User(outcomeInvitations = 1)
    @ApiLogin
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        new MainPage().getHeader()
                .toAllPeoplesPage()
                .checkOutcomeRequestToUser(user.testData().outcomeInvitations().get(0).username());
    }


    @Test
    @User(incomeInvitations = 1)
    @ApiLogin
    void acceptInvitation(UserJson user) {
        new MainPage().getHeader()
                .toFriendsPage()
                .checkFriendRequest(user.testData().incomeInvitations().get(0).username())
                .acceptFriendRequest(user.testData().incomeInvitations().get(0).username());

        new FriendsPage()
                .checkNoFriendsRequests()
                .checkFriendExistsInList(user.testData().incomeInvitations().get(0).username());
    }

    @Test
    @User(incomeInvitations = 1)
    @ApiLogin
    void declineInvitation(UserJson user) {
        new MainPage().getHeader()
                .toFriendsPage()
                .checkFriendRequest(user.testData().incomeInvitations().get(0).username())
                .declineFriendRequest(user.testData().incomeInvitations().get(0).username());

        Selenide.sleep(3000);
        new FriendsPage().checkNoFriendsInList();
    }
}