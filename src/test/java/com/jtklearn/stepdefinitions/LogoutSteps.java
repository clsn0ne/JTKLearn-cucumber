package com.jtklearn.stepdefinitions;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import com.jtklearn.pages.LoginPage;
import com.jtklearn.pages.DashboardPage;
import com.jtklearn.utils.DriverManager;

public class LogoutSteps {
    WebDriver driver = DriverManager.getDriver();
    LoginPage loginPage;
    DashboardPage dashboardPage;

    @Given("Saya sudah login ke JTKLearn")
    public void userIsLoggedIn() {
        driver.get("https://polban-space.cloudias79.com/jtk-learn/");
        loginPage = new LoginPage(driver);
        loginPage.enterEmail("admin@example.com");
        loginPage.enterPassword("admin");
        loginPage.clickLoginButton();

        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL setelah login di LogoutSteps: " + currentUrl);
        Assertions.assertTrue(
            currentUrl.contains("beranda") || currentUrl.contains("home") || currentUrl.contains("dashboard"),
            "Gagal login, URL tidak sesuai: " + currentUrl
        );
        dashboardPage = new DashboardPage(driver);
    }

    @When("Saya mengklik menu profile")
    public void clickProfileMenu() {
        dashboardPage.openUserDropdown();
    }

    @When("Saya mengklik tombol logout")
    public void clickLogoutButton() {
        dashboardPage.clickLogoutButton();
    }

    @Then("Saya akan diarahkan kembali ke halaman login")
    public void verifyRedirectToLoginPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By emailField = By.cssSelector("input[placeholder='Masukkan email']");
        By passwordField = By.cssSelector("input[placeholder='Masukan kata sandi']");

        System.out.println("URL setelah logout: " + driver.getCurrentUrl());

        boolean emailVisible = false;
        boolean passwordVisible = false;
        try {
            emailVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).isDisplayed();
            passwordVisible = driver.findElement(passwordField).isDisplayed();
        } catch (Exception e) {
            System.out.println("Elemen login tidak ditemukan: " + e.getMessage());
        }

        Assertions.assertTrue(emailVisible && passwordVisible,
            "Halaman login tidak muncul setelah logout — field email/password tidak terdeteksi");
        driver.quit();
    }
}