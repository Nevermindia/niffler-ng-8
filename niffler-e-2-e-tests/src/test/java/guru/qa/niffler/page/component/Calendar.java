package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.time.Month;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.util.Calendar.*;

public class Calendar {

    private SelenideElement self = $(".MuiDateCalendar-root");
    private final SelenideElement chooseDateBtn = $("button[aria-label*=\"Choose date\"]");
    private final ElementsCollection chooseYear = self.$$(".MuiPickersYear-yearButton");
    private final SelenideElement currentMonthAndYear = self.$(".MuiPickersFadeTransitionGroup-root");
    private final SelenideElement previousMonthBtn = self.$("[data-testid=\"ArrowLeftIcon\"]");
    private final SelenideElement nextMonthBtn = self.$("[data-testid=\"ArrowRightIcon\"]");
    private final ElementsCollection daysInMonth = $$(".MuiPickersSlideTransition-root button");

    public Calendar(SelenideElement self) {
        this.self = self;
    }

    public Calendar() {
    }

    @Step("Select date in calendar")
    public void selectDateInCalendar(Date date) {
        java.util.Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        chooseDateBtn.click();
        self.shouldBe(visible);

        //choose year
        currentMonthAndYear.click();
        chooseYear.findBy(text(String.valueOf(cal.get(YEAR)))).click();

        //choose month
        int monthIndex = cal.get(MONTH);
        int currentMonthIndex = Month.valueOf(currentMonthAndYear.getText().split(" ")[0].toUpperCase()).ordinal();
        while (monthIndex<currentMonthIndex){
            previousMonthBtn.click();
            Selenide.sleep(2000);
            currentMonthIndex = Month.valueOf(currentMonthAndYear.getText().split(" ")[0].toUpperCase()).ordinal();
        }
        while (currentMonthIndex<monthIndex){
            nextMonthBtn.click();
            Selenide.sleep(2000);
            currentMonthIndex = Month.valueOf(currentMonthAndYear.getText().split(" ")[0].toUpperCase()).ordinal();
        }

        //choose day
        daysInMonth.findBy(text(String.valueOf(cal.get(DAY_OF_MONTH)))).click();
    }
}
