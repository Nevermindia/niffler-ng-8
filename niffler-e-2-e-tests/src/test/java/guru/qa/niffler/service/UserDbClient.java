package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.data.entity.userAuth.AuthUserEntity;
import guru.qa.niffler.data.entity.userAuth.Authority;
import guru.qa.niffler.data.entity.userAuth.AuthorityEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    private final AuthUserDaoJdbc authUserDao = new AuthUserDaoJdbc();
//    private final AuthAuthorityDaoJdbc authorityUserDao = new AuthAuthorityDaoJdbc();
//    private final UserDaoJdbc userDao = new UserDaoJdbc();
    private final AuthUserDaoSpringJdbc authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDaoSpringJdbc authorityUserDao = new AuthAuthorityDaoSpringJdbc();
    private final UserDaoSpringJdbc userDao = new UserDaoSpringJdbc();
    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(), CFG.userdataJdbcUrl()
    );

    private final TransactionTemplate transactionTemplate = new TransactionTemplate(
            new JdbcTransactionManager(dataSource(CFG.authJdbcUrl())));

    public UserJson createUserSpringJdbc(UserJson user) {
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
}
