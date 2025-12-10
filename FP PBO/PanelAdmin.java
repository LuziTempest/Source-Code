import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelAdmin extends JPanel {
    private SistemRobotikaGUI gui;
    private JTable tabel;
    private DefaultTableModel model;

    public PanelAdmin(SistemRobotikaGUI gui) {
        this.gui = gui;
        setLayout(new BorderLayout());

        JLabel lbl = new JLabel("Dashboard Admin");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        add(lbl, BorderLayout.NORTH);

        String[] col = {"Pemohon", "Jenis", "Status"};
        model = new DefaultTableModel(col, 0);
        tabel = new JTable(model);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel();
        JButton btnSetuju = new JButton("Setuju");
        JButton btnTolak = new JButton("Tolak");
        JButton btnRefresh = new JButton("Refresh Data");
        JButton btnLogout = new JButton("Logout");

        // Warna Tombol Admin (Hitam sesuai request)
        btnSetuju.setForeground(Color.BLACK);
        btnTolak.setForeground(Color.BLACK);
        
        btnRefresh.addActionListener(e -> refreshData());
        
        btnSetuju.addActionListener(e -> updateStatus("DISETUJUI"));
        btnTolak.addActionListener(e -> updateStatus("DITOLAK"));
        
        btnLogout.addActionListener(e -> gui.gantiHalaman("Login"));

        pnlBtn.add(btnRefresh);
        pnlBtn.add(btnSetuju);
        pnlBtn.add(btnTolak);
        pnlBtn.add(btnLogout);
        add(pnlBtn, BorderLayout.SOUTH);
    }

    private void refreshData() {
        model.setRowCount(0);
        for (Permintaan p : gui.getDaftarAjuan()) {
            model.addRow(new Object[]{
                p.getNama(),
                (p instanceof PeminjamanGedung) ? "Peminjaman" : "Studi Banding",
                p.getStatus()
            });
        }
    }

    private void updateStatus(String status) {
        int row = tabel.getSelectedRow();
        if (row != -1) {
            gui.getDaftarAjuan().get(row).setStatus(status);
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
        }
    }
}