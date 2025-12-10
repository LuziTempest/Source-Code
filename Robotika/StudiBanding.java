class StudiBanding extends Permintaan {
    private String namaTimTujuan;
    private String alamat;
    private String tujuanStudi;
    private String asalInstansi;
    private String waktuPelaksanaan;

    public StudiBanding(String nama, String tim, String alamat, String tujuan, String instansi, String waktu) {
        super(nama, "Studi Banding");
        this.namaTimTujuan = tim;
        this.alamat = alamat;
        this.tujuanStudi = tujuan;
        this.asalInstansi = instansi;
        this.waktuPelaksanaan = waktu;
    }

    @Override
    public String getInfoLengkap() {
        return "=== STUDI BANDING ===\n" +
               "Status: [" + status.toUpperCase() + "]\n" +
               "Nama: " + namaPemohon + "\n" +
               "Asal Instansi: " + asalInstansi + "\n" +
               "Tujuan Tim: " + namaTimTujuan + "\n" +
               "Waktu: " + waktuPelaksanaan + "\n" +
               "Tujuan: " + tujuanStudi + "\n" +
               "---------------------\n";
    }
}