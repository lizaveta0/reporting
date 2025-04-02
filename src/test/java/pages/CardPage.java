package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static java.time.Duration.ofSeconds;

public class CardPage {

    private SelenideElement inputCity = $("[data-test-id='city'] input");
    private SelenideElement inputCityError = $(By.xpath("//*[@data-test-id='city' and contains(@class, 'input_invalid')]//span[@class='input__sub']"));

    private SelenideElement inputDate = $("[data-test-id='date'] input");
    private SelenideElement inputDateError = $(By.xpath("//*[@data-test-id='date']//span[contains(@class, 'input_invalid')]"));

    private SelenideElement inputName = $("[data-test-id='name'] input");
    private SelenideElement inputNameError = $(By.xpath("//*[@data-test-id='name' and contains(@class, 'input_invalid')]//span[@class='input__sub']"));

    private SelenideElement inputPhone = $("[data-test-id='phone'] input");
    private SelenideElement inputPhoneError = $(By.xpath("//*[@data-test-id='phone' and contains(@class, 'input_invalid')]//span[@class='input__sub']"));

    private SelenideElement agreemetCheck = $("[data-test-id='agreement'] span.checkbox__box");
    private SelenideElement agreemetText = $("[data-test-id='agreement']");

    private SelenideElement submitButton = $(".button");
    private SelenideElement message = $("[data-test-id='success-notification'] div.notification__content");
    private SelenideElement messageReplan = $("[data-test-id='replan-notification'] div.notification__content");
    private SelenideElement submitReplan = $("[data-test-id='replan-notification'] div.notification__content button");

    public CardPage() {
    }


    //Заполнение формы
    @Step("Заполнили форму заказа карты")
    public void fillOrderForm(String city, String date, String name, String phone, Boolean accept) {
        // Заполнение полей
        inputCity.setValue(city);

        inputDate.doubleClick();
        inputDate.sendKeys(date);

        inputName.setValue(name);
        inputPhone.setValue(phone);

        if (accept) {
            agreemetCheck.click();
        }
        submitButton.click();
    }

    @Step("Изменили дату встречи на {date} и проверили, что текст сообщения из модального окна равен {text}")
    public void replanAndGetMessage(String date, String text) {
        inputDate.doubleClick();
        inputDate.sendKeys(date);
        submitButton.click();
        messageReplan.shouldBe(visible, ofSeconds(15)).shouldHave(text(text));
    }

    @Step("Проверили, что сообщение содержит текст {message}")
    public void checkOrderSuccessMessage(String message) {
        this.message.should(visible, ofSeconds(15)).shouldBe(text(message));
    }

    @Step("Проверили, что сообщение содержит текст {error}")
    public void checkErrorMessageForCity(String error) {
        inputCityError.shouldBe(visible, text(error));
    }

    @Step("Проверили, что сообщение содержит текст {error}")
    public void checkErrorMessageForDate(String error) {
        inputDateError.shouldBe(visible, text(error));
    }

    @Step("Проверили, что сообщение содержит текст {error}")
    public void checkErrorMessageForName(String error) {
        inputNameError.shouldBe(visible, text(error));
    }

    @Step("Проверили, что сообщение содержит текст {error}")
    public void checkErrorMessageForPhone(String error) {
        inputPhoneError.shouldBe(visible, text(error));
    }

    @Step("Проверили, что отмечен чек-бокс Cогласие")
    public void agreementIsInvalid() {
        agreemetText.shouldHave(visible);
    }

    @Step("Подтвердили перепланирование встречи")
    public void submitReplan() {
        submitReplan.click();
    }
}
