import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

// === PANEL MENU UTAMA ===
class PanelMenu extends JPanel {
    public PanelMenu(SistemRobotikaGUI gui) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        JPanel centerStack = new JPanel(new GridBagLayout());
        centerStack.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("Menu Utama User");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbc.gridx = 0; gbc.gridy = 0;
        centerStack.add(title, gbc);

        Dimension btnSize = new Dimension(280, 55);
        JButton btnBooking = createBtn("Peminjaman Gedung", new Color(100, 149, 237), btnSize);
        JButton btnStatus = createBtn("Status Ajuan Saya", new Color(255, 215, 0), btnSize);
        JButton btnVisit = createBtn("Studi Banding", new Color(60, 179, 113), btnSize);

        btnBooking.addActionListener(e -> gui.gantiHalaman("Booking"));
        btnStatus.addActionListener(e -> {
            // Refresh status logic could be triggered here via an interface if needed
            // But PanelStatus handles it on show usually.
             gui.gantiHalaman("Status");
        });
        btnVisit.addActionListener(e -> gui.gantiHalaman("Visit"));

        gbc.gridy = 1; centerStack.add(btnBooking, gbc);
        gbc.gridy = 2; centerStack.add(btnStatus, gbc);
        gbc.gridy = 3; centerStack.add(btnVisit, gbc);

        add(centerStack, BorderLayout.CENTER);

        JButton btnLogout = new JButton("Logout / Keluar");
        btnLogout.setBackground(Color.RED); 
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                gui.setCurrentUser("");
                gui.gantiHalaman("Login");
            }
        });
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);
        bottom.add(btnLogout);
        add(bottom, BorderLayout.SOUTH);
    }

    private JButton createBtn(String txt, Color bg, Dimension dim) {
        JButton btn = new JButton(txt);
        btn.setPreferredSize(dim);
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }
}

// === PANEL BOOKING / PEMINJAMAN ===
class PanelBooking extends JPanel {
    private SistemRobotikaGUI gui;
    private JComboBox<String> cmbHari, cmbMetode;
    private JRadioButton rbMhs, rbUmum;
    private JTextField txtNama, txtId;
    private JTextArea txtKegiatan;
    private JLabel lblHarga, lblFoto;
    private JPanel formDinamis;
    private String pathFoto = "-";

    public PanelBooking(SistemRobotikaGUI gui) {
        this.gui = gui;
        setLayout(new BorderLayout());
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10,10,10,10));

        String[] hari = {"Senin (Tersedia)", "Selasa (Penuh)", "Rabu (Tersedia)"};
        cmbHari = new JComboBox<>(hari);
        rbMhs = new JRadioButton("Mahasiswa", true);
        rbUmum = new JRadioButton("Umum");
        ButtonGroup bg = new ButtonGroup(); bg.add(rbMhs); bg.add(rbUmum);

        content.add(new JLabel("Pilih Hari:")); content.add(cmbHari);
        JPanel pnlRadio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlRadio.add(rbMhs); pnlRadio.add(rbUmum);
        content.add(pnlRadio);
        content.add(new JSeparator());

        formDinamis = new JPanel(new GridBagLayout());
        content.add(formDinamis);
        
        // Init Component
        txtNama = new JTextField(20);
        txtId = new JTextField(20);
        txtKegiatan = new JTextArea(3, 20);
        cmbMetode = new JComboBox<>(new String[]{"Tunai", "QRIS"});
        lblHarga = new JLabel("Rp 0");
        lblFoto = new JLabel("No Image");
        lblFoto.setPreferredSize(new Dimension(50,50)); lblFoto.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        updateForm(); // Setup awal

        ActionListener updateListener = e -> updateForm();
        rbMhs.addActionListener(updateListener);
        rbUmum.addActionListener(updateListener);
        cmbHari.addActionListener(e -> {
             if (rbUmum.isSelected()) lblHarga.setText("Rp 500000"); // Simplifikasi logic
        });

        JButton btnSubmit = new JButton("Ajukan");
        JButton btnBack = new JButton("Kembali");
        
        btnSubmit.addActionListener(e -> simpanData());
        btnBack.addActionListener(e -> gui.gantiHalaman("Menu"));

        JPanel btnPanel = new JPanel(); 
        btnPanel.add(btnBack); btnPanel.add(btnSubmit);

        add(new JScrollPane(content), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void updateForm() {
        formDinamis.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0; gbc.gridy=0; gbc.anchor=GridBagConstraints.WEST;
        
        formDinamis.add(new JLabel("Nama:"), gbc); gbc.gridx=1; formDinamis.add(txtNama, gbc);
        gbc.gridx=0; gbc.gridy=1; formDinamis.add(new JLabel("Kegiatan:"), gbc); gbc.gridx=1; formDinamis.add(new JScrollPane(txtKegiatan), gbc);
        
        gbc.gridx=0; gbc.gridy=2;
        if(rbMhs.isSelected()){
            formDinamis.add(new JLabel("NRP:"), gbc); gbc.gridx=1; formDinamis.add(txtId, gbc);
            gbc.gridx=0; gbc.gridy=3; formDinamis.add(new JLabel("KTM:"), gbc);
            JButton btnUp = new JButton("Upload");
            btnUp.addActionListener(e -> {
                 JFileChooser fc = new JFileChooser();
                 if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) pathFoto = fc.getSelectedFile().getAbsolutePath();
            });
            gbc.gridx=1; formDinamis.add(btnUp, gbc);
        } else {
            formDinamis.add(new JLabel("Alamat:"), gbc); gbc.gridx=1; formDinamis.add(txtId, gbc);
            gbc.gridx=0; gbc.gridy=3; formDinamis.add(new JLabel("Biaya:"), gbc); gbc.gridx=1; formDinamis.add(lblHarga, gbc);
        }
        formDinamis.revalidate(); formDinamis.repaint();
    }

    private void simpanData() {
        gui.getDaftarAjuan().add(new PeminjamanGedung(txtNama.getText(), (String)cmbHari.getSelectedItem(), 
             rbMhs.isSelected()?"Mahasiswa":"Umum", txtId.getText(), 0, pathFoto));
        JOptionPane.showMessageDialog(this, "Berhasil!");
        gui.gantiHalaman("Menu");
    }
}

