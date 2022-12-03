package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;

public class SalesPage {
    private ElementsCollection button = $$(".button span.button__text");
    private SelenideElement buyButton = button.findBy(Condition.text("Купить"));

    public BuyPage getBuyPage() {
        buyButton.click();
        return new BuyPage();
    }
}
