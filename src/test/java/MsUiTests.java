import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.Base64;

import static org.junit.Assert.assertFalse;

public class MsUiTests {

    private static WebDriver driver = new ChromeDriver();
    static {driver.get("https://online-api-4.testms.lognex.ru");}
    private static String login = "admin@v1";
    private static String password = "123123";
    private static String originalInput = login+":"+password;

    static String credentials = "Basic "+ Base64.getEncoder().encodeToString(originalInput.getBytes());


    @Test
    public void testRegistration() throws InterruptedException, IOException {
        //Проверяем вход в аккаунт. Если его нет, то создаем и меняем пароль.

        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='lable-login']")));

        WebElement loginInput = driver.findElement(By.xpath("//input[@id='lable-login']"));
        loginInput.sendKeys(login);

        WebElement passwordInput = driver.findElement(By.xpath("//input[@id='lable-password']"));
        passwordInput.sendKeys(password);

        driver.findElement(By.id("submitButton")).click();
        Thread.sleep(3000);

        try {
            if (driver.findElement(By.xpath("//div[@class='error-msg']")).isDisplayed()) {
                goRegister();
                changePassword();
            }
        }
        catch (NoSuchElementException e) {
            System.out.println("Новая регистрация не нужна");;
        }

        Thread.sleep(5000);

        try {
            driver.findElement(By.xpath("//div[@class='login-new']"));
        } catch (NoSuchElementException e) {
            assertFalse("Регистрация не проходит!", true);
        }

        testCreateProduct();
        testCustomerOrder();

    }


    public void goRegister() {
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='lable-login']")));

        driver.findElement(By.id("reglink")).click();

        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='email']")));
        WebElement emailInput = driver.findElement(By.xpath("//input[@id='email']"));
        emailInput.sendKeys("vpligin@moysklad.ru");

        WebElement companyInput = driver.findElement(By.xpath("//div[@class='wrapper']//*[@id = 'company']"));
        companyInput.sendKeys("v1");

        driver.findElement(By.xpath("//button[@id='submit']")).click();


    }

    public void changePassword() throws InterruptedException {
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-new']")));
        driver.findElement(By.xpath("//table[@title='Аккаунт']")).click();
        driver.findElement(By.xpath("//div[contains(@class, 'user-menu-popup-button')]//td[text()='Настройки']")).click();
        driver.findElement(By.xpath("//a[contains(@class,'sidebar-menu')][span[@title='Сотрудники']]")).click();

        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@title='vpligin@moysklad.ru']")));
        driver.findElement(By.xpath("//div[@title='vpligin@moysklad.ru']")).click();

        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
        WebElement passwordInput = driver.findElement(By.xpath("(//input[@type='password'])[1]"));
        passwordInput.sendKeys(password);

        WebElement passwordCheckInput = driver.findElement(By.xpath("(//input[@type='password'])[2]"));
        passwordCheckInput.sendKeys(password);

        driver.findElement(By.xpath("//span[text()='Сохранить']/ancestor::div[@role='button']")).click();
    }

    //@Test
    public void testCreateProduct() throws IOException, InterruptedException {

        //Проверяем, что есть товар "мандарины". Если нет, то создаем его через remap 1.1
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-new']")));

        driver.findElement(By.xpath("//span[@title='Товары']/ancestor::table[contains(@class, 'menu-item')]")).click();

        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@title='Товары и услуги']")));
        driver.findElement(By.xpath("//span[@title='Товары и услуги']")).click();


        try {
            driver.findElement(By.xpath("//div[@title='мандарины']")).isDisplayed();
        }
        catch (NoSuchElementException e) {
            RemapAccess.productPost(credentials);
        }

        driver.findElement(By.xpath("//img[@class='b-tool-button']")).click();
        Thread.sleep(2000);

        try {
            driver.findElement(By.xpath("//div[@title='мандарины']")).isDisplayed();
        }
        catch (NoSuchElementException e) {
            assertFalse("Товар не создался", true);
        }

    }

    //@Test
    public void testCustomerOrder() throws IOException, InterruptedException {
        //Проверяем, что создан заказ покупателя. Если нет, то создаем через remap 1.1
        WebDriverWait waitLoad = new WebDriverWait(driver, 3000);
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-new']")));

        driver.findElement(By.xpath("//span[@title='Продажи']/ancestor::table[contains(@class, 'menu-item')]")).click();

        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@title='Заказы покупателей']")));
        driver.findElement(By.xpath("//span[@title='Заказы покупателей']")).click();


        try {
            driver.findElement(By.xpath("//a[@title='00003']")).isDisplayed();
        }
        catch (NoSuchElementException e) {
            RemapAccess.productPost(credentials);
            RemapAccess.customerOrderPost(credentials);
        }

        driver.findElement(By.xpath("//img[@class='b-tool-button']")).click();
        Thread.sleep(2000);

        try {
            driver.findElement(By.xpath("//a[@title='00003']")).isDisplayed();
        }
        catch (NoSuchElementException e) {
            assertFalse("Заказ не создался", true);
        }


        //RemapAccess.customerOrderPost(credentials);
    }
}
