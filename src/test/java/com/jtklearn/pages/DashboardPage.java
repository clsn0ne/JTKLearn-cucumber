package com.jtklearn.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {
    private final By userDropdownTrigger = By.cssSelector("li.nav-name.dropdown > a.nav-link");
    private final By logoutButton = By.cssSelector("button.dropdown-button");

    public DashboardPage(WebDriver driver) {
        super(driver);
        this.wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(20));
    }

    public void openUserDropdown() {
        click(userDropdownTrigger);
        waitForVisibility(logoutButton);
    }

    public void clickLogoutButton() {
        click(logoutButton);
    }
}