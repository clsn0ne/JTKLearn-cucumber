package com.jtklearn.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class DashboardPage {
    WebDriver driver;
    WebDriverWait wait;

    private final By userDropdownTrigger = By.cssSelector("li.nav-name.dropdown > a.nav-link");

    private final By logoutButton = By.cssSelector("button.dropdown-button");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public void openUserDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(userDropdownTrigger)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
    }

    public void clickLogoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
}