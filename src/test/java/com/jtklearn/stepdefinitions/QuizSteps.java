package com.jtklearn.stepdefinitions;

import com.jtklearn.pages.CoursePage;
import com.jtklearn.pages.LoginPage;
import com.jtklearn.pages.QuizPage;
import com.jtklearn.utils.DriverManager;
import io.cucumber.java.After;
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

    @Given("Pelajar sudah login dan enroll course Bakery With Me! Resep Hayday dan sedang mengerjakan kuis")
    public void pelajar_sudah_login_sedang_mengerjakan_kuis() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        driver.get("https://polban-space.cloudias79.com/jtk-learn/");
        loginPage = new LoginPage(driver);
        loginPage.enterEmail("carissa@example.com");
        loginPage.enterPassword("cariscantik");
        loginPage.clickLoginButton();

        wait.until(d -> d.getCurrentUrl().contains("dashboard") || d.getCurrentUrl().contains("beranda"));

        driver.get("https://polban-space.cloudias79.com/jtk-learn/course/54");
        wait.until(ExpectedConditions.urlContains("/course/54"));

        coursePage = new CoursePage(driver);
        coursePage.clickContinueCourse();

        wait.until(ExpectedConditions.urlContains("/learn-course/54"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sidebarMenu")));
    }

    @When("Pelajar membuka sesi kuis {string}")
    public void openQuizSession(String sesiName) {
        quizPage = new QuizPage(driver);
        if (!driver.getCurrentUrl().contains("/learn-course/54")) {
            driver.get("https://polban-space.cloudias79.com/jtk-learn/learn-course/54");
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/learn-course/54"));
        }
        quizPage.openQuizFromSidebar(sesiName);
        quizPage.clickStartQuizButton();
    }

    @And("Membiarkan minimal satu soal tidak diisi")
    public void leaveOneAnswerEmpty() {
        quizPage.selectAnswersExceptFirst();
    }

    @And("Menunggu hingga timer kuis selama {int} detik habis")
    public void waitForTimerToFinish(int seconds) {
        quizPage.waitForTimerToExpire(seconds);
    }

    @Then("Sistem menampilkan peringatan soal belum terjawab")
    public void verifyWarningPopupDisplayed() {
        boolean warningShown = quizPage.isWarningPopupDisplayed();
        Assertions.assertTrue(warningShown, "Pop-up peringatan/waktu habis tidak muncul otomatis!");
    }

    @Then("Sistem tetap menampilkan hasil kuis dengan soal kosong dihitung salah")
    public void verifyResultDisplayedWithPenalty() {
        boolean resultShown = quizPage.isResultDisplayedAfterWarning();
        Assertions.assertTrue(resultShown, "Hasil kuis tidak tampil setelah alur timeout.");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit(); 
        }
    }
}