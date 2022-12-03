package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BuyPage {
    private SelenideElement heading = $$("h3.heading").findBy(Condition.text("Оплата по карте"));
    private ElementsCollection inputTypeText = $$(".input_type_text");
    private SelenideElement cardNumberField = inputTypeText.find(exactText("Номер карты")).$(".input__control");
    private SelenideElement monthField = inputTypeText.find(exactText("Месяц")).$(".input__control");
    private SelenideElement yearField = inputTypeText.find(exactText("Год")).$(".input__control");
    private SelenideElement ownerField = inputTypeText.find(exactText("Владелец")).$(".input__control");
    private SelenideElement cvcField = inputTypeText.find(exactText("CVC/CVV")).$(".input__control");
    private SelenideElement buttonContinue = $$(".button span.button__text").find(exactText("Продолжить"));
    private SelenideElement notificationError = $$(".notification__content")
            .find(text("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement notification = $(".notification__content");
    private ElementsCollection input = $$(".input");
    private SelenideElement errorCardNumber = input.findBy(text("Номер карты"));
    private SelenideElement errorMonth = input.findBy(text("Месяц"));
    private SelenideElement errorYear = input.findBy(text("Год"));
    private SelenideElement errorOwner = input.findBy(text("Владелец"));
    private SelenideElement errorCVC = input.findBy(text("CVC/CVV"));

    public BuyPage() {
        heading.shouldBe(visible);
    }

    public void inputData(DataHelper.CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getCardNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        ownerField.setValue(cardInfo.getOwner());
        cvcField.setValue(cardInfo.getCvc());
    }

    public void clickContinue(DataHelper.CardInfo cardInfo) {
        inputData(cardInfo);
        buttonContinue.click();
    }

    public void shouldNotification(DataHelper.CardInfo cardInfo, String expected) {
        clickContinue(cardInfo);
            notification.shouldHave(text(expected), Duration.ofSeconds(15));
            notification.shouldBe(visible);
   }

    public void shouldNotificationError(DataHelper.CardInfo cardInfo, String expected) {
        clickContinue(cardInfo);
        notificationError.shouldHave(text(expected), Duration.ofSeconds(15));
        notificationError.shouldBe(visible);
    }

    public void getCardNumberError(String expected) {
        errorCardNumber.$(".input__sub").shouldHave(text(expected)).shouldBe(visible);
    }

    public void getMonthError(String expected) {
        errorMonth.$(".input__sub").shouldHave(text(expected)).shouldBe(visible);
    }

    public void getYearError(String expected) {
        errorYear.$(".input__sub").shouldHave(text(expected)).shouldBe(visible);
    }

    public void getOwnerError(String expected) {
        errorOwner.$(".input__sub").shouldHave(text(expected)).shouldBe(visible);
    }

    public void getCVCError(String expected) {
        errorCVC.$(".input__sub").shouldHave(text(expected)).shouldBe(visible);
    }

    public void checkValueCardNumber(String expected) {
        cardNumberField.shouldHave(Condition.attribute("value", expected)).shouldBe(visible);
    }

    public void checkValueMonth(String expected) {
        monthField.shouldHave(Condition.attribute("value", expected)).shouldBe(visible);
    }

    public void checkValueYear(String expected) {
        yearField.shouldHave(Condition.attribute("value", expected)).shouldBe(visible);
    }

    public void checkValueOwner(String expected) {
        ownerField.shouldHave(Condition.attribute("value", expected)).shouldBe(visible);
    }

    public void checkValueCVC(String expected) {
        cvcField.shouldHave(Condition.attribute("value", expected)).shouldBe(visible);
    }
}