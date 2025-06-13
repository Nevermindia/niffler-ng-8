package guru.qa.niffler.test.fake;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class OauthTest {

    private static final Config CFG = Config.getInstance();
    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    @ApiLogin(username = "nevermindia", password = "nevermindia")
    void oauthTestExistingUser(@Token String token) {
        assertNotNull(token);
    }

    @Test
    @User()
    @ApiLogin
    void oauthTestCreatedUser(@Token String token, UserJson user) {
        System.out.println(user);
        assertNotNull(token);
    }
}
