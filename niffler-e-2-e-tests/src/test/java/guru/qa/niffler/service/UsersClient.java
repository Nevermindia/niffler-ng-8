package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import guru.qa.niffler.service.impl.UsersDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {

    static UsersClient getInstance() {
        return "api".equals(System.getProperty("client.impl"))
                ? new UsersApiClient()
                : new UsersDbClient();
    }

    @Nonnull
    public UserJson createUser(String username, String password);
    public void addFriend(UserJson user, int count);
    public void addIncomeInvitation(UserJson targertUser, int count);
    public void addOutcomeInvitation(UserJson targetUser, int count);
}
