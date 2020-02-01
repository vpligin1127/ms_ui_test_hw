package com.lognex.uitests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

import static com.lognex.uitests.Controls.*;

public abstract class AbstractPage {

    WebDriver driver;
    WebDriverWait waitLoad;

    private String login = String.format("v%d", new Random().nextInt(1000));


    void goRegister() {

        String urlToGo = "https://online-api-4.testms.lognex.ru";
        driver.get(urlToGo);

        expectElement(waitLoad, "//input[@id='lable-login']");
        clickElementId(driver,"reglink");

        expectElement(waitLoad,"//input[@id='email']");

        fillElement(driver,"//input[@id='email']", "vpligin@moysklad.ru");
        fillElement(driver, "//div[@class='wrapper']//*[@id = 'company']", login);

        clickElementId(driver,"submit");

    }

    void createProduct() throws InterruptedException {
        clickElementXpath(driver, "//span[@title='Товары']/ancestor::table[contains(@class, 'menu-item')]");
        expectElement(waitLoad, "//span[@title='Товары и услуги']");
        clickElementXpath(driver,"//span[@title='Товары и услуги']");

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
