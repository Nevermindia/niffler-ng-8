package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {

    @Nonnull
    public UserJson createUser(UserJson user);
    @Nonnull
    public UserJson createUser(String username, String password);
    public void addFriend(UserJson user, int count);
    public void addIncomeInvitation(UserJson targertUser, int count);
    public void addOutcomeInvitation(UserJson targetUser, int count);
}
