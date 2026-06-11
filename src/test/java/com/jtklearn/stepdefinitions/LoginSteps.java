package com.jtklearn.stepdefinitions;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import com.jtklearn.pages.LoginPage;
import com.jtklearn.pages.DashboardPage;
import com.jtklearn.utils.DriverManager;

public class LoginSteps {
    WebDriver driver = DriverManager.getDriver();
    LoginPage loginPage;
    DashboardPage dashboardPage;

    @Given("Saya membuka halaman login JTKLearn")
    public void openLoginPage() {
        driver.get("https://polban-space.cloudias79.com/jtk-learn/");
        loginPage = new LoginPage(driver);
    }

    @When("Saya memasukkan username {string} dan password {string}")
    public void enterCredentials(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
    }

    @When("Saya mengklik tombol login")
    public void clickLoginButton() {
        loginPage.clickLoginButton();
    }

    @Then("Saya akan diarahkan ke halaman dashboard")
    public void verifyDashboardPage() {
        dashboardPage = new DashboardPage(driver);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL setelah login: " + currentUrl);
        Assertions.assertTrue(currentUrl.contains("beranda") || currentUrl.contains("home") || currentUrl.contains("dashboard"),
                "URL tidak mengandung indikasi dashboard: " + currentUrl);
        driver.quit();
    }
}