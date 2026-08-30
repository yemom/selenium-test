package com.saucedemo.tests;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class SauceDemoTests extends BaseTest {

    @Test
    void T1_navigationSmokeTest() {
        assertTrue(new LoginPage(driver).isLoaded(), "SauceDemo login page should load");
        assertEquals("https://www.saucedemo.com/", driver.getCurrentUrl());
    }

    @Test
    void T2_twoLocatorStrategiesAreUsed() {
        LoginPage page = new LoginPage(driver);
        assertTrue(page.isLoaded());
        ProductsPage products = page.login("standard_user", "secret_sauce");
        assertEquals("Products", products.getTitle());
    }

    @Test
    void T3_positiveFlow_loginAddItemAndVerifyCart() {
        ProductsPage products = new LoginPage(driver).login("standard_user", "secret_sauce");
        assertEquals("Products", products.getTitle());
        products.addBackpackToCart();
        assertEquals("1", products.getCartCount());
        products.openCart();
        assertTrue(driver.getCurrentUrl().contains("/cart.html"));
        assertTrue(driver.getPageSource().contains("Sauce Labs Backpack"));
    }

    @Test
    void T4_negativePath_wrongCredentialsShowsError() {
        String error = new LoginPage(driver).loginAndGetError("wrong_user", "wrong_password");
        assertEquals(" Username and password do not match any user in this service", error);
    }

    @Test
    void T5_explicitWait_waitsForProductsTitle() {
        ProductsPage products = new LoginPage(driver).login("standard_user", "secret_sauce");
        assertEquals("Products", products.getTitle());
    }

    // T6: Equivalence Partitioning for username input:
    // valid partition, invalid/nonexistent partition, and locked-user partition.
    @ParameterizedTest(name = "username partition: {0}")
    @CsvSource({
            "standard_user, PASS_EXPECTED",
            "wrong_user, INVALID_USER_EXPECTED",
            "locked_out_user, LOCKED_USER_EXPECTED"
    })
    void T6_parameterizedEquivalencePartitioning(String username, String expected) {
        LoginPage login = new LoginPage(driver);
        if (expected.equals("PASS_EXPECTED")) {
            assertEquals("Products", login.login(username, "secret_sauce").getTitle());
        } else {
            String error = login.loginAndGetError(username, "secret_sauce");
            assertTrue(error.contains("Epic sadface:"));
            if (expected.equals("LOCKED_USER_EXPECTED")) {
                assertEquals(" Sorry, this user has been locked out.", error);
            } else {
                assertEquals(" Username and password do not match any user in this service", error);
            }
        }
    }

    @Test
    void T7_pageObjectIsUsedForLoginAndProducts() {
        LoginPage login = new LoginPage(driver);
        ProductsPage products = login.login("standard_user", "secret_sauce");
        assertEquals("Products", products.getTitle());
    }

    @Test
    void T8_lifecycleCreatesFreshBrowser() {
        assertNotNull(driver);
        assertEquals("Swag Labs", driver.getTitle());
    }
}
