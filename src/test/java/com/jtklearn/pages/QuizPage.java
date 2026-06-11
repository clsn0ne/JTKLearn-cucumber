package com.jtklearn.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class QuizPage extends BasePage {

    // Sidebar item list
    private final By learnListItems = By.cssSelector("ul.learn-list li.learn-list-item");

    // Halaman panduan kuis — tunggu div quiz-guide-box muncul
    private final By quizGuideBox = By.cssSelector("div.quiz-guide-box");

    // Tombol "Mulai Kuis" — hanya pada halaman panduan
    private final By startQuizButton = By.cssSelector("div.submit-container button.custom-btn");

    // Halaman soal kuis — tunggu quiz-container muncul
    private final By quizContainer = By.cssSelector("div.quiz-container");

    // Radio button jawaban — class spesifik dari HTML
    private final By radioOptions = By.cssSelector("input.input-quiz-radio[type='radio']");

    // Tombol submit "KIRIM" — spesifik type=submit
    private final By submitButton = By.cssSelector("div.submit-container button[type='submit'].custom-btn");

    // Area hasil kuis
    private final By resultContainer = By.xpath(
        "//*[contains(@class,'result') or contains(@class,'score') or " +
        "contains(normalize-space(.),'Hasil') or " +
        "contains(normalize-space(.),'Skor') or " +
        "contains(normalize-space(.),'Nilai')]"
    );

    public QuizPage(WebDriver driver) {
        super(driver);
    }

    // Buka kuis dari sidebar (klik item berdasarkan nama)
    public void openQuizFromSidebar(String quizName) {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        longWait.until(ExpectedConditions.presenceOfElementLocated(learnListItems));
        
        List<WebElement> items = driver.findElements(learnListItems);
        WebElement targetItem = null;
        for (WebElement item : items) {
            String text = item.getText();
            if (text != null && text.contains(quizName)) {
                targetItem = item;
                break;
            }
        }
        if (targetItem == null) {
            throw new RuntimeException("Tidak ditemukan item sidebar dengan teks: " + quizName);
        }
        scrollToElement(targetItem);
        targetItem.click();
        System.out.println("Mengklik item kuis: " + quizName);

        // Tunggu halaman panduan kuis muncul setelah klik sidebar
        longWait.until(ExpectedConditions.visibilityOfElementLocated(quizGuideBox));
        System.out.println("Halaman panduan kuis siap.");
    }

    // Klik tombol "Mulai Kuis" lalu tunggu halaman soal
    public void clickStartQuizButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Tunggu tombol Mulai Kuis bisa diklik
        WebElement startBtn = wait.until(
            ExpectedConditions.elementToBeClickable(startQuizButton)
        );
        scrollToElement(startBtn);

        // Klik via JS untuk menghindari intercept
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startBtn);
        System.out.println("Tombol 'Mulai Kuis' diklik.");

        // Tunggu quiz-container muncul (halaman soal tampil)
        wait.until(ExpectedConditions.visibilityOfElementLocated(quizContainer));
        System.out.println("Halaman soal kuis sudah tampil.");

        // Tunggu radio button benar-benar ada
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(radioOptions));
        System.out.println("Radio button soal sudah tersedia.");
    }

    // Pilih jawaban untuk SEMUA soal (satu per satu)
    public void selectAllAnswers() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Ambil semua question-box
        List<WebElement> questionBoxes = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("div.question-box")
            )
        );
        
        System.out.println("Jumlah soal ditemukan: " + questionBoxes.size());
        
        for (int i = 0; i < questionBoxes.size(); i++) {
            WebElement box = questionBoxes.get(i);
            
            // Cari radio button pertama di dalam question-box ini
            List<WebElement> radios = box.findElements(
                By.cssSelector("input.input-quiz-radio[type='radio']")
            );
            
            if (radios.isEmpty()) {
                System.out.println("Soal ke-" + (i+1) + ": tidak ada radio, skip.");
                continue;
            }
            
            WebElement firstRadio = radios.get(0);
            scrollToElement(firstRadio);
            
            // Klik via label dulu, fallback JS
            String radioId = firstRadio.getDomAttribute("id");
            if (radioId != null && !radioId.isEmpty()) {
                try {
                    WebElement label = driver.findElement(
                        By.cssSelector("label[for='" + radioId + "']")
                    );
                    scrollToElement(label);
                    label.click();
                    System.out.println("Soal ke-" + (i+1) + ": jawaban dipilih via label.");
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].click();", firstRadio
                    );
                    System.out.println("Soal ke-" + (i+1) + ": jawaban dipilih via JS.");
                }
            } else {
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", firstRadio
                );
                System.out.println("Soal ke-" + (i+1) + ": jawaban dipilih via JS (no id).");
            }
            
            // Jeda kecil antar soal
            try { Thread.sleep(200); } catch (Exception ignored) {}
        }
        
        System.out.println("Semua soal sudah dijawab.");
    }

    // Klik tombol submit (KIRIM)
    public void clickSubmit() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        scrollToElement(btn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        System.out.println("Tombol submit 'KIRIM' diklik.");
    }

    // Verifikasi apakah hasil kuis ditampilkan
    public boolean isResultDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(resultContainer));
            return result.isDisplayed();
        } catch (Exception e) {
            System.out.println("Hasil kuis tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    // Helper scroll
    private void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element
            );
            Thread.sleep(300);
        } catch (Exception ignored) {}
    }
}