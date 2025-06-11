package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.test.web.utils.OauthUtils;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class AuthApiClient extends RestClient {
    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl());
        authApi = retrofit.create(AuthApi.class);
    }


    @SneakyThrows
    public String login(@Nonnull String username,
                        @Nonnull String password) {
        final String codeVerifier = OauthUtils.generateCodeVerifier();
        final String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);
        final String redirectUri = CFG.frontUrl() + "authorized";
        final String clientId = "client";

        Response<Void> authResponse = authApi.authorize(
                "code",
                clientId,
                "openid",
                redirectUri,
                codeChallenge,
                "S256"
        ).execute();
        assertEquals(302, authResponse.code());

        Response<Void> loginResponse = authApi.login(
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN"),
                username,
                password
        ).execute();
        assertEquals(302, loginResponse.code());

        String locationUrl = loginResponse.headers().get("Location");

        String code = StringUtils.substringAfter(locationUrl, "code=");

        Response<JsonNode> tokenResponse = authApi.token(
                code,
                redirectUri,
                codeVerifier,
                "authorization_code",
                clientId

        ).execute();
        assertEquals(200, tokenResponse.code());

        return tokenResponse.body().get("id_token").asText();
    }

    @Step("Register new user using REST API")
    public void register(@Nonnull String csrf,
                         @Nonnull String username,
                         @Nonnull String password,
                         @Nonnull String passwordSubmit) {
        final Response<Void> response;
        try {
            response = authApi.register(csrf, username, password, passwordSubmit).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
    }

    @Step("Get register form")
    public void requestRegisterForm() {
        final Response<Void> response;
        try {
            response = authApi.getRegisterForm().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }
}
