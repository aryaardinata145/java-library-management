# Sistem Informasi Perpustakaan Sederhana (Kelompok 3)

Program aplikasi perpustakaan berbasis konsol (CLI) menggunakan bahasa pemrograman **Java** untuk mengelola daftar koleksi buku, transaksi peminjaman, transaksi pengembalian beserta perhitungan denda keterlambatan, dan laporan riwayat transaksi.

---

## Struktur File Program

- `MainApp.java` — *Entry point* program (`main` method) untuk menginisialisasi dan menjalankan aplikasi perpustakaan.
- `Buku.java` — *Class model* untuk merepresentasikan data buku (`idBuku`, `judulBuku`, `stokBuku`).
- `Peminjaman.java` — *Class model* untuk merepresentasikan data transaksi peminjaman (`idPeminjaman`, `npmPeminjam`, `namaPeminjam`, `indeksBuku`, `sudahDikembalikan`, `lamaPinjamHari`, `totalDenda`).
- `Perpustakaan.java` — *Class* utama yang memuat logika layanan perpustakaan, validasi input, serta tampilan menu dan tabel.

---

## Flow Program

### Diagram Alur (Flowchart)

```mermaid
flowchart TD
    Start([Mulai Program - MainApp.main]) --> Init[Inisialisasi Objek Perpustakaan:<br/>5 Buku Awal, Kapasitas 100 Transaksi,<br/>Batas Pinjam 7 Hari, Denda Rp1.000/hari]
    Init --> Menu[Tampilkan Menu Utama Perpustakaan<br/>Input Pilihan Menu 0-4]
    
    Menu --> Pilihan{Pilihan Menu?}
    
    Pilihan -->|1: Lihat Daftar Buku| Menu1[Tampilkan Tabel Koleksi Buku:<br/>ID, Judul Buku, dan Stok]
    Menu1 --> Menu
    
    Pilihan -->|2: Pinjam Buku| Menu2[Cek Kapasitas Riwayat & Tampilkan Buku<br/>Input & Validasi ID Buku + Cek Stok > 0<br/>Input NPM & Cek Pinjaman Ganda Aktif<br/>Input Nama Peminjam]
    Menu2 --> SimpanPinjam[Simpan Transaksi Peminjaman Baru<br/>Kurangi Stok Buku -1<br/>Cetak Bukti Peminjaman]
    SimpanPinjam --> Menu
    
    Pilihan -->|3: Kembalikan Buku| Menu3[Cek & Tampilkan Pinjaman Aktif<br/>Input & Validasi ID Peminjaman<br/>Input Lama Pinjam dalam Hari >= 1]
    Menu3 --> HitungDenda[Hitung Keterlambatan & Denda:<br/>Jika > 7 hari: terlambat = lama - 7<br/>Denda = terlambat x Rp1.000]
    HitungDenda --> SimpanKembali[Update Status Sudah Dikembalikan<br/>Tambah Stok Buku +1<br/>Cetak Bukti Pengembalian]
    SimpanKembali --> Menu
    
    Pilihan -->|4: Lihat Riwayat| Menu4[Tampilkan Tabel Riwayat Transaksi<br/>& Ringkasan Statistik + Akumulasi Denda]
    Menu4 --> Menu
    
    Pilihan -->|Selain 0-4| Invalid[Tampilkan Pesan Menu Tidak Tersedia]
    Invalid --> Menu
    
    Pilihan -->|0: Keluar| End([Tutup Scanner & Program Selesai])
```

### Penjelasan Alur Program

1. **Inisialisasi Program (`MainApp.java` & `Perpustakaan.java`)**
   - Program dimulai dari method `main()` pada `MainApp` yang membuat objek `Perpustakaan` dan memanggil method `jalankanAplikasi()`.
   - Sistem menginisialisasi 5 koleksi buku awal (`daftarBuku`), array riwayat transaksi berkapasitas 100 data (`daftarRiwayatPeminjaman`), batas waktu pinjam bebas denda selama **7 hari** (`BATAS_HARI_PINJAM`), dan tarif denda keterlambatan sebesar **Rp1.000/hari** (`DENDA_PER_HARI`).

