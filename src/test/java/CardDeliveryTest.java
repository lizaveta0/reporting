import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataGenerator;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.CardPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.*;

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
        SelenideLogger.addListener("allure", new AllureSelenide());

        locale = "ru";
        UserInfo userInfo = DataGenerator.DeliveryCard.generateUser(locale);
        city = userInfo.getCity();
        date = DataGenerator.generateDate(3);
        name = userInfo.getName();
        phone = userInfo.getPhone();
        accept = userInfo.getAccept();

    }


    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @Step("Позитивный сценарий заказа карты")
    public void testOrderFlowPositive() {

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);

        date = DataGenerator.generateDate(3);
        cardPage.replanAndGetMessage(date, "У вас уже запланирована встреча на другую дату. Перепланировать?");

        cardPage.submitReplan();
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);
    }

    @Test
    @Step("Негативный сценарий заказа карты")
    public void testOrderFlowNegative() {
        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);

        name = "Тестовый Покупатель";
        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);
    }

    @Test
    @Step("Негативный сценарий заказа карты. Пустое значение в поле Город")
    public void testOrderFlowNegativeCity() {
        city = "";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForCity("Поле обязательно для заполнения");
    }

    @Test
    @Step("Негативный сценарий заказа карты. Некорректное значение в поле Город")
    public void testOrderFlowNegativeCityIncorrect() {
        String localeCity = "en";
        city = generateAddress(localeCity);

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForCity("Доставка в выбранный город недоступна");
    }

    @Test
    @Step("Негативный сценарий заказа карты. Некорректное значение в поле Дата")
    public void testOrderFlowNegativeDate() {
        date = "1";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForDate("Неверно введена дата");
    }

    @Test
    @Step("Негативный сценарий заказа карты. Пустое значение в поле Город")
    public void testOrderFlowNegativeDateEmpty() {
        date = " ";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForDate("Неверно введена дата");
    }

    @Test
    @Step("Негативный сценарий заказа карты. Пустое значение в поле ФИО")
    public void testOrderFlowNegativeNameEmpty() {
        name = "";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForName("Поле обязательно для заполнения");
    }

    @Test
    @Step("Негативный сценарий заказа карты. Некорректное значение в поле ФИО")
    public void testOrderFlowNegativeNameIncorrect() {
        String nameLocale = "en";
        name = generateName(nameLocale);

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForName("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.");
    }

    @Test
    @Step("Негативный сценарий заказа карты. Пустое значение в поле Телефон")
    public void testOrderFlowNegativePhoneEmpty() {
        phone = "";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForPhone("Поле обязательно для заполнения");
    }

    @Test
    @Step("Негативный сценарий заказа карты. Неактивен чек бокс Согласие")
    public void testOrderFlowNegativeAgreement() {
        accept = false;

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.agreementIsInvalid();
    }


}
