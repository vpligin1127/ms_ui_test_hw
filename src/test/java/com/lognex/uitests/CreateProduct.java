package com.lognex.uitests;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.lognex.uitests.Controls.*;

public class CreateProduct extends AbstractPage {

    CreateProduct(WebDriver driver, WebDriverWait waitLoad) {
        this.driver = driver;
        this.waitLoad = waitLoad;

    }

    boolean checkProduct() {
        //Проверяем, что есть товар "мандарины". Если нет, возвращаем false
        //Заходим на вкладку Товары - Товары и услуги
        clickElementXpath(driver, "//span[@title='Товары']/ancestor::table[contains(@class, 'menu-item')]");
        expectElement(waitLoad, "//span[@title='Товары и услуги']");
        clickElementXpath(driver,"//span[@title='Товары и услуги']");

        //Проверяем, есть ли мадарины
        try {
            findElement(driver, "//div[@title='мандарины']");
        }
        catch (NoSuchElementException e) {
            return false;
        }

        return true;

    }

    @Override
    void createProduct() throws InterruptedException {
        clickElementXpath(driver, "//div[@role='button'][descendant::span[text()='Товар']]");
        Thread.sleep(2000);

        fillElement(driver,
                "//tr[@class='tutorial-stage-sales-fourth-step']//input[@type='text']",
                "мандарины");

        clickElementXpath(driver, "//div[@role='button']/descendant::span[text()='Сохранить']");
        Thread.sleep(2000);

        clickElementXpath(driver,"//div[@role='button']/descendant::span[text()='Закрыть']");


    }


}
