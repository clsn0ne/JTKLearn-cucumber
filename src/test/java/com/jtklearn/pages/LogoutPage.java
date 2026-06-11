package com.jtklearn.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LogoutPage extends BasePage {
    private final By logoutButton = By.xpath("//button[contains(text(), 'Logout')]");

    public LogoutPage(WebDriver driver) {
        super(driver);
    }

    public void clickLogoutButton() {
        click(logoutButton);
    }
}
