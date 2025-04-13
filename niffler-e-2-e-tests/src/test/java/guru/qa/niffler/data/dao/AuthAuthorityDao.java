package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userAuth.AuthorityEntity;

public interface AuthAuthorityDao {
    AuthorityEntity createUser(AuthorityEntity user);
}