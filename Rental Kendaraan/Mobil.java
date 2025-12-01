public class Mobil extends Kendaraan {
    private int jumlahRoda;

    public Mobil(String merk, String model, int tahunProduksi, int jumlahRoda) {
 
        super(merk, model, tahunProduksi);
        this.jumlahRoda = jumlahRoda;
    }

    @Override
    public String getInfoDetail() {
        return "Mobil: " + merk + " " + model + " (" + tahunProduksi + ") - Roda: " + jumlahRoda;
    }
}