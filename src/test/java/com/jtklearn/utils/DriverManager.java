package com.jtklearn.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {
    public static WebDriver getDriver() {
        ChromeOptions options = new ChromeOptions();
        String chromeBinary = System.getenv("CHROME_BINARY");
        if (chromeBinary != null && !chromeBinary.isEmpty()) {
            options.setBinary(chromeBinary);
            // Use WebDriverManager online to resolve compatible driver
            WebDriverManager.chromedriver().setup();
        } else {
            String braveDefault = "C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe";
            if (new java.io.File(braveDefault).exists()) {
                options.setBinary(braveDefault);
                WebDriverManager.chromedriver().setup();
            } else {
                WebDriverManager.chromedriver().setup();
            }
        }
        options.addArguments("--start-maximized");
        return new ChromeDriver(options);
    }
}