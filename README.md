# Lumina Commerce 🛍️

Aplikasi *e-commerce* Android *native* yang dibangun sebagai Tugas Akhir untuk Mobile Lab 2026. Lumina Commerce menawarkan pengalaman belanja yang modern dan mulus dengan integrasi data *real-time*, tema dinamis, dan antarmuka pengguna yang intuitif.

## 📱 Tentang Proyek
Aplikasi ini dikembangkan untuk memenuhi tugas akhir praktikum Mobile Development di Program Studi Sistem Informasi, Universitas Hasanuddin. Proyek ini mendemonstrasikan implementasi praktis dari konsep pengembangan Android *native*, termasuk konsumsi API, manajemen perpindahan halaman, dan penerapan standar desain Material Design.

## ✨ Fitur Utama
* **Alur Autentikasi:** Halaman Login dan Registrasi dengan validasi *input* data yang rapi.
* **Data Produk Real-Time:** Integrasi dengan Amazon API (melalui Retrofit) untuk mengambil dan menampilkan daftar kategori serta produk secara langsung.
* **Tema Dinamis:** Dukungan Mode Gelap (*Dark Mode*) dan Mode Terang (*Light Mode*) yang preferensinya disimpan secara permanen menggunakan `SharedPreferences`.
* **Navigasi Intuitif:** Transisi yang halus antar-*fragment* (Beranda, Pesanan, Kategori, Pengaturan) menggunakan `BottomNavigationView`.
* **UI/UX Modern:** Antarmuka yang bersih dan responsif menggunakan *Material Design Components* (MaterialCardView, TextInputLayout, MaterialButton).

## 🛠️ Teknologi yang Digunakan
* **Bahasa Pemrograman:** Java, XML
* **IDE:** Android Studio
* **Networking:** [Retrofit2](https://square.github.io/retrofit/) & Gson (untuk pemanggilan API dan *parsing* JSON)
* **Penyimpanan Lokal:** SharedPreferences
* **Arsitektur:** UI berbasis *Activity* dan *Fragment*

## 🚀 Panduan Memulai

### Prasyarat
* Android Studio (disarankan menggunakan versi terbaru)
* Minimum SDK: API 24 (Android 7.0)
* Koneksi Internet Aktif (wajib untuk memuat data API)

### Instalasi
1. *Clone* repositori ini:
   ```bash
   git clone [https://github.com/femi211005/datamart.git](https://github.com/femi211005/datamart.git)
