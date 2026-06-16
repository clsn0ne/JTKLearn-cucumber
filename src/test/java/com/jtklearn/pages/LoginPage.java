package com.jtklearn.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    
    private final By[] emailSelectors = {
        By.cssSelector("input[placeholder='Masukkan email']"),
        By.cssSelector("input[type='email']"),
        By.cssSelector("input[name='email']")
    };
    
    private final By[] passwordSelectors = {
        By.cssSelector("input[placeholder='Masukan kata sandi']"),
        By.cssSelector("input[type='password']"),
        By.cssSelector("input[name='password']")
    };
    
    private final By[] loginButtonSelectors = {
        By.cssSelector("button.btn-danger[type='submit']"),
        By.cssSelector("button[type='submit']"),
        By.xpath("//button[contains(text(), 'Masuk') or contains(text(), 'Login')]")
    };

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterEmail(String email) {
        WebElement emailField = findElementWithFallback(emailSelectors, "Email field");
        emailField.clear();
        emailField.sendKeys(email);
        System.out.println("Email diisi: " + email);
    }

    public void enterPassword(String password) {
        WebElement passwordField = findElementWithFallback(passwordSelectors, "Password field");
        passwordField.clear();
        passwordField.sendKeys(password);
        System.out.println("Password diisi");
    }

    public void clickLoginButton() {
        WebElement loginButton = findElementWithFallback(loginButtonSelectors, "Login button");
        loginButton.click();
        System.out.println("Tombol login diklik");
    }
    
    private WebElement findElementWithFallback(By[] selectors, String elementName) {
        for (int i = 0; i < selectors.length; i++) {
            try {
                WebElement element = waitForVisibility(selectors[i]);
                if (element != null && element.isDisplayed()) {
                    return element;
                }
            } catch (Exception ignored) {}
        }
        throw new RuntimeException("Tidak dapat menemukan elemen: " + elementName);
    }
}