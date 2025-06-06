package guru.qa.niffler.test.api;


import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;


@Order(Integer.MAX_VALUE)
public class FullDbTest {
    private final UsersApiClient usersApiClient = new UsersApiClient();

    @Test
    void fullDbTest() {
        List<UserJson> allUsers = usersApiClient.allUsers("nevermindia", null);
        Assertions.assertFalse(allUsers.isEmpty());
    }
}
