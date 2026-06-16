Feature: Login Functionality
  Sebagai pengguna JTKLearn
  Saya ingin bisa login ke sistem
  Sehingga saya dapat mengakses fitur-fitur pembelajaran

  @login
  Scenario: Login dengan kredensial yang valid
    Given Saya membuka halaman login JTKLearn
    When Saya memasukkan username "carissa@example.com" dan password "cariscantik"
    And Saya mengklik tombol login
    Then Saya akan diarahkan ke halaman dashboard