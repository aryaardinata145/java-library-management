import java.util.Scanner;

public class Perpustakaan {
    Scanner inputKeyboard = new Scanner(System.in);

    Buku[] daftarBuku = {
        new Buku(1, "Dasar Pemrograman Java", 3),
        new Buku(2, "Algoritma dan Pemrograman", 2),
        new Buku(3, "Matematika Diskrit", 2),
        new Buku(4, "Pengantar Sistem Digital", 1),
        new Buku(5, "Pengantar Sistem Multimedia", 2)
    };

    Peminjaman[] daftarRiwayatPeminjaman = new Peminjaman[100];
    int jumlahTransaksiPeminjaman = 0;

    final int BATAS_HARI_PINJAM = 7;
    final long DENDA_PER_HARI = 1000;

    public void jalankanAplikasi() {
        int pilihanMenu = -1;
        while (pilihanMenu != 0) {
            System.out.println("\n+========================================+");
            System.out.println("|         PERPUSTAKAAN SEDERHANA         |");
            System.out.println("|               KELOMPOK 3               |");
            System.out.println("+========================================+");
            System.out.println("| No | Menu Layanan                      |");
            System.out.println("+----+-----------------------------------+");
            System.out.println("| 1  | Lihat daftar buku                 |");
            System.out.println("| 2  | Pinjam buku                       |");
            System.out.println("| 3  | Kembalikan buku                   |");
            System.out.println("| 4  | Lihat riwayat peminjaman          |");
            System.out.println("| 0  | Keluar                            |");
            System.out.println("+----+-----------------------------------+");

            pilihanMenu = bacaInputAngka("Pilih menu (0-4): ");
            switch (pilihanMenu) {
                case 1:
                    tampilkanDaftarBuku();
                    break;
                case 2:
                    prosesPinjamBuku();
                    break;
                case 3:
                    prosesKembalikanBuku();
                    break;
                case 4:
                    tampilkanRiwayatPeminjaman();
                    break;
                case 0:
                    System.out.println("Terima kasih. Program selesai.");
                    break;
                default:
                    System.out.println("Menu tidak tersedia. Pilih angka 0 sampai 4.");
            }
        }
        inputKeyboard.close();
    }

    // Proses peminjaman dan pengembalian

    void prosesPinjamBuku() {
        if (jumlahTransaksiPeminjaman >= daftarRiwayatPeminjaman.length) {
            System.out.println("Kapasitas riwayat penuh (100 transaksi).");
            return;
        }

        tampilkanDaftarBuku();
        int idBukuPilihan = bacaInputAngka("Masukkan ID buku yang ingin dipinjam (0 untuk batal): ");
        if (idBukuPilihan == 0) {
            System.out.println("Peminjaman buku dibatalkan.");
            return;
        }
        if (idBukuPilihan < 1 || idBukuPilihan > daftarBuku.length) {
            System.out.println("ID buku tidak ditemukan.");
            return;
        }

        int indeksBukuTerpilih = idBukuPilihan - 1;
        Buku bukuDipinjam = daftarBuku[indeksBukuTerpilih];
        if (bukuDipinjam.stokBuku <= 0) {
            System.out.println("Stok buku sedang habis. Peminjaman ditolak.");
            return;
        }

        String npmPeminjam = bacaInputTeks("Masukkan NPM peminjam  : ");
        for (int indeks = 0; indeks < jumlahTransaksiPeminjaman; indeks++) {
            Peminjaman dataTransaksi = daftarRiwayatPeminjaman[indeks];
            if (dataTransaksi.npmPeminjam.equalsIgnoreCase(npmPeminjam)
                    && dataTransaksi.indeksBuku == indeksBukuTerpilih
                    && !dataTransaksi.sudahDikembalikan) {
                System.out.println("NPM ini masih meminjam buku yang sama dan belum dikembalikan.");
                return;
            }
        }

        String namaPeminjam = bacaInputTeks("Masukkan Nama peminjam : ");

        int idTransaksiBaru = jumlahTransaksiPeminjaman + 1;
        Peminjaman transaksiBaru = new Peminjaman(idTransaksiBaru, npmPeminjam, namaPeminjam, indeksBukuTerpilih);
        daftarRiwayatPeminjaman[jumlahTransaksiPeminjaman++] = transaksiBaru;
        bukuDipinjam.stokBuku--;

        System.out.println("\n+================================================+");
        System.out.println("|                BUKTI PEMINJAMAN                |");
        System.out.println("+================================================+");
        System.out.println("| ID Peminjaman     : " + formatKolom("" + transaksiBaru.idPeminjaman, 26) + " |");
        System.out.println("| NPM Peminjam      : " + formatKolom(transaksiBaru.npmPeminjam, 26) + " |");
        System.out.println("| Nama Peminjam     : " + formatKolom(transaksiBaru.namaPeminjam, 26) + " |");
        System.out.println("| Judul Buku        : " + formatKolom(bukuDipinjam.judulBuku, 26) + " |");
        System.out.println("| Sisa Stok Buku    : " + formatKolom("" + bukuDipinjam.stokBuku, 26) + " |");
        System.out.println("| Batas Bebas Denda : " + formatKolom(BATAS_HARI_PINJAM + " hari", 26) + " |");
        System.out.println("| Denda Terlambat   : " + formatKolom("Rp" + DENDA_PER_HARI + "/hari", 26) + " |");
        System.out.println("+================================================+");
    }