// === PANEL VISIT / STUDI BANDING ===
class PanelVisit extends JPanel {
    private JTextField txtNama, txtInstansi, txtTujuan;
    
    public PanelVisit(SistemRobotikaGUI gui) {
        setLayout(new GridLayout(6, 2, 10, 10));
        setBorder(new EmptyBorder(20,20,20,20));

        add(new JLabel("Nama Perwakilan:")); txtNama = new JTextField(); add(txtNama);
        add(new JLabel("Instansi:")); txtInstansi = new JTextField(); add(txtInstansi);
        add(new JLabel("Tujuan:")); txtTujuan = new JTextField(); add(txtTujuan);
        add(new JLabel("Tim Tujuan:")); 
        JComboBox<String> cmbTim = new JComboBox<>(new String[]{"ICHIRO", "BAYUCARAKA"});
        add(cmbTim);

        JButton btnSubmit = new JButton("Kirim");
        JButton btnBack = new JButton("Kembali");

        btnSubmit.addActionListener(e -> {
            gui.getDaftarAjuan().add(new StudiBanding(txtNama.getText(), (String)cmbTim.getSelectedItem(), 
                "-", txtTujuan.getText(), txtInstansi.getText(), "Besok"));
            JOptionPane.showMessageDialog(this, "Terkirim!");
            gui.gantiHalaman("Menu");
        });
        btnBack.addActionListener(e -> gui.gantiHalaman("Menu"));

        add(btnBack); add(btnSubmit);
    }
}

// === PANEL STATUS ===
class PanelStatus extends JPanel {
    private JTextArea txtArea;
    private SistemRobotikaGUI gui;

    public PanelStatus(SistemRobotikaGUI gui) {
        this.gui = gui;
        setLayout(new BorderLayout());
        
        txtArea = new JTextArea();
        txtArea.setEditable(false);
        
        JButton btnRefresh = new JButton("Refresh / Lihat Data Terbaru");
        JButton btnBack = new JButton("Kembali");

        btnRefresh.addActionListener(e -> loadData());
        btnBack.addActionListener(e -> gui.gantiHalaman("Menu"));
        
        JPanel top = new JPanel(); top.add(btnRefresh);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(txtArea), BorderLayout.CENTER);
        add(btnBack, BorderLayout.SOUTH);
        
        // Auto load saat panel dibuat (opsional, better di event listener)
        loadData(); 
    }

    private void loadData() {
        if(gui.getDaftarAjuan().isEmpty()) txtArea.setText("Belum ada data.");
        else {
            StringBuilder sb = new StringBuilder();
            for(Permintaan p : gui.getDaftarAjuan()) sb.append(p.getInfoLengkap()).append("\n");
            txtArea.setText(sb.toString());
        }
    }
}