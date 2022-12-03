package ru.netology.data;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private DataHelper(){
    }

    static Faker faker = new Faker(new Locale ("ru"));
    static FakeValuesService fakeValuesService = new FakeValuesService(new Locale("ru"),
        new RandomService());
    public static String generateRandomCardNumber (String cardNumber) {
        return faker.number().digits(16);
    }
    public static String generateMonthYear(int month, int year, String pattern) {
        return LocalDate.now().plusMonths(month).plusYears(year).format(DateTimeFormatter.ofPattern(pattern));
    }
    public static String generateFullNameInRus() {
        Faker faker = new Faker(new Locale("ru"));
        String fullName = faker.name().fullName();
        return fullName;
    }
    public static String generateFullNameInEn() {
        Faker faker = new Faker(new Locale("en"));
        String fullName = faker.name().fullName();
        return fullName;
    }
    public static String generateRandomCVC (String cvc) {
        return faker.number().digits(3);
    }

    public static CardInfo getApprovedCard() {
        return new CardInfo("1111222233334444",
                generateMonthYear(0, 0, "MM"),generateMonthYear(0, 0,"YY"),
                generateFullNameInEn(), generateRandomCVC("en"));
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo("5555666677778888",
                generateMonthYear(0,0,"MM"),generateMonthYear(0,0,"YY"),
                generateFullNameInEn(), generateRandomCVC("en"));
    }

    public static CardInfo getEmptyCard(){
        return new CardInfo("","","","","");
    }

    public static CardInfo getRandomCardNumber(){
        return new CardInfo(generateRandomCardNumber(""),generateMonthYear(0,0,"MM")
                  ,generateMonthYear(0,0,"YY"), generateFullNameInEn(),generateRandomCVC(""));
    }

    public static String generateInvalidOwnerWith64characters(String locale, int length) {
        var str = fakeValuesService.regexify("[a-z]{34}").toUpperCase();
        return str.length() < length ? str : str.substring(0, length);
    }

    @Value
    public static class CardInfo {
        private String cardNumber;
        private String month;
        private String year;
        private String owner;
        private String cvc;
    }
}
