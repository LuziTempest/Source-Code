public abstract class Kendaraan {
    protected String merk;
    protected String model;
    protected int tahunProduksi;
    protected boolean isDisewa;

    public Kendaraan(String merk, String model, int tahunProduksi) {
        this.merk = merk;
        this.model = model;
        this.tahunProduksi = tahunProduksi;
        this.isDisewa = false; 
    }

    public String getMerk() {
        return merk;
    }

    public String getModel() {
        return model;
    }

    public int getTahunProduksi() {
        return tahunProduksi;
    }

    public boolean isAvailable() {
        return !isDisewa;
    }

    public void sewa() {
        if (isAvailable()) {
            this.isDisewa = true;
        } else {
            System.out.println("Maaf, " + merk + " " + model + " sudah disewa.");
        }
    }

    public void kembalikan() {
        this.isDisewa = false;
    }

    public abstract String getInfoDetail();
}