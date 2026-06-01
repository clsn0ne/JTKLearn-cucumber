# JTKLearn-cucumber

Proyek pembelajaran automation testing menggunakan **Cucumber** (BDD) dan **Selenium WebDriver** dengan bahasa **Java**. Proyek ini menerapkan **Page Object Model** untuk memisahkan logika halaman, serta menghasilkan laporan pengujian otomatis.

## ✨ Fitur Utama
- Skenario pengujian ditulis dalam Gherkin (`.feature`).
- Step Definition terintegrasi dengan Page Object.
- Mendukung berbagai browser (Chrome, Brave) melalui WebDriverManager.
- Laporan HTML otomatis dari Cucumber (built-in report).
- Build dan eksekusi mudah dengan Maven.

## ⚙️ Prasyarat
- Java JDK
- Maven
- Web browser (Chrome/Brave)

## 🚀 Cara Menjalankan
1. **Clone repositori**
   ```bash
   git clone https://github.com/clsn0ne/JTKLearn-cucumber.git
   cd JTKLearn-cucumber
2. **Jalankan seluruh pengujian**
   ```bash
   mvn clean test
   ```
   Perintah ini akan:
   - Mengunduh dependencies dan WebDriver otomatis
   - Menjalankan skenario dari folder src/test/resources/features
   - Menghasilkan laporan di target/cucumber-reports.html
