
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

// bisa juga diimpor sekaligus seperti ini:
// import java.sql.*;

public class BelajarJavaMysql {

    // Menyiapkan parameter JDBC untuk koneksi ke database
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/perpustakaan?useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "";

    // Menyiapkan objek yang diperlukan untuk mengelola database
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;

    public static void main(String[] args) {

        // Melakukan koneksi ke database
        try {
            // register driver yang akan dipakai
            Class.forName(JDBC_DRIVER);

            // buat koneksi ke database
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // buat objek statement
            stmt = conn.createStatement();

            // buat query ke database
            String sql = "SELECT * FROM buku";

            // eksekusi query dan simpan hasilnya di ResultSet
            rs = stmt.executeQuery(sql);

            // tampilkan hasil query
            while (rs.next()) {
                System.out.println("ID Buku   : " + rs.getInt("id_buku"));
                System.out.println("Judul     : " + rs.getString("judul"));
                System.out.println("Pengarang : " + rs.getString("pengarang"));
                System.out.println("-----------------------------------");
            }

            // tutup koneksi
            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("--- TERJADI KESALAHAN KONEKSI ---");
            // Cetak stack trace agar terlihat di terminal/console
            e.printStackTrace();
        }
        
    }
}
