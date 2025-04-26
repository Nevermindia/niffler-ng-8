package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userAuth.AuthUserEntity;
import guru.qa.niffler.data.entity.userAuth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.entity.userAuth.Authority;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserDataRepository;
import guru.qa.niffler.data.repository.impl.hibernate.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.hibernate.UserDataRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.jdbc.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.spring.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.jdbc.UserDataRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.spring.UserDataRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;
import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomUsername;

public class UsersDbClient implements UsersClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    //repository jdbc
    private final AuthUserRepository authUserRepositoryJdbc = new AuthUserRepositoryJdbc();
    private final UserDataRepository userDataRepositoryJdbc = new UserDataRepositoryJdbc();

    //repository spring jdbc
    private final AuthUserRepository authUserSpringRepository = new AuthUserRepositorySpringJdbc();
    private final UserDataRepository userDataSpringRepository = new UserDataRepositorySpringJdbc();

    //repository hibernate
    private final AuthUserRepository authUserHibernateRepository = new AuthUserRepositoryHibernate();
    private final UserDataRepository userDataHibernateRepository = new UserDataRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(), CFG.userdataJdbcUrl()
    );

    private final TransactionTemplate transactionTemplate = new TransactionTemplate(
            new JdbcTransactionManager(dataSource(CFG.authJdbcUrl())));


    //создание юзера с repository JDBC
    public UserJson xaCreateUserRepository(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUserEntity = authUserEntity(user);
            authUserRepositoryJdbc.create(authUserEntity);
            return UserJson.fromEntity(
                    userDataRepositoryJdbc.create(UserEntity.fromJson(user)
                    ));
        });
    }

    //создание юзера с repository Spring JDBC
    public UserJson xaCreateUserSpringRepository(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUserEntity = authUserEntity(user);

            authUserSpringRepository.create(authUserEntity);

            return UserJson.fromEntity(
                    userDataSpringRepository.create(UserEntity.fromJson(user)
                    ));
        });
    }

    //создание юзера с repository hibernate
    public UserJson xaCreateUserHibernateRepository(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUserEntity = authUserEntity(username, password);

            authUserHibernateRepository.create(authUserEntity);

            return UserJson.fromEntity(
                    userDataHibernateRepository.create(userEntity(username)
                    ));
        });
    }

    public void addFriend(UserJson user, UserJson friend) {
        xaTransactionTemplate.execute(() -> {
            userDataHibernateRepository.addFriend(UserEntity.fromJson(user), UserEntity.fromJson(friend));
            return null;
        });
    }

    public void addIncomeInvitation(UserJson targertUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userDataHibernateRepository.findById(targertUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                    String username = randomUsername();
                    AuthUserEntity authUser = authUserEntity(username, "12345");
                    authUserHibernateRepository.create(authUser);
                    UserEntity adressee = userDataHibernateRepository.create(userEntity(username));
                    userDataHibernateRepository.addIncomeRequest(targetEntity, adressee);
                    return null;
                });
            }
        }
    }

    public void addOutcomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userDataHibernateRepository.findById(targetUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUsername();
                            AuthUserEntity authUser = authUserEntity(username, "12345");
                            authUserHibernateRepository.create(authUser);
                            UserEntity adressee = userDataHibernateRepository.create(userEntity(username));
                            userDataHibernateRepository.addOutcomeRequest(targetEntity, adressee);
                            return null;
                        }
                );
            }
        }
    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    @NotNull
    private static AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setUsername(username);
        authUserEntity.setPassword(pe.encode(password));
        authUserEntity.setEnabled(true);
        authUserEntity.setAccountNonExpired(true);
        authUserEntity.setAccountNonLocked(true);
        authUserEntity.setCredentialsNonExpired(true);
        authUserEntity.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUserEntity);
                            ae.setAuthority(e);
                            return ae;
                        }).toList()
        );
        return authUserEntity;
    }

    @NotNull
    private static AuthUserEntity authUserEntity(UserJson user) {
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setUsername(user.username());
        authUserEntity.setPassword(pe.encode("12345"));
        authUserEntity.setEnabled(true);
        authUserEntity.setAccountNonExpired(true);
        authUserEntity.setAccountNonLocked(true);
        authUserEntity.setCredentialsNonExpired(true);
        authUserEntity.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUserEntity);
                            ae.setAuthority(e);
                            return ae;
                        }).toList()
        );
        return authUserEntity;
    }
}
