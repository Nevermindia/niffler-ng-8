package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement sumInput = $("#amount");
  private final SelenideElement submitBtn = $("#save");
  private final SelenideElement categoryInput = $("#category");

  private final Calendar calendar = new Calendar();

  @Step("Edit description")
  @Nonnull
  public MainPage editDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    submitBtn.click();
    return new MainPage();
  }

  @Step("Edit sum")
  @Nonnull
  public MainPage editSum(String sum) {
    sumInput.clear();
    sumInput.setValue(sum);
    submitBtn.click();
    return new MainPage();
  }

  @Step("Add new spending")
  @Nonnull
  public MainPage addNewSpending(SpendJson spend) {
    sumInput.setValue(String.valueOf(spend.amount())).pressEnter();
    categoryInput.setValue(spend.category().name()).pressEnter();
    calendar.selectDateInCalendar(spend.spendDate());
    descriptionInput.setValue(spend.description()).pressEnter();
    return new MainPage();
  }
}
