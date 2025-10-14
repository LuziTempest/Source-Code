import java.util.ArrayList;

public class VendingMachine
{
    private ArrayList<Produk> listProduk;
    private ArrayList<String> log;
    String pesan;
    
    public VendingMachine()
    {
        listProduk = new ArrayList<>();
        log = new ArrayList<>();
        
        listProduk.add(new Produk("Sprite", 10, 5000));
        listProduk.add(new Produk("Fanta", 7, 5000));
        listProduk.add(new Produk("Nipis Madu", 1, 4000));
        listProduk.add(new Produk("Coca Cola", 6, 5000));
        listProduk.add(new Produk("Le Mineral", 4, 3000));
        listProduk.add(new Produk("Aqua", 0, 3500));
    }
    
    public void tampilanMachine(){
        System.out.println("======================================");
        System.out.println("    SELAMAT DATANG DI MONSTER MART    "); 
        System.out.println("======================================");
        System.out.println("--------------------------------------");
        
        for (int i = 0 ; i < listProduk.size() ; i++){
            Produk temp = listProduk.get(i);
            System.out.printf("%d | %s -> Rp %d |\n", 
            (i+1), temp.getNama(),temp.getHarga());
        }
        System.out.println("--------------------------------------");
        System.out.println("======================================");
    }
    
    public boolean cekProduk(int nomer){
        if (nomer < 1 || nomer > listProduk.size()){
            System.out.println("Nomer produk yang anda masukan tidak valid.");
            pesan = "[ALERT]: Nomer produk yang anda masukan tidak valid.";
            catatLog(pesan);
            return false;
        } else if (listProduk.get(nomer-1).getStock() <= 0 ){
            System.out.println("Mohon maaf, produk yang anda pilih kehabisan stock.");
            pesan = "[ADMIN ALERT]: Stok " + listProduk.get(nomer-1).getNama() 
            + " habis, segera isi ulang!";
            catatLog(pesan);
            return false;
        } else {
            return true;
        }
    }
    
    public int totalHarga(int nomer){
        Produk pilihan = listProduk.get(nomer-1);
        return pilihan.getHarga();
    }
    
    public void pembelianBarang(int nomer, int uang){
        Produk pilihan = listProduk.get(nomer - 1);
        if (uang < pilihan.getHarga()){
            System.out.println("Mohon maaf uang yang anda miliki kurang.");
            pesan = "[ALERT]: Mohon maaf uang yang anda miliki kurang.";
            catatLog(pesan);
            return;
        } else {
            int kembalian = uang - pilihan.getHarga();
            pilihan.kurangiStock();
            
            System.out.println("\nTRANSAKSI BERHASIL!");
            System.out.println("Anda membeli: " + pilihan.getNama());
            System.out.println("Kembalian Anda: Rp " + kembalian);
            System.out.println("Terima kasih!");
            
            pesan = "[TRANSAKSI]: " + pilihan.getNama() + " terjual 1";
            catatLog(pesan);
        }
        
        if (pilihan.getStock() == 0){
            
            System.out.println("Stok " + pilihan.getNama() 
            + " habis, segera isi ulang!");
            pesan = "[ADMIN ALERT]: Stok " + pilihan.getNama() 
            + " habis, segera isi ulang!";
            catatLog(pesan);
        }   
    }
    
    private void catatLog(String pesan){
        log.add(pesan);
    }
    
    public void tampilkanLog(){
        for (String temp : log){
            System.out.println(temp);
        }
    }
}