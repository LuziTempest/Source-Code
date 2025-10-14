public class Produk
{
    private String nama;
    private int stock;
    private int harga;
    
    public Produk(String nama, int stock, int harga)
    {
        this.nama = nama;
        this.stock = stock;
        this.harga = harga;
    }

    public String getNama(){
        return nama;
    }
    
    public int getStock()
    {
        return stock;
    }
    
    public int getHarga()
    {
        return harga;
    }
    
    public int cekStock(){
        if (stock > 0){
            return 1;
        }
        return 0;
    }
    
    public void kurangiStock(){
        stock--;
    }
}