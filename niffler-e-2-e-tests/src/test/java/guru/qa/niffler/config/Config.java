package guru.qa.niffler.config;

import javax.annotation.Nonnull;

public interface Config {

    static @Nonnull Config getInstance() {
        return "docker".equals(System.getProperty("test.env"))
                ? DockerConfig.instance
                : LocalConfig.instance;
    }
    @Nonnull
    String frontUrl();

    @Nonnull
    String authUrl();

    @Nonnull
    String authJdbcUrl();

    @Nonnull
    String gatewayUrl();

    @Nonnull
    String userdataUrl();

    @Nonnull
    String userdataJdbcUrl();

    @Nonnull
    String spendUrl();

    @Nonnull
    String spendJdbcUrl();

    @Nonnull
    String currencyJdbcUrl();

    @Nonnull
    String currencyGrpcAddress();

    default int currencyGrpcPort(){
        return 8092;
    }

    @Nonnull
    default String ghUrl(){
        return "https://api.github.com/";
    }
}
