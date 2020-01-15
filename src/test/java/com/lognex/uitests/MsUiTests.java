package com.lognex.uitests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

import static org.junit.Assert.assertFalse;

public class MsUiTests {

    @Test
    public void testRegistration() throws InterruptedException {
        //Проверяем вход в аккаунт. Регистрируемся со случайным именем

        WebDriver driver = new ChromeDriver();
        driver.get("https://online-api-4.testms.lognex.ru");
        //Формируем логин и пароль
        String login = String.format("v%d", new Random().nextInt(100));
        String password = "123123";

        //Формируем шифрованную строку для http-запросов по Remap (не используется)
        //String originalInput = login+":"+password;
        //String credentials = "Basic "+ Base64.getEncoder().encodeToString(originalInput.getBytes());

        //Регистрируемся
        goRegister(driver, login);

        Thread.sleep(10000);

        //Проверяем, что находимся на стартовой странице, если нет - тест не проходит.
        try {
            driver.findElement(By.xpath("//div[@class='login-new']"));
        } catch (NoSuchElementException e) {
            assertFalse("Регистрация не проходит!", true);
        }

        //Меняем пароль
        changePassword(driver, password);

        //Проверяем есть ли продукт мандарины, если нет - создаем
        testCreateProduct(driver);

        //Проверяем, есть ли заказ покупателя, если нет - создаем
        testCustomerOrder(driver);

    }


    public void goRegister(WebDriver driver, String login) {
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='lable-login']")));

        driver.findElement(By.id("reglink")).click();

        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='email']")));
        WebElement emailInput = driver.findElement(By.xpath("//input[@id='email']"));
        emailInput.sendKeys("vpligin@moysklad.ru");

        WebElement companyInput = driver.findElement(By.xpath("//div[@class='wrapper']//*[@id = 'company']"));
        companyInput.sendKeys(login);

        driver.findElement(By.xpath("//button[@id='submit']")).click();


    }

    public void changePassword(WebDriver driver, String password) {
        //зайти в Настройки - Сотрудники
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-new']")));
        driver.findElement(By.xpath("//table[@title='Аккаунт']")).click();
        driver.findElement(By.xpath("//div[contains(@class, 'user-menu-popup-button')]//td[text()='Настройки']")).click();
        driver.findElement(By.xpath("//a[contains(@class,'sidebar-menu')][span[@title='Сотрудники']]")).click();

        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@title='vpligin@moysklad.ru']")));
        driver.findElement(By.xpath("//div[@title='vpligin@moysklad.ru']")).click();

        //Ввести в два поля ввода пароль 123123 и нажать на кнопку сохранить
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
        WebElement passwordInput = driver.findElement(By.xpath("(//input[@type='password'])[1]"));
        passwordInput.sendKeys(password);

        WebElement passwordCheckInput = driver.findElement(By.xpath("(//input[@type='password'])[2]"));
        passwordCheckInput.sendKeys(password);

        driver.findElement(By.xpath("//span[text()='Сохранить']/ancestor::div[@role='button']")).click();
    }


    public void testCreateProduct(WebDriver driver) throws InterruptedException {

        //Проверяем, что есть товар "мандарины". Если нет, то создаем его вручную
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-new']")));

        //Заходим на вкладку Товары - Товары и услуги
        driver.findElement(By.xpath("//span[@title='Товары']/ancestor::table[contains(@class, 'menu-item')]")).click();

        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@title='Товары и услуги']")));
        driver.findElement(By.xpath("//span[@title='Товары и услуги']")).click();

        //Проверяем, есть ли мадарины, если нет - создаем, вызвав метод
        try {
            driver.findElement(By.xpath("//div[@title='мандарины']")).isDisplayed();
        }
        catch (NoSuchElementException e) {
            goCreateProductUI(driver);
        }

        //Обновляем список товаров
        driver.findElement(By.xpath("//img[@class='b-tool-button']")).click();
        Thread.sleep(2000);

        //Проверяем снова
        try {
            driver.findElement(By.xpath("//div[@title='мандарины']")).isDisplayed();
        }
        catch (NoSuchElementException e) {
            assertFalse("Товар не создался", true);
        }

    }

    public void goCreateProductUI(WebDriver driver) throws InterruptedException {
        driver.findElement(By.xpath("//div[@role='button'][descendant::span[text()='Товар']]")).click();
        Thread.sleep(2000);

        WebElement productName = driver.findElement(By.xpath(
                "//tr[@class='tutorial-stage-sales-fourth-step']//input[@type='text']"));
        productName.sendKeys("мандарины");

        driver.findElement(By.xpath("//div[@role='button']/descendant::span[text()='Сохранить']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@role='button']/descendant::span[text()='Закрыть']")).click();


    }

    //@Test
    public void testCustomerOrder(WebDriver driver) throws InterruptedException {
        //Проверяем, что создан заказ покупателя. Если нет, то создаем через UI
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-new']")));

        //Заходим на вкладку Продажи - Заказы покупателей
        driver.findElement(By.xpath("//span[@title='Продажи']/ancestor::table[contains(@class, 'menu-item')]")).click();

        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@title='Заказы покупателей']")));
        driver.findElement(By.xpath("//span[@title='Заказы покупателей']")).click();

        //Проверяем, есть ли хоть один заказ, если нет - создаем
        try {
            driver.findElement(By.xpath("//a[contains(@href, '#customerorder')]")).isDisplayed();
        }
        catch (NoSuchElementException e) {
            goCreateCustomerorderUI(driver);
        }

        //Обновляем заказы
        driver.findElement(By.xpath("//img[@class='b-tool-button']")).click();
        Thread.sleep(2000);

        //Проверяем снова
        try {
            driver.findElement(By.xpath("//a[contains(@href, '#customerorder')]")).isDisplayed();
        }
        catch (NoSuchElementException e) {
            assertFalse("Заказ не создался", true);
        }




    }

    public void goCreateCustomerorderUI(WebDriver driver) throws InterruptedException {
        driver.findElement(By.xpath("//div[@role='button'][descendant::span[text()='Заказ']]")).click();
        Thread.sleep(2000);

        driver.findElement(
                By.xpath(
                        "(//td[@class='widget']//div[@class='load-button tutorial-selector-image'])[4]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@class='popupContent']//div[@title='ООО \"Покупатель\"']")).click();

        WebElement productInput = driver.findElement(By.xpath("//input[contains(@placeholder, 'Добавить позицию')]"));
        productInput.sendKeys("мандарины");
        Thread.sleep(2000);

        driver.findElement(By.xpath("//div[@class='popupContent']//span[text()='мандарины']")).click();

        driver.findElement(By.xpath("//div[@role='button'][descendant::span[text()='Сохранить']]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@role='button'][descendant::span[text()='Закрыть']]")).click();
    }
}
