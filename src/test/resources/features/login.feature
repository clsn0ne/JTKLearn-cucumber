Feature: Login Functionality
  Sebagai pengguna JTKLearn
  Saya ingin bisa login ke sistem
  Sehingga saya dapat mengakses fitur-fitur pembelajaran

  @login_success
  Scenario: Login dengan kredensial yang valid
    Given Saya membuka halaman login JTKLearn
    When Saya memasukkan username "ratna@example.com" dan password "ratna"
    And Saya mengklik tombol login
    Then Saya akan diarahkan ke halaman dashboard