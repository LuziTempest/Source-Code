public class Penyewa {
    private String nama;
    private Kendaraan kendaraanDisewa;
    public Penyewa(String nama) {
        this.nama = nama;
        this.kendaraanDisewa = null; 
    }

    public String getNama() {
        return nama;
    }

    public void sewaKendaraan(Kendaraan kendaraan) {
        if (kendaraan.isAvailable()) {
            this.kendaraanDisewa = kendaraan;
            kendaraan.sewa(); 
            System.out.println(nama + " berhasil menyewa " + kendaraan.getMerk() + " " + kendaraan.getModel());
        } else {
            System.out.println("Maaf, " + kendaraan.getMerk() + " " + kendaraan.getModel() + " tidak tersedia.");
        }
    }

    public String getInfoRental() {
        if (kendaraanDisewa != null) {
            return "Penyewa: " + nama + "\n  Menyewa: " + kendaraanDisewa.getInfoDetail();
        } else {
            return "Penyewa: " + nama + "\n  (Tidak sedang menyewa)";
        }
    }
}