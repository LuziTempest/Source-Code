abstract class Permintaan {
    protected String namaPemohon;
    protected String jenisAjuan;
    // BARU: Variabel Status
    protected String status = "Menunggu Konfirmasi"; 

    public Permintaan(String nama, String jenis) {
        this.namaPemohon = nama;
        this.jenisAjuan = jenis;
    }

    // BARU: Setter dan Getter untuk status
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    
    public String getNama() { return namaPemohon; } // Helper untuk tabel admin

    public abstract String getInfoLengkap();
}