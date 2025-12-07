public abstract class Permintaan {
    protected String namaPemohon;
    protected String jenisAjuan;

    public Permintaan(String nama, String jenis) {
        this.namaPemohon = nama;
        this.jenisAjuan = jenis;
    }

    // Abstract method untuk dipaksa diimplementasikan oleh anak kelas
    public abstract String getInfoLengkap();
}