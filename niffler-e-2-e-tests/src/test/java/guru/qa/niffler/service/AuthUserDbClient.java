package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.data.entity.userAuth.AuthUserEntity;
import guru.qa.niffler.data.entity.userAuth.Authority;
import guru.qa.niffler.data.entity.userAuth.AuthorityEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Arrays;

import static guru.qa.niffler.data.Databases.dataSource;

public class AuthUserDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setUsername(user.username());
        authUserEntity.setPassword(pe.encode("12345"));
        authUserEntity.setEnabled(true);
        authUserEntity.setAccountNonExpired(true);
        authUserEntity.setAccountNonLocked(true);
        authUserEntity.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).createUser(authUserEntity);
        AuthorityEntity[] userAuthorities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }).toArray(AuthorityEntity[]::new);


        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).createUser(userAuthorities);
       return UserJson.fromEntity(new UserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl())).createUser(
               UserEntity.fromJson(user)
       ));
    }

}
