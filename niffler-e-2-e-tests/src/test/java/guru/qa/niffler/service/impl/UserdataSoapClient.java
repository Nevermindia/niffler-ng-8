package guru.qa.niffler.service.impl;


import guru.qa.niffler.api.UserdataSoapApi;
import guru.qa.niffler.api.core.converter.SoapConverterFactory;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.service.RestClient;
import io.qameta.allure.Step;
import jaxb.userdata.CurrentUserRequest;
import jaxb.userdata.UserResponse;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class UserdataSoapClient extends RestClient {

    private static final Config CFG = Config.getInstance();
    private final UserdataSoapApi userdataSoapApi;

    public UserdataSoapClient() {
        super(CFG.userdataUrl(), false, SoapConverterFactory.create("niffler-userdata"), HttpLoggingInterceptor.Level.BODY);
        this.userdataSoapApi = retrofit.create(UserdataSoapApi.class);
    }

    @NotNull
    @Step("Get current user info using SOAP")
    public UserResponse currentUser(CurrentUserRequest request) throws IOException {
        return userdataSoapApi.currentUser(request).execute().body();
    }
}
