package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.service.RestClient;
import io.qameta.allure.Step;
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