2. **Perulangan Menu Utama (`jalankanAplikasi`)**
   - Program menampilkan menu utama secara berulang (`while (pilihanMenu != 0)`) dan meminta input angka menu (`0`–`4`) melalui fungsi validasi `bacaInputAngka()`.

3. **Menu 1: Lihat Daftar Buku (`tampilkanDaftarBuku`)**
   - Menampilkan seluruh daftar koleksi buku yang tersedia di perpustakaan dalam format tabel yang memuat **ID**, **Judul Buku**, dan **Stok** terkini.

4. **Menu 2: Pinjam Buku (`prosesPinjamBuku`)**
   - Memeriksa apakah kapasitas array riwayat peminjaman (100 transaksi) masih tersedia.
   - Menampilkan tabel koleksi buku dan meminta pengguna memasukkan **ID Buku** yang ingin dipinjam (`0` untuk membatalkan).
   - Memvalidasi apakah ID buku terdaftar dan memeriksa ketersediaan stok (`stokBuku > 0`). Jika stok habis, peminjaman ditolak.
   - Meminta input **NPM Peminjam** (divalidasi tidak boleh kosong oleh `bacaInputTeks()`).
   - Memeriksa riwayat transaksi aktif untuk memastikan NPM tersebut tidak sedang meminjam judul buku yang sama yang belum dikembalikan.
   - Meminta input **Nama Peminjam**.
   - Membuat objek `Peminjaman` baru, menyimpannya ke dalam `daftarRiwayatPeminjaman`, mengurangi stok buku sebesar `1`, lalu menampilkan **Bukti Peminjaman**.

5. **Menu 3: Kembalikan Buku (`prosesKembalikanBuku`)**
   - Memeriksa apakah ada transaksi peminjaman yang masih berstatus belum dikembalikan (`!sudahDikembalikan`). Jika tidak ada, program kembali ke menu utama.
   - Menampilkan tabel **Daftar Pinjaman yang Belum Dikembalikan** dan meminta pengguna memasukkan **ID Peminjaman** (`0` untuk membatalkan).
   - Memvalidasi keberadaan ID peminjaman dan memastikan buku pada transaksi tersebut belum dikembalikan sebelumnya.
   - Meminta input **Lama Peminjaman** dalam hari (divalidasi minimal `1` hari).
   - Menghitung keterlambatan dan total denda:
     - Jika `lamaPinjamHari > 7`, maka `jumlahHariTerlambat = lamaPinjamHari - 7` dan `totalDenda = jumlahHariTerlambat * 1000`.
     - Jika `lamaPinjamHari <= 7`, maka `jumlahHariTerlambat = 0` dan `totalDenda = 0`.
   - Memperbarui status transaksi menjadi `sudahDikembalikan = true`, menambah kembali stok buku sebesar `1` (`stokBuku++`), lalu mencetak **Bukti Pengembalian**.

6. **Menu 4: Lihat Riwayat Peminjaman (`tampilkanRiwayatPeminjaman`)**
   - Memeriksa apakah sudah ada transaksi peminjaman yang tercatat.
   - Menampilkan tabel lengkap seluruh riwayat transaksi (**ID**, **NPM**, **Nama Peminjam**, **Judul Buku**, **Status**, **Lama Pinjam**, dan **Denda**).
   - Menampilkan tabel **Ringkasan Riwayat Transaksi** yang merangkum total transaksi peminjaman, jumlah pinjaman yang masih aktif, jumlah pinjaman yang sudah dikembalikan, serta total akumulasi denda tercatat.

7. **Menu 0: Keluar**
   - Menampilkan pesan selesai, keluar dari perulangan menu utama, menutup `Scanner`, dan menghentikan eksekusi program.

---

## Cara Menjalankan Program

Pastikan **JDK (Java Development Kit)** sudah terinstal, lalu jalankan perintah berikut pada terminal di direktori proyek:

```bash
javac MainApp.java Buku.java Peminjaman.java Perpustakaan.java
java MainApp
```

---

## ✨ Contributors & Development Team

<div align="center">

<!-- Animated Typing Header -->
<a href="https://github.com/DhilHUB07">
  <img src="https://readme-typing-svg.demolab.com?font=Plus+Jakarta+Sans&weight=700&size=24&duration=3000&pause=1000&color=3B82F6&center=true&vCenter=true&width=700&lines=Kelompok+3+%E2%80%94+Pemrograman+Komputer+(Kompro);Program+Studi+Informatika+%E2%80%94+Universitas+Bengkulu;Perpustakaan+Sederhana+Berbasis+Java+OOP+%F0%9F%93%9A" alt="Kelompok 3 Typing Banner" />
