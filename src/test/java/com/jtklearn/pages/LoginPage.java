package com.jtklearn.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    
    private final By[] emailSelectors = {
        By.cssSelector("input[placeholder='Masukkan email']"),
        By.cssSelector("input[placeholder*='email' i]"),
        By.cssSelector("input[type='email']"),
        By.cssSelector("input[name='email']"),
        By.cssSelector("input[name*='email' i]"),
        By.xpath("//input[contains(@placeholder, 'email') or contains(@placeholder, 'Email')]")
    };
    
    private final By[] passwordSelectors = {
        By.cssSelector("input[placeholder='Masukan kata sandi']"),
        By.cssSelector("input[placeholder*='kata sandi' i]"),
        By.cssSelector("input[placeholder*='password' i]"),
        By.cssSelector("input[type='password']"),
        By.cssSelector("input[name='password']"),
        By.xpath("//input[@type='password']")
    };
    
    private final By[] loginButtonSelectors = {
        By.cssSelector("button.btn-danger[type='submit']"),
        By.cssSelector("button[type='submit']"),
        By.xpath("//button[@type='submit']"),
        By.xpath("//button[contains(text(), 'Login') or contains(text(), 'Masuk') or contains(@class, 'btn-danger')]")
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
        System.out.println("Tombol login ditemukan: " + loginButton.getText());
        loginButton.click();
        System.out.println("Tombol login diklik");
    }
    
    private WebElement findElementWithFallback(By[] selectors, String elementName) {
        Exception lastException = null;
        
        for (int i = 0; i < selectors.length; i++) {
            try {
                System.out.println("Mencoba selector " + (i+1) + " untuk " + elementName + ": " + selectors[i]);
                WebElement element = waitForVisibility(selectors[i]);
                if (element != null && element.isDisplayed()) {
                    System.out.println("✓ Selector berhasil!");
                    return element;
                }
            } catch (Exception e) {
                lastException = e;
                System.out.println("✗ Selector gagal: " + e.getMessage());
            }
        }
        
        System.err.println("SEMUA SELECTOR GAGAL untuk " + elementName);
        throw new RuntimeException("Tidak dapat menemukan " + elementName + " dengan semua selector yang tersedia", lastException);
    }
}
