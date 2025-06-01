package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.model.Bubble;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.test.web.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.condition.SpendConditions.spend;
import static guru.qa.niffler.condition.StatConditions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

    private final Header header = new Header();
    private final SpendingTable spendingTable = new SpendingTable();

    private final ElementsCollection headerElements = $$(".MuiTypography-h5");
    private final SelenideElement diagramElement = $("canvas[role='img']");
    private final ElementsCollection statisticCells = $$("#legend-container li");

    @Nonnull
    public Header getHeader() {
        return header;
    }

    @Nonnull
    public SpendingTable getSpendingTable() {
        return spendingTable;
    }


    @Step("Delete spending with description {0}")
    @Nonnull
    public MainPage deleteSpending(String spendingDescription) {
        getSpendingTable().deleteSpending(spendingDescription);
        return new MainPage();
    }

    @Step("Check Main page is opened")
    @Nonnull
    public MainPage checkMainPageIsOpened() {
        headerElements.find(text("Statistics")).shouldBe(visible);
        headerElements.find(text("History of Spendings")).shouldBe(visible);
        headerElements.find(text("Niffler")).shouldBe(visible);
        return this;
    }

    @SneakyThrows
    @Step("Check statistic diagram")
    @Nonnull
    public MainPage checkStatisticDiagram(BufferedImage expected) {
        Selenide.sleep(3000);
        BufferedImage actual = ImageIO.read(diagramElement.screenshot());
        assertFalse(new ScreenDiffResult(expected, actual), "Screen comparison failure");
        return this;
    }

    @Step("Check statistic cell")
    @Nonnull
    public MainPage checkStatisticDiagramInfo(List<String> spendInfo) {
        for (String info : spendInfo){
            statisticCells.findBy(text(info)).shouldBe(visible);
        }
        return this;
    }

    @Step("Check statistic bubble color")
    @Nonnull
    public MainPage checkBubbles(Color... expectedColor) {
        statisticCells.shouldHave(color(expectedColor));
        return this;
    }

    @Step("Check stat bubbles")
    @Nonnull
    public MainPage checkStatBubbles(Bubble... expectedBubbles) {
        statisticCells.shouldHave(bubble(expectedBubbles));
        return this;
    }

    @Step("Check stat bubbles in any order")
    @Nonnull
    public MainPage checkStatBubblesInAnyOrder(Bubble... expectedBubbles) {
        statisticCells.shouldHave(bubblesInAnyOrder(expectedBubbles));
        return this;
    }

    @Step("Check stat bubbles contains")
    @Nonnull
    public MainPage checkStatBubblesContains(Bubble... expectedBubbles) {
        statisticCells.shouldHave(bubblesContains(expectedBubbles));
        return this;
    }
}
