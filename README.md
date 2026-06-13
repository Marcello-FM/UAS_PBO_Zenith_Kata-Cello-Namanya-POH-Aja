# UAS PBO_Zenith_Kata-Cello-Namanya-POH-Aja

## Deskripsi Aplikasi
Zenith Mental Health adalah aplikasi desktop yang dirancang sebagai wellness tool personal untuk membantu pengguna memantau tingkat stres dan memfasilitasi relaksasi mandiri. Aplikasi ini dikembangkan menggunakan antarmuka JavaFX untuk bagian frontend dan framework Spring Boot untuk backend REST API, serta menggunakan H2 Database untuk penyimpanan data yang terintegrasi melalui arsitektur MVC (Model-View-Controller). Proyek ini juga mengimplementasikan empat pilar utama Pemrograman Berorientasi Objek yaitu Encapsulation, Inheritance, Polymorphism, dan Abstraction.

## Fitur Utama
* Autentikasi Pengguna: Sistem Login dan Register yang aman.
* Kuisioner Relaksasi (Stress Check): Asesmen mandiri untuk mendeteksi tingkat stres pengguna secara real-time.
* Progress Kesehatan Mental: Dasbor statistik untuk melacak riwayat, skor rata-rata mood, dan sesi pengguna.
* Audio Relaksasi: Pemutar suara alam (hujan, ombak, hutan) untuk membantu meditasi dan menenangkan pikiran.
* Game Relaksasi: Permainan pernapasan interaktif yang mensinkronkan ritme napas pengguna dengan pergerakan visual, game pengingat dan juga game mindfull drawing

## Cara Menjalankan Aplikasi

Aplikasi ini terdiri dari dua bagian (Backend dan Frontend) yang harus dijalankan secara bersamaan. Pastikan JDK 17 (atau lebih baru) dan Maven sudah terinstal.

### Opsi 1: Menjalankan via IDE (IntelliJ IDEA / Eclipse)
1. Buka folder repositori proyek ini di IDE pilihan Anda.
2. Tunggu proses sinkronisasi dan indexing Maven selesai.
3. **Jalankan Backend:** Arahkan ke folder `backend/src/main/java/com/zenith/backend`, cari file `BackendApplication.java`, lalu klik kanan dan pilih **Run**. Tunggu hingga server Spring Boot berjalan di port 8080.
4. **Jalankan Frontend:** Arahkan ke folder frontend tempat file launcher Anda berada, cari file `Launcher.java`, lalu klik kanan dan pilih **Run**. Jendela aplikasi Zenith Mental Health akan otomatis terbuka.

### Opsi 2: Menjalankan via Terminal / Command Prompt
1. Buka Terminal dan arahkan ke root direktori proyek.
2. Buka tab Terminal pertama untuk menjalankan Backend:
   - `cd backend`
   - `mvn spring-boot:run`
3. Buka tab Terminal kedua untuk menjalankan Frontend:
   - `cd frontend`
   - `mvn javafx:run`

## Link Video Presentasi
https://youtu.be/uKTf-3m4lJA
