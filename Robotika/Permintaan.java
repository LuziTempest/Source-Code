abstract class Permintaan {
    protected String namaPemohon;
    protected String jenisAjuan;
    protected String status = "Menunggu Konfirmasi"; 

    public Permintaan(String nama, String jenis) {
        this.namaPemohon = nama;
        this.jenisAjuan = jenis;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    
    public String getNama() { return namaPemohon; } 

    public abstract String getInfoLengkap();
}