</a>

<p>
  <img src="https://img.shields.io/badge/Kelompok_3-Kompro_2026-3B82F6?style=for-the-badge&logo=openjdk&logoColor=white" alt="Kelompok 3"/>
  <img src="https://img.shields.io/badge/Informatika-Universitas_Bengkulu-F59E0B?style=for-the-badge&logo=googlescholar&logoColor=white" alt="UNIB"/>
  <img src="https://img.shields.io/badge/Team_Size-4_Contributors-10B981?style=for-the-badge&logo=github&logoColor=white" alt="4 Contributors"/>
</p>

<table>
  <tr>
    <!-- 1. M. Fadhil Ramadhan -->
    <td align="center" width="220px">
      <br />
      <a href="https://github.com/DhilHUB07">
        <img src="https://github.com/DhilHUB07.png" width="120px" style="border-radius: 50%; border: 3px solid #F59E0B;" alt="M. Fadhil Ramadhan"/>
      </a>
      <br /><br />
      <a href="https://github.com/DhilHUB07">
        <b>M. Fadhil Ramadhan</b>
      </a>
      <br />
      <img src="https://img.shields.io/badge/NPM-G1A026006-F59E0B?style=flat-square&labelColor=1e293b" alt="G1A026006"/>
      <br /><br />
      <img src="https://img.shields.io/badge/%F0%9F%91%91_Ketua_Kelompok-F59E0B?style=flat-square&labelColor=78350F&color=F59E0B" alt="Ketua Kelompok"/>
      <br /><br />
      <a href="https://github.com/DhilHUB07">
        <img src="https://img.shields.io/badge/GitHub-DhilHUB07-181717?style=flat&logo=github&logoColor=white" alt="GitHub DhilHUB07"/>
      </a>
      <br />
      <a href="https://github.com/DhilHUB07">
        <img src="https://img.shields.io/github/followers/DhilHUB07?label=Follow&style=social" alt="Follow DhilHUB07"/>
      </a>
      <br /><br />
    </td>
    <!-- 2. Putri Nabila -->
    <td align="center" width="220px">
      <br />
      <a href="https://github.com/putr1nabilaayz-beep">
        <img src="https://github.com/putr1nabilaayz-beep.png" width="120px" style="border-radius: 50%; border: 3px solid #EC4899;" alt="Putri Nabila"/>
      </a>
      <br /><br />
      <a href="https://github.com/putr1nabilaayz-beep">
        <b>Putri Nabila</b>
      </a>
      <br />
      <img src="https://img.shields.io/badge/NPM-G1A026018-EC4899?style=flat-square&labelColor=1e293b" alt="G1A026018"/>
      <br /><br />
      <img src="https://img.shields.io/badge/%F0%9F%91%A9%E2%80%8D%F0%9F%92%BB_Anggota_Tim-EC4899?style=flat-square&labelColor=831843&color=EC4899" alt="Anggota"/>
      <br /><br />
      <a href="https://github.com/putr1nabilaayz-beep">
        <img src="https://img.shields.io/badge/GitHub-putr1nabilaayz--beep-181717?style=flat&logo=github&logoColor=white" alt="GitHub putr1nabilaayz-beep"/>
      </a>
      <br />
      <a href="https://github.com/putr1nabilaayz-beep">
        <img src="https://img.shields.io/github/followers/putr1nabilaayz-beep?label=Follow&style=social" alt="Follow putr1nabilaayz-beep"/>
      </a>
      <br /><br />
    </td>
    <!-- 3. Muhammad Abrar Muzakki -->
    <td align="center" width="220px">
      <br />
      <a href="https://github.com/abrarmuzakki22">
        <img src="https://github.com/abrarmuzakki22.png" width="120px" style="border-radius: 50%; border: 3px solid #06B6D4;" alt="Muhammad Abrar Muzakki"/>
      </a>
      <br /><br />
      <a href="https://github.com/abrarmuzakki22">
        <b>Muhammad Abrar Muzakki</b>
      </a>
      <br />
      <img src="https://img.shields.io/badge/NPM-G1A026034-06B6D4?style=flat-square&labelColor=1e293b" alt="G1A026034"/>
      <br /><br />
      <img src="https://img.shields.io/badge/%F0%9F%91%A8%E2%80%8D%F0%9F%92%BB_Anggota_Tim-06B6D4?style=flat-square&labelColor=164E63&color=06B6D4" alt="Anggota"/>
      <br /><br />
      <a href="https://github.com/abrarmuzakki22">
        <img src="https://img.shields.io/badge/GitHub-abrarmuzakki22-181717?style=flat&logo=github&logoColor=white" alt="GitHub abrarmuzakki22"/>
      </a>
      <br />
      <a href="https://github.com/abrarmuzakki22">
        <img src="https://img.shields.io/github/followers/abrarmuzakki22?label=Follow&style=social" alt="Follow abrarmuzakki22"/>
      </a>
      <br /><br />
    </td>
    <!-- 4. Arya Ardinata -->
    <td align="center" width="220px">
      <br />
      <a href="https://github.com/aryaardinata145">
        <img src="https://github.com/aryaardinata145.png" width="120px" style="border-radius: 50%; border: 3px solid #10B981;" alt="Arya Ardinata"/>
      </a>
      <br /><br />
      <a href="https://github.com/aryaardinata145">
        <b>Arya Ardinata</b>
      </a>
      <br />
      <img src="https://img.shields.io/badge/NPM-G1A026040-10B981?style=flat-square&labelColor=1e293b" alt="G1A026040"/>
      <br /><br />
      <img src="https://img.shields.io/badge/%F0%9F%91%A8%E2%80%8D%F0%9F%92%BB_Anggota_Tim-10B981?style=flat-square&labelColor=064E3B&color=10B981" alt="Anggota"/>
      <br /><br />
      <a href="https://github.com/aryaardinata145">
        <img src="https://img.shields.io/badge/GitHub-aryaardinata145-181717?style=flat&logo=github&logoColor=white" alt="GitHub aryaardinata145"/>
      </a>
      <br />
      <a href="https://github.com/aryaardinata145">
        <img src="https://img.shields.io/github/followers/aryaardinata145?label=Follow&style=social" alt="Follow aryaardinata145"/>
      </a>
      <br /><br />
    </td>
  </tr>
