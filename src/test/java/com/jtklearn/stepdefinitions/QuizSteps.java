package com.jtklearn.stepdefinitions;

import com.jtklearn.pages.CoursePage;
import com.jtklearn.pages.LoginPage;
import com.jtklearn.pages.QuizPage;
import com.jtklearn.utils.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class QuizSteps {

    WebDriver driver = DriverManager.getDriver();
    LoginPage loginPage;
    CoursePage coursePage;
    QuizPage quizPage;

    @Given("Pelajar sudah login dan enroll course Bakery With Me! Resep Hayday🥐🍲")
    public void pelajar_sudah_login_dan_enroll_course_bakery_with_me_resep_hayday() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        // Login
        driver.get("https://polban-space.cloudias79.com/jtk-learn/");
        loginPage = new LoginPage(driver);
        loginPage.enterEmail("ratna@example.com");
        loginPage.enterPassword("ratna");
        loginPage.clickLoginButton();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        wait.until(d -> d.getCurrentUrl().contains("beranda") ||
                       d.getCurrentUrl().contains("home") ||
                       d.getCurrentUrl().contains("dashboard"));
        System.out.println("Login berhasil. URL: " + driver.getCurrentUrl());

        // Buka course ID 54 (Bakery With Me!)
        driver.get("https://polban-space.cloudias79.com/jtk-learn/course/54");
        wait.until(ExpectedConditions.urlContains("/course/54"));
        System.out.println("Halaman course 54 terbuka.");

        // Klik "Lanjutkan Kursus"
        coursePage = new CoursePage(driver);
        coursePage.clickContinueCourse();

        // Tunggu hingga masuk ke halaman belajar
        wait.until(ExpectedConditions.urlContains("/learn-course/54"));
        System.out.println("Berada di halaman belajar course: " + driver.getCurrentUrl());

        // Tunggu sidebar muncul
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sidebarMenu")));
        System.out.println("Sidebar siap.");
    }

    @When("Pelajar membuka sesi kuis {string}")
    public void openQuizSession(String sesiName) {
        quizPage = new QuizPage(driver);
        // Pastikan URL benar
        if (!driver.getCurrentUrl().contains("/learn-course/54")) {
            driver.get("https://polban-space.cloudias79.com/jtk-learn/learn-course/54");
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/learn-course/54"));
        }
        // 1. Klik item sidebar sesuai nama kuis
        quizPage.openQuizFromSidebar(sesiName);
        // 2. Klik tombol "Mulai Kuis" pada halaman panduan
        quizPage.clickStartQuizButton();
        // 3. Tunggu hingga halaman soal muncul (radio button)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='radio']")));
        System.out.println("Kuis siap dikerjakan.");
    }

    @And("Memilih satu opsi jawaban")
    public void selectOneAnswer() {
        quizPage.selectAllAnswers();
    }

    @And("Klik submit")
    public void clickSubmitButton() {
        quizPage.clickSubmit();
    }

    @Then("Sistem menampilkan hasil kuis")
    public void verifyResultDisplayed() {
        Assertions.assertTrue(quizPage.isResultDisplayed(), "Hasil kuis tidak ditampilkan.");
        System.out.println("Hasil kuis tampil. Test berhasil.");
        // Jangan tutup driver di sini jika masih ada skenario lain
        // driver.quit();
    }
}