import java.util.ArrayList;

public class MataKuliah {
    private String kodeMk;
    private String namaMk;
    private int sks;
    private Dosen dosen;
    private ArrayList<Mahasiswa> daftarMahasiswa;

    public MataKuliah(String kodeMk, String namaMk, int sks, Dosen dosen) {
        this.kodeMk = kodeMk;
        this.namaMk = namaMk;
        this.sks = sks;
        this.dosen = dosen;
        this.daftarMahasiswa = new ArrayList<>();
    }

    public void tambahMahasiswa(Mahasiswa mahasiswa) {
        this.daftarMahasiswa.add(mahasiswa);
    }
    
    public String getKodeMk() { return kodeMk; }
    public String getNamaMk() { return namaMk; }
    public int getSks() { return sks; }
    public Dosen getDosen() { return dosen; }

    public String toString() {
        return String.format("| %-8s | %-35s | %d | %-20s |", kodeMk, namaMk, sks, dosen.getNama());
    }
}