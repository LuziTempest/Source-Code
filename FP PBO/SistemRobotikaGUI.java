import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SistemRobotikaGUI extends JFrame {
    // --- DATA CENTER ---
    private ArrayList<Permintaan> daftarAjuan = new ArrayList<>();
    private HashMap<String, String> dataUser = new HashMap<>();
    private String currentUser = "";

    // --- KOMPONEN NAVIGASI ---
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public SistemRobotikaGUI() {
        super("Sistem Manajemen Robotika");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Dummy Data
        dataUser.put("user", "user123");
        dataUser.put("mahasiswa", "mhs123");

        // Setup Layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // --- INISIALISASI PANEL TERPISAH ---
        // Kita kirimkan 'this' (objek GUI ini) ke panel lain agar mereka bisa akses data
        mainPanel.add(new PanelLogin(this), "Login");
        mainPanel.add(new PanelRegister(this), "Register");
        mainPanel.add(new PanelMenu(this), "Menu");
        mainPanel.add(new PanelBooking(this), "Booking");
        mainPanel.add(new PanelVisit(this), "Visit");
        mainPanel.add(new PanelStatus(this), "Status");
        mainPanel.add(new PanelAdmin(this), "Admin");

        add(mainPanel);
        setVisible(true);
    }

    // --- METHOD UNTUK AKSES DATA DARI CLASS LAIN ---
    
    public void gantiHalaman(String namaHalaman) {
        cardLayout.show(mainPanel, namaHalaman);
    }

    public ArrayList<Permintaan> getDaftarAjuan() {
        return daftarAjuan;
    }

    public HashMap<String, String> getDataUser() {
        return dataUser;
    }

    public void setCurrentUser(String user) {
        this.currentUser = user;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(SistemRobotikaGUI::new);
    }
}