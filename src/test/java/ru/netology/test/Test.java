package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.data.DataBaseHelper;
import ru.netology.data.DataHelper;
import ru.netology.page.SalesPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanAll() {
        DataBaseHelper.cleanAll();
    }

    @org.junit.jupiter.api.Test
    void shouldSuccessBuyOfApprovedCard() {
        var salesPage = new SalesPage();
        var approvedCard = DataHelper.getApprovedCard();
        var buyPage = salesPage.getBuyPage();
        buyPage.shouldNotification(approvedCard, "Операция одобрена банком.");
        assertEquals(DataBaseHelper.getStatus(), "APPROVED");
    }

    @org.junit.jupiter.api.Test
    void shouldSuccessBuyDeclinedCard() {
        var salesPage = new SalesPage();
        var declinedCard = DataHelper.getDeclinedCard();
        var buyPage = salesPage.getBuyPage();
        buyPage.shouldNotification(declinedCard, "Операция одобрена банком.");
        assertEquals(DataBaseHelper.getStatus(), "DECLINED");
    }

    @org.junit.jupiter.api.Test
    void allFieldsAreEmpty() {
        var salesPage = new SalesPage();
        var emptyCard = DataHelper.getEmptyCard();
        var buyPage = salesPage.getBuyPage();
        buyPage.clickContinue(emptyCard);
        buyPage.getCardNumberError("Неверный формат");
        buyPage.getMonthError("Неверный формат");
        buyPage.getYearError("Неверный формат");
        buyPage.getOwnerError("Неверный формат");
        buyPage.getCVCError("Неверный формат");
    }

    @org.junit.jupiter.api.Test
    void randomCardNumber() {
        var salesPage = new SalesPage();
        var randomCardNumber = DataHelper.getRandomCardNumber();
        var buyPage = salesPage.getBuyPage();
        buyPage.shouldNotificationError(randomCardNumber, "Ошибка! Банк отказал в проведении операции.");
    }

    @ParameterizedTest
    @CsvSource({
            "Неверный формат, ",                    //пустое поле
            "Неверный формат, 1111 2222 3333 444",  //15 цифр
            "Неверный формат, 1"                    //1 цифра
    })
    void shouldInvalidFieldOfCardNumberTests(String expected, String cardNumber) {
        var salesPage = new SalesPage();
        var approvedCard = DataHelper.getApprovedCard();
        var newCard = new DataHelper.CardInfo(cardNumber,
                approvedCard.getMonth(), approvedCard.getYear(), approvedCard.getOwner(), approvedCard.getCvc());
        var buyPage = salesPage.getBuyPage();
        buyPage.clickContinue(newCard);
        buyPage.getCardNumberError(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "Неверный формат, ",                                      //пустое поле
            "Неверно указан срок действия карты, 00",                 //00
            "Неверный формат, 0",                                     //1 цифра
            "Неверно указан срок действия карты, 11"                  //Прошлый месяц от текущей даты
    })
    void shouldInvalidFieldOfMonthTests(String expected, String month) {
        var salesPage = new SalesPage();
        var approvedCard = DataHelper.getApprovedCard();
        var newCard = new DataHelper.CardInfo(approvedCard.getCardNumber(), month, approvedCard.getYear(),
                approvedCard.getOwner(), approvedCard.getCvc());
        var buyPage = salesPage.getBuyPage();
        buyPage.clickContinue(newCard);
        buyPage.getMonthError(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "Неверный формат, ",                                      //пустое поле
            "Истёк срок действия карты, 00",                          //00
            "Неверный формат, 0",                                     //1 цифра
            "Истёк срок действия карты, 21"                           //Прошлый год от текущей даты
    })
    void shouldInvalidFieldOfYearTests(String expected, String year) {
        var salesPage = new SalesPage();
        var approvedCard = DataHelper.getApprovedCard();
        var newCard = new DataHelper.CardInfo(approvedCard.getCardNumber(), approvedCard.getMonth(),
                year, approvedCard.getOwner(), approvedCard.getCvc());
        var buyPage = salesPage.getBuyPage();
        buyPage.clickContinue(newCard);
        buyPage.getYearError(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "Agafonov Ilya, Agafonov Ilya Mihailovich",               //больше одного пробела
            "Agafonov Ilya, Агафонов Илья",                           //имя и фамилия на кириллице
            "Agafonov Ilya, agafonov ilya",                           //маленькая буква вначале Имя или Фамилия
            "Agafonov Ilya, Agafonov",                                //одно слово
            "Agafonov Ilya, Agafonov№;%:",                            //спецсимволы

    })
    void shouldInvalidFieldOfOwnerTests(String expected, String owner) {
        var salesPage = new SalesPage();
        var approvedCard = DataHelper.getApprovedCard();
        var newCard = new DataHelper.CardInfo(approvedCard.getCardNumber(),
                approvedCard.getMonth(), approvedCard.getYear(), owner, approvedCard.getCvc());
        var buyPage = salesPage.getBuyPage();
        buyPage.inputData(newCard);
        buyPage.checkValueOwner(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "Поле обязательно для заполнения, ",                      //пустое поле

    })
    void shouldEmptyFieldOfOwnerTests(String expected, String owner) {
        var salesPage = new SalesPage();
        var approvedCard = DataHelper.getApprovedCard();
        var newCard = new DataHelper.CardInfo(approvedCard.getCardNumber(),
                approvedCard.getMonth(), approvedCard.getYear(), owner, approvedCard.getCvc());
        var buyPage = salesPage.getBuyPage();
        buyPage.clickContinue(newCard);
        buyPage.getOwnerError(expected);
    }

    @org.junit.jupiter.api.Test
    void shouldFieldOfOwnerWith64Characters() {
        var salesPage = new SalesPage();
        var approvedCard = DataHelper.getApprovedCard();
        var owner = DataHelper.generateInvalidOwnerWith64characters("en", 64);
        var newCard = new DataHelper.CardInfo(approvedCard.getCardNumber(),
                approvedCard.getMonth(), approvedCard.getYear(), owner, approvedCard.getCvc());
        var buyPage = salesPage.getBuyPage();
        buyPage.inputData(newCard);

        var expected = owner.length() < 64 ? owner : owner.substring(0, 64);
        buyPage.checkValueOwner(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "Неверный формат, ",                                      //пустое поле
            "Неверный формат, 0",                                     //1 цифра
            "Неверный формат, 00"                                     //2 цифры
    })
    void shouldInvalidFieldOfCVCTests(String expected, String cvc) {
        var salesPage = new SalesPage();
        var approvedCard = DataHelper.getApprovedCard();
        var newCard = new DataHelper.CardInfo(approvedCard.getCardNumber(),
                approvedCard.getMonth(), approvedCard.getYear(), approvedCard.getOwner(), cvc);
        var buyPage = salesPage.getBuyPage();
            buyPage.clickContinue(newCard);
            buyPage.getCVCError(expected);
    }
}



