package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.service.impl.GatewayApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.FRIEND;
import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.INVITE_RECEIVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RestTest
public class FriendsRestTest {
    @RegisterExtension
    static ApiLoginExtension apiLoginExtension = ApiLoginExtension.restApiLoginExtension();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @User(friends = 1, incomeInvitations = 2)
    @ApiLogin
    @Test
    void friendsAndIncomeInvitationsShouldBeReturnedFromGateway(@Token String bearerToken) {
        final List<UserJson> responseBody = gatewayApiClient.allFriends(bearerToken, null);
        assertEquals(3, responseBody.size());
    }

    @User(friends = 1, incomeInvitations = 1)
    @ApiLogin
    @Test
    void shouldReturnFriendsAndIncomeInvitations(UserJson user, @Token String bearerToken) {
        UserJson expectedFriend = user.testData().friends().getFirst();
        UserJson expectedIncomeInvitation = user.testData().incomeInvitations().getFirst();

        List<UserJson> friendsAndInvitations = gatewayApiClient.allFriends(bearerToken, null);
        assertEquals(2, friendsAndInvitations.size());

        UserJson actualFriend = friendsAndInvitations.getLast();
        UserJson actualIncomeInvitation = friendsAndInvitations.getFirst();

        assertEquals(FRIEND, actualFriend.friendshipStatus());
        assertEquals(expectedFriend.id(), actualFriend.id());
        assertEquals(expectedFriend.username(), actualFriend.username());

        assertEquals(expectedIncomeInvitation.id(), actualIncomeInvitation.id());
        assertEquals(expectedIncomeInvitation.username(), actualIncomeInvitation.username());
    }

    @User(friends = 1)
    @ApiLogin
    @Test
    void shouldDisplayFriendInFriendsList(UserJson user, @Token String bearerToken) {
        UserJson expectedFriend = user.testData().friends().getFirst();
        List<UserJson> friendsList = gatewayApiClient.allFriends(bearerToken, null);
        UserJson actualFriend = friendsList.getLast();

        assertEquals(1, friendsList.size());
        assertEquals(expectedFriend.username(), actualFriend.username());
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void shouldDisplayIncomeInvitationInFriendsList(UserJson user, @Token String bearerToken) {
        UserJson expectedIncomeInvitation = user.testData().incomeInvitations().getFirst();
        List<UserJson> friendsAndInvitations = gatewayApiClient.allFriends(bearerToken, null);

        assertEquals(1, friendsAndInvitations.size());

        final UserJson actualIncomeInvitation = friendsAndInvitations.getFirst();
        assertEquals(expectedIncomeInvitation.username(), actualIncomeInvitation.username());
        assertEquals(INVITE_RECEIVED, actualIncomeInvitation.friendshipStatus());

    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void shouldAcceptFriendInvitation(UserJson user, @Token String bearerToken) {
        UserJson expectedFriend = user.testData().incomeInvitations().getFirst();
        UserJson acceptedFriend = gatewayApiClient.acceptInvitation(bearerToken, new FriendJson(expectedFriend.username()));
        assertEquals(FRIEND, acceptedFriend.friendshipStatus());
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void shouldDeclineFriendInvitation(UserJson user, @Token String bearerToken) {
        UserJson expectedInvitation = user.testData().incomeInvitations().getFirst();
        UserJson declinedInvitation = gatewayApiClient.declineInvitation(bearerToken, new FriendJson(expectedInvitation.username()));
        UserJson currentUser = gatewayApiClient.currentUser(bearerToken);
        assertNull(declinedInvitation.friendshipStatus());
        assertNull(currentUser.friendshipStatus());
    }

    @User(friends = 1)
    @ApiLogin
    @Test
    void shouldRemoveFriendFromFriendsList(UserJson user, @Token String bearerToken) {
        UserJson friendToRemove = user.testData().friends().getFirst();
        gatewayApiClient.deleteFriend(bearerToken, friendToRemove.username());

        UserJson currentUser = gatewayApiClient.currentUser(bearerToken);
        assertNull(currentUser.friendshipStatus());
    }
}
