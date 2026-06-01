Feature: Logout Functionality
  Sebagai pengguna yang sudah login
  Saya ingin bisa keluar dari sistem dengan aman
  Sehingga akun saya tidak digunakan oleh orang lain

  @logout_success
  Scenario: Logout dari sistem
    Given Saya sudah login ke JTKLearn
    When Saya mengklik menu profile
    And Saya mengklik tombol logout
    Then Saya akan diarahkan kembali ke halaman login