    void prosesKembalikanBuku() {
        boolean adaPinjamanAktif = false;
        for (int indeks = 0; indeks < jumlahTransaksiPeminjaman; indeks++) {
            if (!daftarRiwayatPeminjaman[indeks].sudahDikembalikan) {
                adaPinjamanAktif = true;
                break;
            }
        }
        if (!adaPinjamanAktif) {
            System.out.println("Saat ini tidak ada pinjaman buku yang aktif.");
            return;
        }

        String garisTabel = "+----+--------------+----------------------+-------------------------------+";
        System.out.println("\n--- DAFTAR PINJAMAN YANG BELUM DIKEMBALIKAN ---");
        System.out.println(garisTabel);
        System.out.println("| " + formatKolom("ID", 2) + " | "
                + formatKolom("NPM", 12) + " | "
                + formatKolom("Nama Peminjam", 20) + " | "
                + formatKolom("Judul Buku", 29) + " |");
        System.out.println(garisTabel);

        for (int indeks = 0; indeks < jumlahTransaksiPeminjaman; indeks++) {
            Peminjaman dataTransaksi = daftarRiwayatPeminjaman[indeks];
            if (!dataTransaksi.sudahDikembalikan) {
                System.out.println("| " + formatKolom("" + dataTransaksi.idPeminjaman, 2) + " | "
                        + formatKolom(dataTransaksi.npmPeminjam, 12) + " | "
                        + formatKolom(dataTransaksi.namaPeminjam, 20) + " | "
                        + formatKolom(daftarBuku[dataTransaksi.indeksBuku].judulBuku, 29) + " |");
            }
        }
        System.out.println(garisTabel);

        int idPeminjamanPilihan = bacaInputAngka("Masukkan ID peminjaman yang dikembalikan (0 untuk batal): ");
        if (idPeminjamanPilihan == 0) {
            System.out.println("Pengembalian buku dibatalkan.");
            return;
        }
        if (idPeminjamanPilihan < 1 || idPeminjamanPilihan > jumlahTransaksiPeminjaman) {
            System.out.println("ID peminjaman tidak ditemukan.");
            return;
        }

        Peminjaman transaksiTerpilih = daftarRiwayatPeminjaman[idPeminjamanPilihan - 1];
        if (transaksiTerpilih.sudahDikembalikan) {
            System.out.println("Buku pada ID transaksi tersebut sudah pernah dikembalikan.");
            return;
        }

        int lamaPinjamHari = bacaInputAngka("Masukkan lama peminjaman dalam hari (minimal 1): ");
        while (lamaPinjamHari < 1) {
            System.out.println("Lama peminjaman minimal 1 hari!");
            lamaPinjamHari = bacaInputAngka("Masukkan lama peminjaman dalam hari (minimal 1): ");
        }

        int jumlahHariTerlambat = lamaPinjamHari > BATAS_HARI_PINJAM ? lamaPinjamHari - BATAS_HARI_PINJAM : 0;
        transaksiTerpilih.lamaPinjamHari = lamaPinjamHari;
        transaksiTerpilih.totalDenda = jumlahHariTerlambat * DENDA_PER_HARI;
        transaksiTerpilih.sudahDikembalikan = true;

        Buku bukuDikembalikan = daftarBuku[transaksiTerpilih.indeksBuku];
        bukuDikembalikan.stokBuku++;

        System.out.println("\n+================================================+");
        System.out.println("|               BUKTI PENGEMBALIAN               |");
        System.out.println("+================================================+");
        System.out.println("| ID Peminjaman     : " + formatKolom("" + transaksiTerpilih.idPeminjaman, 26) + " |");
        System.out.println("| NPM Peminjam      : " + formatKolom(transaksiTerpilih.npmPeminjam, 26) + " |");
        System.out.println("| Nama Peminjam     : " + formatKolom(transaksiTerpilih.namaPeminjam, 26) + " |");
        System.out.println("| Judul Buku        : " + formatKolom(bukuDikembalikan.judulBuku, 26) + " |");
        System.out.println("| Lama Peminjaman   : " + formatKolom(transaksiTerpilih.lamaPinjamHari + " hari", 26) + " |");
        System.out.println("| Keterlambatan     : " + formatKolom(jumlahHariTerlambat + " hari", 26) + " |");
        System.out.println("| Total Denda       : " + formatKolom("Rp" + transaksiTerpilih.totalDenda, 26) + " |");
        System.out.println("| Stok Buku Saat Ini: " + formatKolom("" + bukuDikembalikan.stokBuku, 26) + " |");
        System.out.println("+================================================+");
    }

    // Tampilan daftar buku dan riwayat

