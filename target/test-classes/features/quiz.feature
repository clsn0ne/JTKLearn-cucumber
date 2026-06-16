Feature: Quizzez
  Menguji fungsionalitas kuis pada course Bakery With Me! Resep Hayday

  @quiz @tc17
  Scenario: Verifikasi sistem ketika pelajar submit kuis dengan ada soal yang tidak dijawab
    Given Pelajar sudah login dan enroll course Bakery With Me! Resep Hayday dan sedang mengerjakan kuis
    When Pelajar membuka sesi kuis "Kuis Donuts"
    And Membiarkan minimal satu soal tidak diisi
    And Menunggu hingga timer kuis selama 60 detik habis
    Then Sistem menampilkan peringatan soal belum terjawab
    And Sistem tetap menampilkan hasil kuis dengan soal kosong dihitung salah