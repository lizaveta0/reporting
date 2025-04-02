import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import data.DataGenerator;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import pages.CardPage;
import org.openqa.selenium.TakesScreenshot;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.*;

@ExtendWith(CardDeliveryTest.ScreenshotOnFailureExtension.class)
public class CardDeliveryTest {

    private CardPage cardPage;
    private DataGenerator dataGenerator;
    String locale;
    String city;
    String date;
    String name;
    String phone;
    Boolean accept;

    @BeforeEach
    public void setUp() {
        Configuration.reportsFolder = "target/selenide/reports";
        open("http://localhost:9999/");
        cardPage = new CardPage();

        locale = "ru";
        UserInfo userInfo = DataGenerator.DeliveryCard.generateUser(locale);
        city = userInfo.getCity();
        date = DataGenerator.generateDate(3);
        name = userInfo.getName();
        phone = userInfo.getPhone();
        accept = userInfo.getAccept();

    }
    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] takeScreenshot() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    public static class ScreenshotOnFailureExtension implements TestWatcher {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            takeScreenshot();
        }
    }

    @Test
    public void testOrderFlowPositive() {

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована н " + date);

        date = DataGenerator.generateDate(3);
        cardPage.replanAndGetMessage(date, "У вас уже запланирована встреча на другую дату. Перепланировать?");

        cardPage.submitReplan();
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);
    }

    @Test
    public void testOrderFlowNegative() {
        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);

        name = "Тестовый Покупатель";
        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);
    }

    @Test
    public void testOrderFlowNegativeCity() {
        city = "";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForCity("Поле обязательно для заполнения");
    }

    @Test
    public void testOrderFlowNegativeCityIncorrect() {
        String localeCity = "en";
        city = generateAddress(localeCity);

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForCity("Доставка в выбранный город недоступна");
    }

    @Test
    public void testOrderFlowNegativeDate() {
        date = "1";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForDate("Неверно введена дата");
    }

    @Test
    public void testOrderFlowNegativeDateEmpty() {
        date = " ";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForDate("Неверно введена дата");
    }

    @Test
    public void testOrderFlowNegativeNameEmpty() {
        name = "";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForName("Поле обязательно для заполнения");
    }

    @Test
    public void testOrderFlowNegativeNameIncorrect() {
        String nameLocale = "en";
        name = generateName(nameLocale);

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForName("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.");
    }

    @Test
    public void testOrderFlowNegativePhoneEmpty() {
        phone = "";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForPhone("Поле обязательно для заполнения");
    }

    @Test
    public void testOrderFlowNegativeAgreement() {
        accept = false;

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.agreementIsInvalid();
    }


}