</table>

<br />

### 📋 Daftar Kontributor

| No | Nama Lengkap | NPM | GitHub Profile | Status |
| :-: | :---: | :-: | :-: | :-: |
| **1** | **M. Fadhil Ramadhan** | `G1A026006` | [![](https://img.shields.io/badge/@DhilHUB07-181717?style=flat-square&logo=github)](https://github.com/DhilHUB07) | 👑 **Ketua** |
| **2** | **Putri Nabila** | `G1A026018` | [![](https://img.shields.io/badge/@putr1nabilaayz--beep-181717?style=flat-square&logo=github)](https://github.com/putr1nabilaayz-beep) | 👩‍💻 Anggota |
| **3** | **Muhammad Abrar Muzakki** | `G1A026034` | [![](https://img.shields.io/badge/@abrarmuzakki22-181717?style=flat-square&logo=github)](https://github.com/abrarmuzakki22) | 🧑‍💻 Anggota |
| **4** | **Arya Ardinata** | `G1A026040` | [![](https://img.shields.io/badge/@aryaardinata145-181717?style=flat-square&logo=github)](https://github.com/aryaardinata145) | 🧑‍💻 Anggota |

<br />

<details>
<summary><b>🎓 Klik untuk Melihat Informasi Akademik & Dosen Pengampu</b></summary>
<br />

| Kategori | Keterangan |
| :--- | :--- |
| **Mata Kuliah** | Proyek Pengantar Pemrograman (Komputer dan Pemrograman) |
| **Dosen Pengampu** | 1. Funny Farady Coastera, S.Kom., M.T.<br/>2. Ir. Arie Vatresia, S.T., M.T.I., Ph.D., IPP. |
| **Program Studi** | Informatika — Fakultas Teknik |
| **Institusi** | Universitas Bengkulu (UNIB) |
| **Tahun Akademik** | 2026 |

</details>

</div>
