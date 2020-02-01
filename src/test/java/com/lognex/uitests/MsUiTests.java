package com.lognex.uitests;

import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.lognex.uitests.Controls.clickElementXpath;
import static com.lognex.uitests.Controls.findElement;
import static org.junit.Assert.assertFalse;

public class MsUiTests {


    @Test
    public void testRegistration() throws InterruptedException {

        WebDriver driver = new ChromeDriver();
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);

        //Проверяем вход в аккаунт. Регистрируемся со случайным именем
        RegPage registration = new RegPage(driver, waitLoad);

        registration.goRegister();

        Thread.sleep(13000);

        //Проверяем, что находимся на стартовой странице, если нет - тест не проходит.
        try {
            findElement(driver, "//div[@class='login-new']");
        } catch (NoSuchElementException e) {
            assertFalse("Регистрация не проходит!", true);
        }

        //Меняем пароль
        registration.changePassword();

        driver.quit();

    }

    @Test
    public void testCreateProduct() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);

        CreateProduct productCreation = new CreateProduct(driver, waitLoad);

        productCreation.goRegister();

        Thread.sleep(13000);

        //Проверяем, что находимся на стартовой странице, если нет - тест не проходит.
        try {
            findElement(driver, "//div[@class='login-new']");
        } catch (NoSuchElementException e) {
            assertFalse("Регистрация не проходит!", true);
        }

        if (!productCreation.checkProduct()) {
            productCreation.createProduct();
        }

        //Обновляем список товаров
        clickElementXpath(driver, "//img[@class='b-tool-button']");
        Thread.sleep(2000);

        assertFalse("Товар не создается!", productCreation.checkProduct());

        driver.quit();
    }


    @Test
    public void testCustomerOrder() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);

        CreateOrder orderCreation = new CreateOrder(driver, waitLoad);

        orderCreation.goRegister();
        Thread.sleep(13000);

        //Проверяем, что находимся на стартовой странице, если нет - тест не проходит.
        try {
            findElement(driver, "//div[@class='login-new']");
        } catch (NoSuchElementException e) {
            assertFalse("Регистрация не проходит!", true);
        }

        orderCreation.createProduct();

        if (!orderCreation.checkOrder()) {
            orderCreation.createOrder();
        }

        //Обновляем список заказов
        clickElementXpath(driver, "//img[@class='b-tool-button']");
        Thread.sleep(2000);

        assertFalse("Заказ не создается!", orderCreation.checkOrder());

        driver.quit();

    }

}