    void tampilkanDaftarBuku() {
        String garisTabel = "+----+-------------------------------+------+";
        System.out.println("\n--- DAFTAR KOLEKSI BUKU ---");
        System.out.println(garisTabel);
        System.out.println("| " + formatKolom("ID", 2) + " | "
                + formatKolom("Judul Buku", 29) + " | "
                + formatKolom("Stok", 4) + " |");
        System.out.println(garisTabel);

        for (int indeks = 0; indeks < daftarBuku.length; indeks++) {
            Buku dataBuku = daftarBuku[indeks];
            System.out.println("| " + formatKolom("" + dataBuku.idBuku, 2) + " | "
                    + formatKolom(dataBuku.judulBuku, 29) + " | "
                    + formatKolom("" + dataBuku.stokBuku, 4) + " |");
        }
        System.out.println(garisTabel);
    }

    void tampilkanRiwayatPeminjaman() {
        System.out.println("\n--- RIWAYAT TRANSAKSI PEMINJAMAN ---");
        if (jumlahTransaksiPeminjaman == 0) {
            System.out.println("Belum ada transaksi peminjaman yang tercatat.");
            return;
        }

        String garisTabel = "+----+--------------+----------------------+-------------------------------+--------------------+-------------+------------+";
        System.out.println(garisTabel);
        System.out.println("| " + formatKolom("ID", 2) + " | "
                + formatKolom("NPM", 12) + " | "
                + formatKolom("Nama Peminjam", 20) + " | "
                + formatKolom("Judul Buku", 29) + " | "
                + formatKolom("Status", 18) + " | "
                + formatKolom("Lama Pinjam", 11) + " | "
                + formatKolom("Denda", 10) + " |");
        System.out.println(garisTabel);

        int jumlahPinjamanAktif = 0;
        long akumulasiTotalDenda = 0;

        for (int indeks = 0; indeks < jumlahTransaksiPeminjaman; indeks++) {
            Peminjaman dataTransaksi = daftarRiwayatPeminjaman[indeks];
            String teksStatus = dataTransaksi.sudahDikembalikan ? "Sudah dikembalikan" : "Masih dipinjam";
            String teksLamaPinjam = dataTransaksi.sudahDikembalikan ? (dataTransaksi.lamaPinjamHari + " hari") : "-";
            String teksDenda = dataTransaksi.sudahDikembalikan ? ("Rp" + dataTransaksi.totalDenda) : "-";

            System.out.println("| " + formatKolom("" + dataTransaksi.idPeminjaman, 2) + " | "
                    + formatKolom(dataTransaksi.npmPeminjam, 12) + " | "
                    + formatKolom(dataTransaksi.namaPeminjam, 20) + " | "
                    + formatKolom(daftarBuku[dataTransaksi.indeksBuku].judulBuku, 29) + " | "
                    + formatKolom(teksStatus, 18) + " | "
                    + formatKolom(teksLamaPinjam, 11) + " | "
                    + formatKolom(teksDenda, 10) + " |");

            if (dataTransaksi.sudahDikembalikan) {
                akumulasiTotalDenda += dataTransaksi.totalDenda;
            } else {
                jumlahPinjamanAktif++;
            }
        }
        System.out.println(garisTabel);

        int jumlahSudahDikembalikan = jumlahTransaksiPeminjaman - jumlahPinjamanAktif;
        System.out.println("\n+-----------------------------------+------------+");
        System.out.println("| RINGKASAN RIWAYAT TRANSAKSI       | JUMLAH     |");
        System.out.println("+-----------------------------------+------------+");
        System.out.println("| Total Transaksi Peminjaman        | " + formatKolom("" + jumlahTransaksiPeminjaman, 10) + " |");
        System.out.println("| Pinjaman Masih Aktif              | " + formatKolom("" + jumlahPinjamanAktif, 10) + " |");
        System.out.println("| Pinjaman Sudah Dikembalikan       | " + formatKolom("" + jumlahSudahDikembalikan, 10) + " |");
        System.out.println("| Total Akumulasi Denda Tercatat    | " + formatKolom("Rp" + akumulasiTotalDenda, 10) + " |");
        System.out.println("+-----------------------------------+------------+");
    }

    String formatKolom(String teksKolom, int lebarKolom) {
        while (teksKolom.length() < lebarKolom) {
            teksKolom += " ";
        }
        return teksKolom;
    }

    // Input pengguna

    int bacaInputAngka(String pesanInput) {
        while (true) {
            System.out.print(pesanInput);
            if (inputKeyboard.hasNextInt()) {
                int angkaMasukan = inputKeyboard.nextInt();
                inputKeyboard.nextLine();
                return angkaMasukan;
            }
            System.out.println("Input tidak valid! Masukkan bilangan bulat.");
            inputKeyboard.nextLine();
        }
    }

    String bacaInputTeks(String pesanInput) {
        while (true) {
            System.out.print(pesanInput);
            String teksMasukan = inputKeyboard.nextLine().trim();
            if (!teksMasukan.isEmpty()) {
                return teksMasukan;
            }
            System.out.println("Input tidak boleh kosong!");
        }
    }
}
