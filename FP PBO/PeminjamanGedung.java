class PeminjamanGedung extends Permintaan {
    private String tanggal;
    private String kategoriUser;
    private String detailKhusus;
    private double harga;
    private String pathGambar;

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
                      "Status: [" + status.toUpperCase() + "]\n" + 
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