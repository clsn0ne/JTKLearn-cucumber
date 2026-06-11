Feature: Logout Functionality
  Sebagai pengguna yang sudah login
  Saya ingin bisa keluar dari sistem dengan aman
  Sehingga akun saya tidak digunakan oleh orang lain

  @logout_success
  Scenario: Logout berhasil saat pengguna menekan tombol logout
    Given Pengguna sudah login
    And Pengguna berada di halaman dashboard
    When Klik menu akun di navbar
    And Klik tombol Logout
    Then Sistem mengakhiri sesi dan menampilkan kembali halaman login