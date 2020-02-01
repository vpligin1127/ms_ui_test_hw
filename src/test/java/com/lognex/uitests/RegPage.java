package com.lognex.uitests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

import static com.lognex.uitests.Controls.*;

class RegPage {

    RegPage(WebDriver driver, WebDriverWait waitLoad) {
        this.driver = driver;
        this.waitLoad = waitLoad;

    }

    WebDriver driver;
    WebDriverWait waitLoad;

    private String urlToGo = "https://online-api-4.testms.lognex.ru";
    private String login = String.format("v%d", new Random().nextInt(100));
    private String password = "123123";


    void goRegister() {

        driver.get(urlToGo);

        expectElement(waitLoad, "//input[@id='lable-login']");
        clickElementId(driver,"reglink");

        expectElement(waitLoad,"//input[@id='email']");

        fillElement(driver,"//input[@id='email']", "vpligin@moysklad.ru");
        fillElement(driver, "//div[@class='wrapper']//*[@id = 'company']", login);

        clickElementId(driver,"submit");

    }


    public void changePassword() throws InterruptedException {
        //зайти в Настройки - Сотрудники
        expectElement(waitLoad,"//div[@class='login-new']");
        clickElementXpath(driver,"//table[@title='Аккаунт']");
        clickElementXpath(driver,"//div[contains(@class, 'user-menu-popup-button')]//td[text()='Настройки']");
        clickElementXpath(driver,"//a[contains(@class,'sidebar-menu')][span[@title='Сотрудники']]");

        expectElement(waitLoad,"//div[@title='vpligin@moysklad.ru']");
        clickElementXpath(driver,"//div[@title='vpligin@moysklad.ru']");

        //Ввести в два поля ввода пароль 123123 и нажать на кнопку сохранить
        expectElement(waitLoad,"//input[@type='password']");
        fillElement(driver,"(//input[@type='password'])[1]", password);
        fillElement(driver,"(//input[@type='password'])[2]", password);

        clickElementXpath(driver,"//span[text()='Сохранить']/ancestor::div[@role='button']");
    }


}
