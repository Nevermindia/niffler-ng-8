package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.data.entity.userAuth.AuthUserEntity;
import guru.qa.niffler.data.entity.userAuth.Authority;
import guru.qa.niffler.data.entity.userAuth.AuthorityEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    //dao jdbc
    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authorityUserDao = new AuthAuthorityDaoJdbc();
    private final UserDao userDao = new UserDaoJdbc();
    //dao spring
    private final AuthUserDao authUserDaoSpring = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authorityUserDaoSpring = new AuthAuthorityDaoSpringJdbc();
    private final UserDao userDaoSpring = new UserDaoSpringJdbc();

    private final TransactionTemplate xaTransactionTemplateChained = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            dataSource(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            dataSource(CFG.userdataJdbcUrl())
                    )
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(), CFG.userdataJdbcUrl()
    );

    private final TransactionTemplate transactionTemplate = new TransactionTemplate(
            new JdbcTransactionManager(dataSource(CFG.authJdbcUrl())));

    //создание юзера jdbc без транзакции
    public UserJson createUserJdbc(UserJson user) {
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setUsername(user.username());
        authUserEntity.setPassword(pe.encode("12345"));
        authUserEntity.setEnabled(true);
        authUserEntity.setAccountNonExpired(true);
        authUserEntity.setAccountNonLocked(true);
        authUserEntity.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDao.createUser(authUserEntity);
        AuthorityEntity[] userAuthorities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }).toArray(AuthorityEntity[]::new);

        authorityUserDao.createUser(userAuthorities);
        return UserJson.fromEntity(
                userDao.createUser(UserEntity.fromJson(user)
                ));
    }

    //создание юзера jdbc с транзакциями
    public UserJson xaCreateUserJdbc(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUserEntity = new AuthUserEntity();
            authUserEntity.setUsername(user.username());
            authUserEntity.setPassword(pe.encode("12345"));
            authUserEntity.setEnabled(true);
            authUserEntity.setAccountNonExpired(true);
            authUserEntity.setAccountNonLocked(true);
            authUserEntity.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDao.createUser(authUserEntity);
            AuthorityEntity[] userAuthorities = Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    }).toArray(AuthorityEntity[]::new);

            authorityUserDao.createUser(userAuthorities);
            return UserJson.fromEntity(
                    userDao.createUser(UserEntity.fromJson(user)
                    ));
        });
    }

    //создание юзера spring-jdbc с транзакциями
    public UserJson xaCreateUserSpringJdbc(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUserEntity = new AuthUserEntity();
            authUserEntity.setUsername(user.username());
            authUserEntity.setPassword(pe.encode("12345"));
            authUserEntity.setEnabled(true);
            authUserEntity.setAccountNonExpired(true);
            authUserEntity.setAccountNonLocked(true);
            authUserEntity.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDaoSpring.createUser(authUserEntity);
            AuthorityEntity[] userAuthorities = Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    }).toArray(AuthorityEntity[]::new);

            authorityUserDaoSpring.createUser(userAuthorities);
            return UserJson.fromEntity(
                    userDaoSpring.createUser(UserEntity.fromJson(user)
                    ));
        });
    }

    //создание юзера spring-jdbc без транзакций
    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setUsername(user.username());
        authUserEntity.setPassword(pe.encode("12345"));
        authUserEntity.setEnabled(true);
        authUserEntity.setAccountNonExpired(true);
        authUserEntity.setAccountNonLocked(true);
        authUserEntity.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDaoSpring.createUser(authUserEntity);
        AuthorityEntity[] userAuthorities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(null); //!
                    ae.setAuthority(e);
                    return ae;
                }).toArray(AuthorityEntity[]::new);

        authorityUserDaoSpring.createUser(userAuthorities);
        return UserJson.fromEntity(
                userDaoSpring.createUser(UserEntity.fromJson(user)
                ));
    }

    //создание юзера с ChainedTransactionManager
    public UserJson createUserChainedTxManager(UserJson user) {
        return xaTransactionTemplateChained.execute(status -> {
            AuthUserEntity authUserEntity = new AuthUserEntity();
            authUserEntity.setUsername(user.username());
            authUserEntity.setPassword(pe.encode("12345"));
            authUserEntity.setEnabled(true);
            authUserEntity.setAccountNonExpired(true);
            authUserEntity.setAccountNonLocked(true);
            authUserEntity.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDao.createUser(authUserEntity);
            AuthorityEntity[] userAuthorities = Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    }).toArray(AuthorityEntity[]::new);

            authorityUserDao.createUser(userAuthorities);
            return UserJson.fromEntity(
                    userDao.createUser(UserEntity.fromJson(user)
                    ));
        });
    }
}
