package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField> {

    private final SelenideElement clearBtn = $("#input-clear");

    public SearchField() {
        super($("input[aria-label=\"search\"]"));
    }

    @Step("Search by query {0}")
    @Nonnull
    public SearchField search(String query) {
        self.setValue(query).pressEnter();
        return this;
    }

    @Step("Clear search input if not empty")
    @Nonnull
    public SearchField clearIfNotEmpty() {
        if (!self.getValue().isEmpty()) {
            clearBtn.click();
        }
        return this;
    }
}
