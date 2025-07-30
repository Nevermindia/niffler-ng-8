package guru.qa.niffler.test.soap;


import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.jaxb.userdata.UsersResponse;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.SoapTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.UserdataSoapClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SoapTest
public class SoapUsersTest {

    private final UserdataSoapClient userdataSoapClient = new UserdataSoapClient();

    @Test
    @User
    void currentUserTest(UserJson user) throws IOException {
        CurrentUserRequest request = new CurrentUserRequest();
        request.setUsername(user.username());
        UserResponse response = userdataSoapClient.currentUser(user.username());
        assertEquals(
                user.username(),
                response.getUser().getUsername()
        );
    }

    @User(friends = 2)
    @Test
    void paginatedFriendsTest(UserJson user) throws IOException {
        UsersResponse response = userdataSoapClient
                .friendsWithPagination(user.username(), 0, 10, null);

         assertEquals(2, response.getTotalElements());
         assertEquals(1, response.getTotalPages());
         assertEquals(2, response.getUser().size());
    }

    @User(friends = 7)
    @Test
    void filterByQueryTest(UserJson user) throws IOException {
        UsersResponse response = userdataSoapClient.friendsWithPagination(user.username(), 0, 10, "a");

        assertTrue(response.getUser().stream()
                .allMatch(u -> u.getUsername().contains(".")));
    }

    @User
    @Test
    void sendInvitationTest(UserJson user) throws IOException {
        String friendToBeRequested = "nevermindia";
        UserResponse response = userdataSoapClient.sendInvitation(user.username(), friendToBeRequested);

        assertEquals(FriendshipStatus.INVITE_SENT.toString(), response.getUser().getFriendshipStatus().toString());
        assertEquals(friendToBeRequested, response.getUser().getUsername());
    }

    @User(incomeInvitations = 1)
    @Test
    void acceptInvitationTest(UserJson user) throws IOException {
        String friendToBeAdded = user.testData().incomeInvitations().getFirst().username();
        UserResponse response = userdataSoapClient.acceptInvitation(user.username(), friendToBeAdded);

        assertEquals(FriendshipStatus.FRIEND.toString(), response.getUser().getFriendshipStatus().toString());
        assertEquals(friendToBeAdded, response.getUser().getUsername());
    }

    @User(incomeInvitations = 1)
    @Test
    void declineInvitationTest(UserJson user) throws IOException {
        String invitationToBeDeclined = user.testData().incomeInvitations().getFirst().username();
        UserResponse response = userdataSoapClient.declineInvitation(user.username(), invitationToBeDeclined);

        assertEquals(invitationToBeDeclined, response.getUser().getUsername());
    }

    @User(friends = 1)
    @Test
    void deleteFriendTest(UserJson user) throws IOException {
        String friendToBeRemoved = user.testData().friends().getFirst().username();
        userdataSoapClient.removeFriend(user.username(), friendToBeRemoved);

        UsersResponse verifyResponse = userdataSoapClient.friends(user.username());
        assertEquals(0, verifyResponse.getUser().size());
    }
}
