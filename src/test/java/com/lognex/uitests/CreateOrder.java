package com.lognex.uitests;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.lognex.uitests.Controls.*;


public class CreateOrder extends AbstractPage {

    CreateOrder(WebDriver driver, WebDriverWait waitLoad) {
        this.driver = driver;
        this.waitLoad = waitLoad;

    }

    boolean checkOrder() {
        //Проверка наличия заказа
        clickElementXpath(driver, "//span[@title='Продажи']/ancestor::table[contains(@class, 'menu-item')]");

        expectElement(waitLoad, "//span[@title='Заказы покупателей']");
        clickElementXpath(driver, "//span[@title='Заказы покупателей']");

        try {
            findElement(driver, "//a[contains(@href, '#customerorder')]");
        }
        catch (NoSuchElementException e) {
            return false;
        }

        return true;
    }

    void createOrder() throws InterruptedException {
        clickElementXpath(driver, "//span[@title='Продажи']/ancestor::table[contains(@class, 'menu-item')]");

        expectElement(waitLoad, "//span[@title='Заказы покупателей']");
        clickElementXpath(driver, "//span[@title='Заказы покупателей']");

        //Создание заказа для контрагента "Покупатель" с одной позицией - мандарины
        clickElementXpath(driver, "//div[@role='button'][descendant::span[text()='Заказ']]");
        Thread.sleep(2000);

        clickElementXpath(driver,
                "(//td[@class='widget']//div[@class='load-button tutorial-selector-image'])[4]");
        Thread.sleep(2000);

        clickElementXpath(driver, "//div[@class='popupContent']//div[@title='ООО \"Покупатель\"']");
        Thread.sleep(2000);

        fillElement(driver,
                "//input[contains(@placeholder, 'Добавить позицию')]",
                "мандарины");
        Thread.sleep(2000);

        clickElementXpath(driver, "//div[@role='button'][descendant::span[text()='Сохранить']]");
        Thread.sleep(2000);

        clickElementXpath(driver, "//div[@role='button'][descendant::span[text()='Закрыть']]");

    }

}


