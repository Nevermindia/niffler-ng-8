package guru.qa.niffler.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.CodeInterceptor;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.test.web.utils.OauthUtils;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class AuthApiClient extends RestClient {
    private static final Config CFG = Config.getInstance();
    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl(), true, new CodeInterceptor());
        this.authApi = retrofit.create(AuthApi.class);
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

        Response<Void> loginResponse = authApi.login(
                username,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        ).execute();

        Response<JsonNode> tokenResponse = authApi.token(
                ApiLoginExtension.getCode(),
                redirectUri,
                clientId,
                codeVerifier,
                "authorization_code"

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
