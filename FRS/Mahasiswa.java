import java.util.ArrayList;

public class Mahasiswa {
    private String nama;
    private String nrp;
    private ArrayList<MataKuliah> mataKuliahDiambil;

    public Mahasiswa(String nama, String nrp) {
        this.nama = nama;
        this.nrp = nrp;
        this.mataKuliahDiambil = new ArrayList<>();
    }
    
    public String getNama() { return this.nama; }
    public String getNrp() { return this.nrp; }

    public void ambilMataKuliah(MataKuliah mk) {
        if (!mataKuliahDiambil.contains(mk)) {
            this.mataKuliahDiambil.add(mk);
            mk.tambahMahasiswa(this);
            System.out.println(">> " + this.nama + " berhasil mengambil mata kuliah " + mk.getNamaMk());
        } else {
            System.out.println(">> GAGAL! Mata kuliah " + mk.getNamaMk() + " sudah diambil.");
        }
    }

    public void lihatKrs() {
        System.out.println("\n===== KARTU RENCANA STUDI (KRS) =====");
        System.out.println("Nama      : " + this.nama);
        System.out.println("NRP       : " + this.nrp);
        System.out.println("----------------------------------------");
        System.out.println("Daftar Mata Kuliah yang Diambil:");
        if (mataKuliahDiambil.isEmpty()) {
            System.out.println("- (Belum ada mata kuliah yang diambil)");
        } else {
            int totalSks = 0;
            System.out.println("+--------+-------------------------------------+-----+");
            System.out.println("| Kode   | Nama Mata Kuliah                    | SKS |");
            System.out.println("+--------+-------------------------------------+-----+");
            for (MataKuliah mk : mataKuliahDiambil) {
                System.out.printf("| %-6s | %-35s |  %d  |\n", mk.getKodeMk(), mk.getNamaMk(), mk.getSks());
                totalSks += mk.getSks();
            }
            System.out.println("+--------+-------------------------------------+-----+");
            System.out.println("Total SKS: " + totalSks);
        }
        System.out.println("=======================================");
    }
}