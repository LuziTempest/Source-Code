import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();
        Scanner scanner = new Scanner(System.in);
        
        vendingMachine.tampilanMachine();

        boolean lanjutMembeli = true;

        while (lanjutMembeli) {
            System.out.print("\nMasukkan nomor produk yang diinginkan: ");
            int pilihan = scanner.nextInt();
            
            if (vendingMachine.cekProduk(pilihan)){
                System.out.println("Total harga: Rp " + vendingMachine.totalHarga(pilihan));
                System.out.print("Masukkan jumlah uang: Rp ");
                int uang = scanner.nextInt();
                
                vendingMachine.pembelianBarang(pilihan, uang);
            }
            
            System.out.print("\nLanjut membeli? (y/n): ");
            String temp = scanner.next();
            if (temp.equalsIgnoreCase("n")) {
                System.out.print("\nTampilkan log transaksi? (y/n): ");
                temp = scanner.next();
                if (temp.equalsIgnoreCase("y")){
                    vendingMachine.tampilkanLog();
                }
                break;
                
            }
            vendingMachine.tampilanMachine();
        }

        System.out.println("\nTerima kasih sudah berbelanja!");
        scanner.close();
    }
}