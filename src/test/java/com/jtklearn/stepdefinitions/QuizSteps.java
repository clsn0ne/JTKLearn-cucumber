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

    // ===== TC11 (PILIHAN GANDA) & TC12 (ESSAY) =====
    // PERBAIKAN: Teks disamakan persis dengan yang tertulis di file quiz.feature
    @Given("Pelajar sudah login dan enroll course Bakery With Me! Resep Hayday🥐🍲")
    public void pelajar_sudah_login_dan_enroll_course_bakery_with_me_resep_hayday() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
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

        driver.get("https://polban-space.cloudias79.com/jtk-learn/course/54");
        wait.until(ExpectedConditions.urlContains("/course/54"));
        System.out.println("Halaman course 54 terbuka.");

        coursePage = new CoursePage(driver);
        coursePage.clickContinueCourse();

        wait.until(ExpectedConditions.urlContains("/learn-course/54"));
        System.out.println("Berada di halaman belajar course: " + driver.getCurrentUrl());

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sidebarMenu")));
        System.out.println("Sidebar siap.");
    }

    @When("Pelajar membuka sesi kuis {string}")
    public void openQuizSession(String sesiName) {
        quizPage = new QuizPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        // 1. Amankan transisi URL: Pastikan browser sudah benar-benar stabil berada di halaman belajar
        if (!driver.getCurrentUrl().contains("/learn-course/54")) {
            driver.get("https://polban-space.cloudias79.com/jtk-learn/learn-course/54");
        }
        wait.until(ExpectedConditions.urlContains("/learn-course/54"));
        
        // 2. Beri jeda napas 2 detik agar state React & Sidebar selesai me-render komponen secara utuh
        try { 
            Thread.sleep(2000); 
        } catch (InterruptedException ignored) {}

        // 3. Eksekusi pembukaan kuis dari sidebar
        quizPage.openQuizFromSidebar(sesiName);
        
        // 4. Beri jeda napas 1 detik sebelum memicu paksa klik tombol mulai
        try { 
            Thread.sleep(1000); 
        } catch (InterruptedException ignored) {}
        
        quizPage.clickStartQuizButton();
        
        // 5. Tunggu lembar input soal dimuat di layar
        System.out.println("⏳ Menunggu halaman soal di-render oleh React...");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("input")));
        
        System.out.println("Kuis siap dikerjakan. Halaman soal berhasil dimuat.");
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
    }

    // ===== TC12 (ESSAY) =====
    @And("Pelajar mengisi jawaban {string} pada kolom essay")
    public void pelajar_mengisi_jawaban_pada_kolom_essay(String answer) {
        quizPage.fillAllEssayAnswers();
    }

    @Then("Sistem menampilkan hasil kuis dengan nilai yang sesuai")
    public void sistem_menampilkan_hasil_kuis_dengan_nilai_yang_sesuai() {
        Assertions.assertTrue(quizPage.isResultDisplayed(), "Hasil kuis tidak ditampilkan.");
        String score = quizPage.getQuizScore();
        System.out.println("Nilai yang didapat: " + score);
        Assertions.assertTrue(!score.isEmpty() && !score.equals("0"), 
            "Nilai tidak valid atau kosong: " + score);
    }
}