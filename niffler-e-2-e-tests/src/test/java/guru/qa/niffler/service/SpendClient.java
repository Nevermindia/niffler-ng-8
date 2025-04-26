package guru.qa.niffler.service;

import guru.qa.niffler.model.SpendJson;

public interface SpendClient {

    public SpendJson createSpendJdbc(SpendJson spend);
    public SpendJson createSpendSpring(SpendJson spend);
    public SpendJson createSpendHibernate(SpendJson spend);
}
