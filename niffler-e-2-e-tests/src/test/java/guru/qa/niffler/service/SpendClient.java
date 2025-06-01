package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendClient {

    @Nonnull
    public SpendJson createSpend(SpendJson spend);

    @Nonnull
    public CategoryJson createCategory(CategoryJson categoryJson);
}
