public class PeminjamanGedung extends Permintaan {
    private String tanggal;
    private String kategoriUser; // "Mahasiswa" atau "Umum"
    private String detailKhusus; // NRP/Kegiatan
    private double harga;
    private String pathGambar; // Lokasi file KTM

    public PeminjamanGedung(String nama, String tanggal, String kategori, String detail, double harga, String pathGambar) {
        super(nama, "Peminjaman Gedung");
        this.tanggal = tanggal;
        this.kategoriUser = kategori;
        this.detailKhusus = detail;
        this.harga = harga;
        this.pathGambar = pathGambar;
    }

    @Override
    public String getInfoLengkap() {
        String info = "=== PEMINJAMAN GEDUNG ===\n" +
                      "Nama: " + namaPemohon + "\n" +
                      "Tanggal: " + tanggal + "\n" +
                      "Kategori: " + kategoriUser + "\n";
        
        if (kategoriUser.equals("Mahasiswa")) {
            info += "NRP: " + detailKhusus + "\n" +
                    "Bukti KTM: " + (pathGambar != null ? "Terlampir" : "Tidak ada") + "\n";
        } else {
            info += "Info: " + detailKhusus + "\n" +
                    "Total Biaya: Rp " + (int)harga + "\n";
        }
        return info + "-------------------------\n";
    }
}