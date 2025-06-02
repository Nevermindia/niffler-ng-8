package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.condition.SpendConditions.spend;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent<SpendingTable>{
    private final SearchField searchField = new SearchField();
    private final SelenideElement dateFilter = self.$("#period");
    private final SelenideElement currencyFilter = self.$("#currency");
    private final ElementsCollection menuItems = $$("ul[role=\"listbox\"]");
    private final SelenideElement deleteBtn = self.$("#delete");
    private final ElementsCollection tableRows = self.$("tbody").$$("tr");
    private final SelenideElement dialogWindow = $("div[role='dialog']");

    public SpendingTable() {
        super($("#spendings"));
    }

    @Step("Select period {0}")
    public SpendingTable selectPeriod(DataFilterValues period) {
        dateFilter.click();
        menuItems.findBy(text(period.toString())).click();
        return this;
    }

    @Step("Edit spending with description {0}")
    public EditSpendingPage editSpending(String description) {
        searchField.search(description);
        tableRows.find(text(description))
                .$$("td")
                .get(5)
                .click();
        return new EditSpendingPage();
    }

    @Step("Delete spending with description {0}")
    public MainPage deleteSpending(String description) {
        searchField.search(description);
        tableRows.find(text(description))
                .$$("td")
                .get(0)
                .click();
        deleteBtn.click();
        dialogWindow.$(byText("Delete")).click();
        return new MainPage();
    }

    @Step("Search spending with description {0}")
    public SpendingTable searchSpendingByDescription(String description) {
        searchField.search(description);
        return this;
    }

    @Step("check table contains spendings")
    public SpendingTable checkTableContains(String... expectedSpends) {
        for (String spend : expectedSpends){
            searchField.search(spend);
            tableRows.find(text(spend))
                    .should(visible);
        }
        return this;
    }

    @Step("Check table size is {0}")
    public SpendingTable checkTableSize(int expectedSize) {
        tableRows.should(size(expectedSize));
        return this;
    }

    @Step("Check spending table")
    @Nonnull
    public SpendingTable checkSpendTable(SpendJson... expectedSpends) {
        tableRows.shouldHave(spend(expectedSpends), Duration.ofSeconds(5));
        return this;
    }
}
