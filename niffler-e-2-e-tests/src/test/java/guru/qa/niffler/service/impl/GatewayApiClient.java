package guru.qa.niffler.service.impl;


import guru.qa.niffler.api.GatewayApi;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.service.RestClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class GatewayApiClient extends RestClient {

    private final GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayApi = retrofit.create(GatewayApi.class);
    }

    @Step("Get all friends & income invitations using /api/friends/all endpoint")
    @Nonnull
    public List<UserJson> allFriends(String bearerToken,
                                     @Nullable String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = gatewayApi.allFriends(bearerToken, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Step("Delete friend using /api/friends/remove endpoint")
    public void deleteFriend(String bearerToken, @Nullable String targetUsername) {
        final Response<Void> response;
        try {
            response = gatewayApi.removeFriend(bearerToken, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    @Step("Sent invitations using /api/invitations/send endpoint")
    @Nonnull
    public UserJson sendInvitation(String bearerToken, FriendJson friend) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.sendInvitation(bearerToken, friend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Step("Accept invitation using /api/invitations/accept endpoint")
    @Nonnull
    public UserJson acceptInvitation(String bearerToken, FriendJson friend) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.acceptInvitation(bearerToken, friend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Step("Decline invitation using /api/invitations/decline endpoint")
    @Nonnull
    public UserJson declineInvitation(String bearerToken, FriendJson friend) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.declineInvitation(bearerToken, friend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }


    @Step("Get all users using /api/users/all endpoint")
    @Nonnull
    public List<UserJson> allUsers(String bearerToken, @Nullable String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = gatewayApi.allUsers(bearerToken, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }


    @Step("Sent GET request /api/users/current to niffler-gateway")
    @Nonnull
    public UserJson currentUser(String bearerToken) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.currentUser(bearerToken)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }
}
