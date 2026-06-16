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

    // ========== SELECTOR ELEMENT ==========
    // Sidebar & Navigation
    private final By learnListItems = By.cssSelector("ul.learn-list li.learn-list-item");
    private final By quizGuideBox = By.cssSelector("div.quiz-guide-box");
    private final By startQuizButton = By.cssSelector("div.submit-container button.custom-btn");

    // Soal & Form Input (Menggunakan tag HTML murni agar aman dari perubahan placeholder)
    private final By questionBox = By.cssSelector("div.question-box");
    private final By anyTextarea = By.tagName("textarea");
    private final By anyInput = By.tagName("input");

    // Tombol Submit Kuis
    private final By submitButton = By.cssSelector("div.submit-container button[type='submit'].custom-btn");

    // Hasil Kuis & Skor (Dibuat lebih global agar pasti menangkap text hasil dari web)
    private final By resultContainer = By.xpath(
        "//*[contains(@class,'result') or contains(@class,'score') or " +
        "contains(normalize-space(.),'Hasil') or contains(normalize-space(.),'Nilai') or " +
        "contains(normalize-space(.),'Skor')]"
    );
    
    private final By scoreElement = By.xpath(
        "//*[contains(text(),'Skor') or contains(text(),'Nilai') or " +
        "contains(@class,'score') or contains(@class,'nilai') or " +
        "//*[normalize-space(text())='100']]" // Fallback jika langsung keluar angka 100
    );

    // ========== CONSTRUCTOR ==========
    public QuizPage(WebDriver driver) {
        super(driver);
    }

    // ========== METHOD TINDAKAN (ACTIONS) ==========

    /**
     * Membuka item kuis berdasarkan nama dari sidebar
     */
    public void openQuizFromSidebar(String quizName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfElementLocated(learnListItems));
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
        wait.until(ExpectedConditions.visibilityOfElementLocated(quizGuideBox));
        System.out.println("Halaman panduan kuis siap.");
    }

    public void clickStartQuizButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // 1. Pastikan tombol siap berada di dalam viewport
        WebElement startBtn = wait.until(ExpectedConditions.elementToBeClickable(startQuizButton));
        scrollToElement(startBtn);

        System.out.println("🔍 [ATTACK] Memaksa klik tombol 'Mulai Kuis' via Native JavaScript Event...");
        
        // bypass total: Bersihkan fokus, scroll ke tengah, lalu dispatch click event murni
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].blur();" + 
                "arguments[0].scrollIntoView({block: 'center'});" +
                "var evObj = document.createEvent('MouseEvents');" +
                "evObj.initEvent('click', true, true);" +
                "arguments[0].dispatchEvent(evObj);", 
                startBtn
            );
            System.out.println("✅ [ATTACK] Pemaksaan click event via JS berhasil dikirim.");
        } catch (Exception e) {
            System.out.println("⚠️ [ATTACK] Gagal mengirim event klik, mencoba klik direct: " + e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startBtn);
        }

        // Jeda penstabil transisi halaman kuis
        try { 
            Thread.sleep(3000); 
        } catch (InterruptedException ignored) {}
    }

    /**
     * Mengisi semua pertanyaan bertipe Essay / Jawaban Singkat secara berurutan [cite: 4]
     */
    public void fillAllEssayAnswers() {
        // Daftar jawaban yang disesuaikan dengan kebutuhan materi kuis kue/donat Anda
        String[] answers = {
            "1,5 jam",
            "Cokelat batangan dan krim",
            "Suhu 180°C dan digoreng selama 1 menit per sisi"
        };
        
        List<WebElement> questionBoxes = driver.findElements(questionBox);
        System.out.println("Mengisi kuis essay. Kotak soal ditemukan: " + questionBoxes.size());

        for (int i = 0; i < questionBoxes.size(); i++) {
            WebElement box = questionBoxes.get(i);
            // Ambil jawaban berdasarkan urutan indeks, jika soal lebih banyak dari data, fallback ke jawaban pertama [cite: 4]
            String currentAnswer = (i < answers.length) ? answers[i] : "1,5 jam";

            // Cari elemen textarea terlebih dahulu di dalam kotak soal
            List<WebElement> textareas = box.findElements(anyTextarea);
            if (!textareas.isEmpty()) {
                WebElement target = textareas.get(0);
                scrollToElement(target);
                target.clear();
                target.sendKeys(currentAnswer);
                System.out.println("Mengisi field essay ke-" + (i+1) + " dengan: " + currentAnswer);
                continue;
            }

            // Fallback mencari elemen input biasa jika struktur form berubah
            List<WebElement> inputs = box.findElements(anyInput);
            if (!inputs.isEmpty()) {
                WebElement target = inputs.get(0);
                scrollToElement(target);
                target.clear();
                target.sendKeys(currentAnswer);
                System.out.println("Mengisi field input ke-" + (i+1) + " dengan: " + currentAnswer);
            }
        }
    }

    /**
     * Memilih opsi pertama untuk kuis bertipe Pilihan Ganda
     */
    public void selectAllAnswers() {
        List<WebElement> questionBoxes = driver.findElements(questionBox);
        System.out.println("Jumlah soal ditemukan: " + questionBoxes.size());
        
        for (WebElement box : questionBoxes) {
            List<WebElement> radios = box.findElements(By.cssSelector("input.input-quiz-radio[type='radio']"));
            if (radios.isEmpty()) continue;
            
            WebElement firstRadio = radios.get(0);
            scrollToElement(firstRadio);
            String radioId = firstRadio.getDomAttribute("id");
            
            if (radioId != null && !radioId.isEmpty()) {
                try {
                    WebElement label = driver.findElement(By.cssSelector("label[for='" + radioId + "']"));
                    scrollToElement(label);
                    label.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstRadio);
                }
            } else {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstRadio);
            }
            
            try { Thread.sleep(200); } catch (Exception ignored) {}
        }
    }

    /**
     * Mengklik tombol submit jawaban kuis
     */
    public void clickSubmit() {
        WebElement btn = waitForClickable(submitButton);
        scrollToElement(btn);
        try {
            btn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        System.out.println("Tombol submit kuis diklik.");
    }

    /**
     * Memeriksa apakah halaman hasil/skor kuis sudah tampil
     */
    public boolean isResultDisplayed() {
        try {
            return waitForVisibility(resultContainer).isDisplayed();
        } catch (Exception e) {
            System.out.println("Hasil kuis tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public String getQuizScore() {
        try {
            String scoreText = waitForVisibility(scoreElement).getText();
            System.out.println("Skor yang didapat: " + scoreText);
            return scoreText;
        } catch (Exception e) {
            System.out.println("Gagal mengambil scoreElement, mencoba membaca dari resultContainer...");
            try {
                String fallbackText = waitForVisibility(resultContainer).getText();
                System.out.println("Teks dari container hasil: " + fallbackText);
                return fallbackText;
            } catch (Exception ex) {
                System.out.println("Tidak bisa mengambil nilai sama sekali: " + ex.getMessage());
                return "0";
            }
        }
    }

    // ==================== HELPER SCROLL ====================
    private void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element
            );
            Thread.sleep(300); // Memberikan waktu jeda render animasi scroll browser
        } catch (Exception ignored) {}
    }
}