package com.jtklearn.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CoursePage extends BasePage {

    private final By enrollmentCodeField = By.cssSelector("input[placeholder='Kode Pendaftaran']");
    private final By enrollButton = By.cssSelector("button.button-enroll");
    private final By closePopupButton = By.xpath("//button[contains(text(),'Tutup')]");
    private final By continueCourseButton = By.xpath("//button[contains(text(),'Lanjutkan Kursus')]");

    public CoursePage(WebDriver driver) {
        super(driver);
    }

    public void enrollWithCode(String code) {
        try {
            waitForVisibility(enrollmentCodeField).sendKeys(code);
            click(enrollButton);
            // Tunggu popup sukses dan klik tutup
            waitForVisibility(closePopupButton).click();
            System.out.println("Enroll berhasil, popup ditutup.");
        } catch (Exception e) {
            System.out.println("Enroll tidak diperlukan: " + e.getMessage());
        }
    }

    public void clickContinueCourse() {
        try {
            waitForClickable(continueCourseButton).click();
            System.out.println("Klik Lanjutkan Kursus.");
        } catch (Exception e) {
            System.out.println("Tombol Lanjutkan Kursus tidak muncul, mungkin sudah di halaman materi.");
        }
    }
}