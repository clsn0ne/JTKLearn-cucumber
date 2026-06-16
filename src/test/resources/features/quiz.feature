Feature: Quizzez
  Menguji fungsionalitas kuis pada course Bakery With Me! Resep Hayday🥐🍲

  @quiz @tc11
  Scenario: Verifikasi kuis dengan tipe soal pilihan ganda dapat dikerjakan
    Given Pelajar sudah login dan enroll course Bakery With Me! Resep Hayday🥐🍲
    When Pelajar membuka sesi kuis "Kuis Fish Soup"
    And Memilih satu opsi jawaban
    And Klik submit
    Then Sistem menampilkan hasil kuis

  @quiz @tc12 @essay
  Scenario: Verifikasi kuis dengan tipe soal jawaban singkat dapat dikerjakan
    Given Pelajar sudah login dan enroll course Bakery With Me! Resep Hayday🥐🍲
    When Pelajar membuka sesi kuis "Kuis Donuts Recipe"
    And Pelajar mengisi jawaban "1,5 jam" pada kolom essay
    And Klik submit
    Then Sistem menampilkan hasil kuis dengan nilai yang sesuai