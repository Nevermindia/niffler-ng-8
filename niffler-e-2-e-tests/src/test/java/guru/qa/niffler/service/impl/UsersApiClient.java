package guru.qa.niffler.service.impl;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.UsersApi;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.INVITE_RECEIVED;
import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.INVITE_SENT;
import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomUsername;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UsersApiClient extends RestClient implements UsersClient {

    private final UsersApi usersApi;
    private final AuthApiClient authApi = new AuthApiClient();

    public UsersApiClient() {
        super(CFG.userdataUrl());
        usersApi = retrofit.create(UsersApi.class);
    }

    @Nonnull
    @Override
    @Step("Create user using API")
    public UserJson createUser(@Nonnull String username, String password) {
        authApi.requestRegisterForm();
        authApi.register(
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN"),
                username,
                password,
                password
        );

        long maxWaitTime = 5000L;
        Stopwatch sw = Stopwatch.createStarted();

        while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
            try{
                UserJson userJson = currentUser(username);
                if (userJson != null && userJson.id() != null) {
                    return userJson;
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        throw new AssertionError("User was not found in userdata");
    }

    @Step("Get current user using API")
    @Nonnull
    public UserJson currentUser(@Nonnull String username){
        final Response<UserJson> response;
        try {
            response = usersApi.currentUser(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Get all users using API")
    @Nonnull
    public List<UserJson> allUsers(@Nonnull String username, @Nonnull String searchQuery){
        final Response<List<UserJson>> response;
        try {
            response = usersApi.allUsers(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null ? response.body() : Collections.emptyList();
    }

    @Step("Get user's friends using API")
    @Nonnull
    public List<UserJson> getFriends(@Nonnull String username){
        final Response<List<UserJson>> response;
        try {
            response = usersApi.friends(username, null)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null ? response.body() : Collections.emptyList();
    }

    @Step("Get all user's people using API")
    @Nonnull
    public List<UserJson> getAllPeople(@Nonnull String username){
        final Response<List<UserJson>> response;
        try {
            response = usersApi.allUsers(username, null)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null ? response.body() : Collections.emptyList();
    }

    @Step("Get user's income invitations using API")
    @Nonnull
    public List<UserJson> getIncomeInvitations(@Nonnull String username){
        List<UserJson> friends = getFriends(username);

        return friends.stream()
                .filter(userJson -> userJson.friendshipStatus().equals(INVITE_RECEIVED))
                .toList();
    }

    @Step("Get user's outcome invitations using API")
    @Nonnull
    public List<UserJson> getOutcomeInvitations(@Nonnull String username){
        List<UserJson> allPeople = getAllPeople(username);

        return allPeople.stream()
                .filter(userJson -> userJson.friendshipStatus() != null)
                .filter(userJson -> userJson.friendshipStatus().equals(INVITE_SENT))
                .collect(Collectors.toList());
    }

    @Step("Add friend using API")
    @Override
    public void addFriend(@Nonnull UserJson user, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final Response<UserJson> response;
                final UserJson newUser;
                try {
                    newUser = createUser(randomUsername(), "12345");
                    usersApi.sendInvitation(
                            user.username(),
                            newUser.username()
                    ).execute();
                    response = usersApi.acceptInvitation(user.username(), newUser.username())
                            .execute();
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                assertEquals(200, response.code());
            }}

    }

    @Step("Add income invitation using API")
    @Override
    public void addIncomeInvitation(@Nonnull UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final Response<UserJson> response;
                final UserJson newUser;
                try {
                    newUser = createUser(randomUsername(), "12345");

                    response = usersApi.sendInvitation(
                            newUser.username(),
                            targetUser.username()
                    ).execute();
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                assertEquals(200, response.code());
            }
        }
    }

    @Step("Add outcome invitation using API")
    @Override
    public void addOutcomeInvitation(@Nonnull UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final Response<UserJson> response;
                final UserJson newUser;
                try {
                    newUser = createUser(randomUsername(), "12345");

                    response = usersApi.sendInvitation(
                            targetUser.username(),
                            newUser.username()
                    ).execute();
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                assertEquals(200, response.code());
            }
        }
    }
}
