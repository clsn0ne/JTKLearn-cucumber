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

    private final By learnListItems = By.cssSelector("ul.learn-list li.learn-list-item");
    private final By quizGuideBox = By.cssSelector("div.quiz-guide-box");
    private final By startQuizButton = By.cssSelector("div.submit-container button.custom-btn");
    private final By quizContainer = By.cssSelector("div.quiz-container");
    private final By radioOptions = By.cssSelector("input.input-quiz-radio[type='radio']");
    private final By submitButton = By.cssSelector("div.submit-container button[type='submit'].custom-btn");

    private final By resultContainer = By.xpath(
        "//*[contains(@class,'result') or contains(@class,'score') or " +
        "contains(normalize-space(.),'Hasil') or " +
        "contains(normalize-space(.),'Skor') or " +
        "contains(normalize-space(.),'Nilai')]"
    );

    private final By warningPopup = By.xpath(
        "//*[contains(@class,'alert') or contains(@class,'warning') or " +
        "contains(@class,'modal') or contains(@class,'swal') or " +
        "contains(@class,'toast') or contains(@class,'notification') or " +
        "contains(normalize-space(.),'belum') or " +
        "contains(normalize-space(.),'kosong') or " +
        "contains(normalize-space(.),'wajib') or " +
        "contains(normalize-space(.),'harus') or " +
        "contains(normalize-space(.),'isi') or " +
        "contains(normalize-space(.),'jawab') or " +
        "contains(normalize-space(.),'habis')]"
    );

    public QuizPage(WebDriver driver) {
        super(driver);
    }

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

        longWait.until(ExpectedConditions.visibilityOfElementLocated(quizGuideBox));
        System.out.println("Halaman panduan kuis siap.");
    }

    public void clickStartQuizButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement startBtn = wait.until(ExpectedConditions.elementToBeClickable(startQuizButton));
        scrollToElement(startBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startBtn);
        System.out.println("Tombol 'Mulai Kuis' diklik.");

        wait.until(ExpectedConditions.visibilityOfElementLocated(quizContainer));
        System.out.println("Halaman soal kuis sudah tampil.");

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(radioOptions));
        System.out.println("Radio button soal sudah tersedia.");
    }

    public void selectAnswersExceptFirst() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<WebElement> questionBoxes = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.question-box"))
        );

        System.out.println("Jumlah soal ditemukan: " + questionBoxes.size());

        for (int i = 1; i < questionBoxes.size(); i++) {
            WebElement box = questionBoxes.get(i);
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
            try { Thread.sleep(150); } catch (Exception ignored) {}
        }
        System.out.println("Soal pertama sengaja dikosongkan (TC17). Soal lainnya diisi.");
    }

    public void waitForTimerToExpire(int seconds) {
        System.out.println("Menunggu timer kuis habis selama " + seconds + " detik...");
        try {
            Thread.sleep((seconds + 2) * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Waktu tunggu kuis selesai.");
    }

    public boolean isWarningPopupDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            try {
                org.openqa.selenium.Alert browserAlert = wait.until(ExpectedConditions.alertIsPresent());
                browserAlert.accept(); 
                return true;
            } catch (Exception alertNotPresent) {}

            WebElement warning = wait.until(ExpectedConditions.visibilityOfElementLocated(warningPopup));
            return warning.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isResultDisplayedAfterWarning() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(resultContainer));
            return result.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
            Thread.sleep(200);
        } catch (Exception ignored) {}
    }
}