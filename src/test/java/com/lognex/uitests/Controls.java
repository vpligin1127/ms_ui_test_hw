package com.lognex.uitests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Controls {

    static void expectElement(WebDriverWait waitLoad, String xpath) {
        waitLoad.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    static void findElement(WebDriver driver, String xpath) {
        driver.findElement(By.xpath(xpath));
    }

    static void clickElementId(WebDriver driver, String id) {
        driver.findElement(By.id(id)).click();
    }

    static void clickElementXpath(WebDriver driver, String xpath) {
        driver.findElement(By.xpath(xpath)).click();
    }

    static void fillElement(WebDriver driver, String xpath, String input) {
        WebElement element = driver.findElement(By.xpath(xpath));
        element.sendKeys(input);
    }
}
