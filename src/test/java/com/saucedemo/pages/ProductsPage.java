package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By title = By.cssSelector("[data-test='title']");
    private final By backpackAdd = By.id("add-to-cart-sauce-labs-backpack");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By cart = By.cssSelector("[data-test='shopping-cart-link']");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getTitle() { return wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText(); }

    public void addBackpackToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(backpackAdd)).click();
    }

    public String getCartCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText();
    }

    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cart)).click();
    }
}
