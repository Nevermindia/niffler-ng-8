package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.pageable.RestResponsePage;
import guru.qa.niffler.service.impl.GatewayV2ApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.data.domain.Page;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RestTest
public class FriendsV2RestTest {

    @RegisterExtension
    static ApiLoginExtension apiLoginExtension = ApiLoginExtension.restApiLoginExtension();

    private final GatewayV2ApiClient gatewayV2ApiClient = new GatewayV2ApiClient();

    @User(friends = 1, incomeInvitations = 2)
    @ApiLogin
    @Test
    void friendsAndIncomeInvitationsShouldBeReturnedFromGateway(@Token String bearerToken) {
        final Page<UserJson> responseBody = gatewayV2ApiClient.allFriends(
                bearerToken,
                0,
                10,
                "username",
                null
        );
        assertEquals(3, responseBody.getContent().size());
    }

    @User(friends = 1, incomeInvitations = 1)
    @ApiLogin
    @Test
    void friendsAndIncomeInvitationsShouldBeReturned(UserJson user, @Token String bearerToken) {
        final UserJson expectedFriend = user.testData().friends().getFirst();
        final UserJson expectedIncomeInvitation = user.testData().incomeInvitations().getFirst();

        final RestResponsePage<UserJson> response = gatewayV2ApiClient
                .allFriends(
                        bearerToken,
                        0,
                        10,
                        "username,ASC",
                        null);
        assertEquals(2, response.getContent().size());


        final UserJson actualFriend = response.getContent().getLast();
        final UserJson actualIncomeInvitation = response.getContent().getFirst();
        assertEquals(expectedFriend.username(), actualFriend.username());
        assertEquals(expectedIncomeInvitation.username(), actualIncomeInvitation.username());
    }
}
