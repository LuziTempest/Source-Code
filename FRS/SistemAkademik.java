import java.util.ArrayList;
import java.util.Scanner;

public class SistemAkademik {
    private static ArrayList<Mahasiswa> daftarMahasiswa = new ArrayList<>();
    private static ArrayList<Dosen> daftarDosen = new ArrayList<>();
    private static ArrayList<MataKuliah> daftarMataKuliah = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inisialisasiData();

        boolean berjalan = true;
        while (berjalan) {
            tampilkanMenu();
            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    menuDaftarMahasiswaBaru();
                    break;
                case 2:
                    menuLihatMataKuliah();
                    break;
                case 3:
                    menuLihatDosen();
                    break;
                case 4:
                    menuAmbilMataKuliah();
                    break;
                case 5:
                    menuLihatKrs();
                    break;
                case 6:
                    berjalan = false;
                    System.out.println("Terima kasih telah menggunakan sistem FRS ini.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
            if(berjalan) {
                 System.out.print("\nTekan Enter untuk kembali ke menu...");
                 scanner.nextLine();
            }
        }
        scanner.close();
    }

    public static void tampilkanMenu() {
        System.out.println("\n\n=== SISTEM FRS INFORMATIKA ITS ===");
        System.out.println("1. Daftar Mahasiswa Baru");
        System.out.println("2. Lihat Daftar Mata Kuliah");
        System.out.println("3. Lihat Daftar Dosen");
        System.out.println("4. Ambil Mata Kuliah (KRS)");
        System.out.println("5. Lihat KRS Mahasiswa");
        System.out.println("6. Keluar");
        System.out.print("Pilih menu (1-6): ");
    }
    
    public static void inisialisasiData() {
        Dosen d1 = new Dosen("Ir. Misbakhul Munir Irfan Subakti, S.Kom., M.Sc., M.Phil.", "198801012010121001");
        Dosen d2 = new Dosen("Fajar Baskoro, S.Kom., M.T.", "199002022012122002");
        Dosen d3 = new Dosen("Royyana Muslim Ijtihadie, S.Kom., M.Kom., Ph.D.", "198503032008101003");
        daftarDosen.add(d1);
        daftarDosen.add(d2);
        daftarDosen.add(d3);

        daftarMataKuliah.add(new MataKuliah("EF234301", "Pemrograman Web", 5, d1));
        daftarMataKuliah.add(new MataKuliah("EF234302", "Pemrograman Berorientasi Objek", 3, d1));
        daftarMataKuliah.add(new MataKuliah("EF234302", "Jaringan Komputer", 4, d2));
        daftarMataKuliah.add(new MataKuliah("EF234304", "Teori Graf", 3, d3));
        
        daftarMahasiswa.add(new Mahasiswa("Leonardoro", "5025241043"));
        daftarMahasiswa.add(new Mahasiswa("Xepher", "5025241034"));
    }

    public static void menuDaftarMahasiswaBaru() {
        System.out.println("\n--- Pendaftaran Mahasiswa Baru ---");
        System.out.print("Masukkan Nama: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan NRP: ");
        String nrp = scanner.nextLine();

        daftarMahasiswa.add(new Mahasiswa(nama, nrp));
        System.out.println(">> Mahasiswa baru '" + nama + "' berhasil didaftarkan!");
    }

    public static void menuLihatMataKuliah() {
        System.out.println("\n--- Daftar Mata Kuliah yang Tersedia ---");
        System.out.println("+----------+-------------------------------------+---+------------------------+");
        System.out.println("| Kode MK  | Nama Mata Kuliah                    |SKS| Dosen Pengampu         |");
        System.out.println("+----------+-------------------------------------+---+------------------------+");
        for (MataKuliah mk : daftarMataKuliah) {
            System.out.printf("| %-8s | %-35s | %d | %-22s |\n", mk.getKodeMk(), mk.getNamaMk(), mk.getSks(), mk.getDosen().getNama());
        }
        System.out.println("+----------+-------------------------------------+---+------------------------+");
    }

    public static void menuLihatDosen() {
        System.out.println("\n--- Daftar Dosen Informatika ---");
        int no = 1;
        for (Dosen d : daftarDosen) {
            System.out.println(no + ". " + d.toString());
            no++;
        }
    }
    
    public static void menuAmbilMataKuliah() {
        System.out.println("\n--- Pengambilan Mata Kuliah (KRS) ---");
        System.out.print("Masukkan NRP Anda: ");
        String nrp = scanner.nextLine();

        
        Mahasiswa mhs = null;
        for (Mahasiswa m : daftarMahasiswa) {
            if (m.getNrp().equals(nrp)) {
                mhs = m;
                break;
            }
        }
        
        if (mhs == null) {
            System.out.println(">> GAGAL! Mahasiswa dengan NRP " + nrp + " tidak ditemukan.");
            return;
        }

        System.out.println("Selamat datang, " + mhs.getNama());
        menuLihatMataKuliah(); 
        
        System.out.print("Masukkan Kode MK yang ingin diambil: ");
        String kodeMk = scanner.nextLine();
        
        MataKuliah mk = null;
        for (MataKuliah matkul : daftarMataKuliah) {
            if (matkul.getKodeMk().equalsIgnoreCase(kodeMk)) {
                mk = matkul;
                break;
            }
        }

        if (mk == null) {
            System.out.println(">> GAGAL! Mata kuliah dengan kode " + kodeMk + " tidak ditemukan.");
            return;
        }
        
        mhs.ambilMataKuliah(mk);
    }

    public static void menuLihatKrs() {
        System.out.println("\n--- Lihat KRS Mahasiswa ---");
        System.out.print("Masukkan NRP Mahasiswa: ");
        String nrp = scanner.nextLine();
        
        Mahasiswa mhs = null;
        for (Mahasiswa m : daftarMahasiswa) {
            if (m.getNrp().equals(nrp)) {
                mhs = m;
                break;
            }
        }
        
        if (mhs != null) {
            mhs.lihatKrs();
        } else {
            System.out.println(">> GAGAL! Mahasiswa dengan NRP " + nrp + " tidak ditemukan.");
        }
    }
}