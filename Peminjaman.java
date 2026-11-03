public class Peminjaman {
    int idPeminjaman;
    String npmPeminjam;
    String namaPeminjam;
    int indeksBuku;
    boolean sudahDikembalikan;
    int lamaPinjamHari;
    long totalDenda;

    public Peminjaman(int inputIdPeminjaman, String inputNpmPeminjam, String inputNamaPeminjam, int inputIndeksBuku) {
        idPeminjaman = inputIdPeminjaman;
        npmPeminjam = inputNpmPeminjam;
        namaPeminjam = inputNamaPeminjam;
        indeksBuku = inputIndeksBuku;
    }
}
