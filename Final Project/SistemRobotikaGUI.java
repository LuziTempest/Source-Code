import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.File;
import java.util.Vector;

public class SistemRobotikaGUI extends JFrame {
    // Penyimpanan data sementara
    private ArrayList<Permintaan> daftarAjuan = new ArrayList<>();

    // Komponen Utama
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Panel Halaman
    private JPanel menuPanel;
    private JPanel bookingPanel;
    private JPanel statusPanel;
    private JPanel visitPanel;

    // Komponen Booking
    private JComboBox<String> cmbHari;
    private JRadioButton rbMahasiswa, rbUmum;
    private JTextField txtNamaBooking, txtIdentitasBooking;
    private JTextArea txtKegiatanBooking;
    private JComboBox<String> cmbMetodeBayar;
    private JLabel lblHarga, lblPreviewKTM;
    private String pathFotoKTM = "-";
    private JPanel panelFormDinamis;

    // Komponen Studi Banding (Baru)
    private JComboBox<String> cmbTimRiset;
    private JComboBox<String> cmbTanggal, cmbBulan, cmbTahun, cmbJam; // Komponen Waktu
    private JTextField txtNamaVisit, txtAlamatVisit, txtInstansiVisit;
    private JTextArea txtTujuanVisit;

    // Komponen Status
    private JTextArea txtAreaStatus;

    public SistemRobotikaGUI() {
        super("Sistem Manajemen Robotika");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inisialisasi Halaman
        initMenuPanel();
        initBookingPanel();
        initStatusPanel();
        initVisitPanel();

        // Tambah ke Main Panel
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(bookingPanel, "Booking");
        mainPanel.add(statusPanel, "Status");
        mainPanel.add(visitPanel, "Visit");

        add(mainPanel);
        setVisible(true);
    }

    // --- 1. MENU UTAMA (Updated: Posisi Tengah) ---
    private void initMenuPanel() {
        // Menggunakan GridBagLayout untuk menengahkan komponen secara presisi
        menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(new Color(240, 248, 255)); // Warna background agak cerah

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0); // Jarak vertikal antar tombol
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Tombol akan melebar sesuai isi

        // Judul Menu
        JLabel title = new JLabel("Sistem Manajemen Robotika");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Buat constraint khusus judul
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0; titleGbc.gridy = 0;
        titleGbc.insets = new Insets(0, 0, 40, 0); // Jarak bawah judul
        menuPanel.add(title, titleGbc);

        // Membuat Tombol dengan Ukuran Seragam
        Dimension btnSize = new Dimension(250, 50);

        JButton btnBooking = createStyledButton("Peminjaman Gedung", new Color(100, 149, 237), btnSize);
        JButton btnStatus = createStyledButton("Status Ajuan", new Color(255, 215, 0), btnSize);
        JButton btnVisit = createStyledButton("Studi Banding", new Color(60, 179, 113), btnSize);

        // Aksi Tombol
        btnBooking.addActionListener(e -> cardLayout.show(mainPanel, "Booking"));
        btnStatus.addActionListener(e -> {
            updateStatusDisplay();
            cardLayout.show(mainPanel, "Status");
        });
        btnVisit.addActionListener(e -> cardLayout.show(mainPanel, "Visit"));

        // Menambahkan tombol ke layout (tumpuk vertikal)
        gbc.gridy = 1; menuPanel.add(btnBooking, gbc);
        gbc.gridy = 2; menuPanel.add(btnStatus, gbc);
        gbc.gridy = 3; menuPanel.add(btnVisit, gbc);
    }

    // Helper untuk membuat tombol seragam
    private JButton createStyledButton(String text, Color bg, Dimension dim) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(dim);
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK); // Warna teks hitam
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }

    // --- 2. PEMINJAMAN GEDUNG (Sama seperti sebelumnya) ---
    private void initBookingPanel() {
        bookingPanel = new JPanel(new BorderLayout());
        JPanel formContentPanel = new JPanel();
        formContentPanel.setLayout(new BoxLayout(formContentPanel, BoxLayout.Y_AXIS));
        formContentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Form Peminjaman Gedung"),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Header
        JPanel pnlHari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] hari = {"Senin (Tersedia)", "Selasa (Penuh)", "Rabu (Tersedia)", "Kamis (Tersedia)", "Jumat (Penuh)", "Sabtu (Weekend)", "Minggu (Weekend)"};
        cmbHari = new JComboBox<>(hari);
        pnlHari.add(new JLabel("Pilih Hari: ")); pnlHari.add(cmbHari);
        formContentPanel.add(pnlHari);

        JPanel pnlTipe = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbMahasiswa = new JRadioButton("Mahasiswa"); rbUmum = new JRadioButton("Umum");
        ButtonGroup bg = new ButtonGroup(); bg.add(rbMahasiswa); bg.add(rbUmum);
        pnlTipe.add(new JLabel("Tipe Pemohon: ")); pnlTipe.add(rbMahasiswa); pnlTipe.add(rbUmum);
        formContentPanel.add(pnlTipe);

        formContentPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        formContentPanel.add(Box.createVerticalStrut(10));

        panelFormDinamis = new JPanel(new GridBagLayout());
        formContentPanel.add(panelFormDinamis);

        // Komponen Input
        txtNamaBooking = new JTextField(20);
        txtIdentitasBooking = new JTextField(20);
        txtKegiatanBooking = new JTextArea(4, 20); txtKegiatanBooking.setLineWrap(true);
        cmbMetodeBayar = new JComboBox<>(new String[]{"Tunai", "Transfer Bank", "QRIS"});
        lblHarga = new JLabel("Harga: Rp 0");
        lblPreviewKTM = new JLabel("Belum ada foto", SwingConstants.CENTER);
        lblPreviewKTM.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblPreviewKTM.setPreferredSize(new Dimension(120, 120));

        // Listeners
        ActionListener typeListener = e -> updateFormBooking();
        rbMahasiswa.addActionListener(typeListener); rbUmum.addActionListener(typeListener);
        cmbHari.addActionListener(e -> hitungHarga());
        rbMahasiswa.setSelected(true); updateFormBooking();

        // Tombol
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSubmit = new JButton("Ajukan Peminjaman");
        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> { resetFormBooking(); cardLayout.show(mainPanel, "Menu"); });
        btnSubmit.addActionListener(e -> prosesSimpanBooking());
        btnPanel.add(btnBack); btnPanel.add(btnSubmit);

        bookingPanel.add(new JScrollPane(formContentPanel), BorderLayout.CENTER);
        bookingPanel.add(btnPanel, BorderLayout.SOUTH);
    }
    
    // --- Helper Layout Booking ---
    private void addComponent(JPanel panel, Component comp, int x, int y, int width, int height, double weightx, double weighty, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x; gbc.gridy = y; gbc.gridwidth = width; gbc.gridheight = height;
        gbc.weightx = weightx; gbc.weighty = weighty; gbc.fill = fill;
        gbc.insets = new Insets(5, 5, 5, 5); gbc.anchor = GridBagConstraints.WEST;
        panel.add(comp, gbc);
    }

    private void updateFormBooking() {
        panelFormDinamis.removeAll();
        addComponent(panelFormDinamis, new JLabel("Nama Lengkap:"), 0, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
        addComponent(panelFormDinamis, txtNamaBooking, 1, 0, 2, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);
        addComponent(panelFormDinamis, new JLabel("Kegiatan:"), 0, 1, 1, 1, 0, 0, GridBagConstraints.NONE);
        addComponent(panelFormDinamis, new JScrollPane(txtKegiatanBooking), 1, 1, 2, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);

        if (rbMahasiswa.isSelected()) {
            addComponent(panelFormDinamis, new JLabel("NRP / NIM:"), 0, 2, 1, 1, 0, 0, GridBagConstraints.NONE);
            addComponent(panelFormDinamis, txtIdentitasBooking, 1, 2, 2, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);
            addComponent(panelFormDinamis, new JLabel("Bukti KTM:"), 0, 3, 1, 1, 0, 0, GridBagConstraints.NONE);
            JPanel pnlUploadBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            JButton btnUp = new JButton("Pilih Foto...");
            btnUp.addActionListener(e -> pilihGambar());
            pnlUploadBtn.add(btnUp);
            addComponent(panelFormDinamis, pnlUploadBtn, 1, 3, 1, 1, 0, 0, GridBagConstraints.NONE);
            addComponent(panelFormDinamis, lblPreviewKTM, 2, 3, 1, 2, 0, 0, GridBagConstraints.NONE);
        } else {
            addComponent(panelFormDinamis, new JLabel("Alamat:"), 0, 2, 1, 1, 0, 0, GridBagConstraints.NONE);
            addComponent(panelFormDinamis, txtIdentitasBooking, 1, 2, 2, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);
            addComponent(panelFormDinamis, new JLabel("Metode Bayar:"), 0, 3, 1, 1, 0, 0, GridBagConstraints.NONE);
            addComponent(panelFormDinamis, cmbMetodeBayar, 1, 3, 2, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);
            addComponent(panelFormDinamis, new JLabel("Estimasi Biaya:"), 0, 4, 1, 1, 0, 0, GridBagConstraints.NONE);
            lblHarga.setFont(new Font("SansSerif", Font.BOLD, 14));
            addComponent(panelFormDinamis, lblHarga, 1, 4, 2, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);
            hitungHarga();
        }
        panelFormDinamis.revalidate(); panelFormDinamis.repaint();
    }

    private void hitungHarga() {
        String hari = (String) cmbHari.getSelectedItem();
        double basePrice = 500000;
        if (hari != null && hari.contains("Weekend")) basePrice *= 2;
        lblHarga.setText("Rp " + (int)basePrice);
    }

    private void pilihGambar() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            pathFotoKTM = file.getAbsolutePath();
            ImageIcon icon = new ImageIcon(pathFotoKTM);
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblPreviewKTM.setIcon(new ImageIcon(img));
            lblPreviewKTM.setText("");
        }
    }

    private void prosesSimpanBooking() {
        if (txtNamaBooking.getText().trim().isEmpty() || txtKegiatanBooking.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!"); return;
        }
        if (rbMahasiswa.isSelected() && (pathFotoKTM.equals("-") || txtIdentitasBooking.getText().isEmpty())) {
            JOptionPane.showMessageDialog(this, "Lengkapi data mahasiswa!"); return;
        }

        String hari = (String) cmbHari.getSelectedItem();
        String nama = txtNamaBooking.getText();
        String detail = txtIdentitasBooking.getText();
        String jenisUser = rbMahasiswa.isSelected() ? "Mahasiswa" : "Umum";
        double harga = 0;
        if (rbUmum.isSelected()) {
             try { harga = Double.parseDouble(lblHarga.getText().replace("Rp ", "")); } catch(Exception e){}
             detail += " | " + cmbMetodeBayar.getSelectedItem();
        }
        daftarAjuan.add(new PeminjamanGedung(nama, hari, jenisUser, detail, harga, pathFotoKTM));
        JOptionPane.showMessageDialog(this, "Peminjaman Berhasil!");
        resetFormBooking();
        cardLayout.show(mainPanel, "Menu");
    }

    private void resetFormBooking() {
        txtNamaBooking.setText(""); txtIdentitasBooking.setText(""); txtKegiatanBooking.setText("");
        lblPreviewKTM.setIcon(null); lblPreviewKTM.setText("Belum ada foto");
        pathFotoKTM = "-"; cmbHari.setSelectedIndex(0);
        rbMahasiswa.setSelected(true); updateFormBooking();
    }

    // --- 3. STUDI BANDING (Updated: Ada Pilihan Waktu) ---
    private void initVisitPanel() {
        visitPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Form Studi Banding"),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Data ComboBox Tanggal
        Vector<String> tgl = new Vector<>(); for(int i=1; i<=31; i++) tgl.add(String.valueOf(i));
        String[] bln = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        Vector<String> thn = new Vector<>(); for(int i=2024; i<=2030; i++) thn.add(String.valueOf(i));
        String[] jam = {"08:00 WIB", "10:00 WIB", "13:00 WIB", "15:00 WIB"};

        // Inisialisasi Komponen
        cmbTimRiset = new JComboBox<>(new String[]{"Tim Robot Soccer (ICHIRO)", "Tim Robot Terbang (BAYUCARAKA)", "Tim Robot Tari (VI-ROSE)", "Tim Robot Kapal (BARUNASTRA)"});
        
        // Panel Tanggal (Horizontal)
        JPanel pnlTanggal = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cmbTanggal = new JComboBox<>(tgl);
        cmbBulan = new JComboBox<>(bln);
        cmbTahun = new JComboBox<>(thn);
        pnlTanggal.add(cmbTanggal); pnlTanggal.add(cmbBulan); pnlTanggal.add(cmbTahun);

        cmbJam = new JComboBox<>(jam);

        txtNamaVisit = new JTextField(20);
        txtInstansiVisit = new JTextField(20);
        txtAlamatVisit = new JTextField(20);
        txtTujuanVisit = new JTextArea(4, 20); txtTujuanVisit.setLineWrap(true);

        // Layouting (Baris per baris)
        int row = 0;
        addComponent(formPanel, new JLabel("Pilih Tim Riset:"), 0, row, 1, 1, 0, 0, GridBagConstraints.NONE);
        addComponent(formPanel, cmbTimRiset, 1, row++, 1, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);
        
        addComponent(formPanel, new JLabel("Tanggal Kunjungan:"), 0, row, 1, 1, 0, 0, GridBagConstraints.NONE);
        addComponent(formPanel, pnlTanggal, 1, row++, 1, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);

        addComponent(formPanel, new JLabel("Jam Kunjungan:"), 0, row, 1, 1, 0, 0, GridBagConstraints.NONE);
        addComponent(formPanel, cmbJam, 1, row++, 1, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);

        addComponent(formPanel, new JLabel("Nama Perwakilan:"), 0, row, 1, 1, 0, 0, GridBagConstraints.NONE);
        addComponent(formPanel, txtNamaVisit, 1, row++, 1, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);
        
        addComponent(formPanel, new JLabel("Asal Instansi:"), 0, row, 1, 1, 0, 0, GridBagConstraints.NONE);
        addComponent(formPanel, txtInstansiVisit, 1, row++, 1, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);
        
        addComponent(formPanel, new JLabel("Alamat Instansi:"), 0, row, 1, 1, 0, 0, GridBagConstraints.NONE);
        addComponent(formPanel, txtAlamatVisit, 1, row++, 1, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);
        
        addComponent(formPanel, new JLabel("Tujuan Studi:"), 0, row, 1, 1, 0, 0, GridBagConstraints.NONE);
        addComponent(formPanel, new JScrollPane(txtTujuanVisit), 1, row++, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);

        // Tombol
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSubmit = new JButton("Kirim Ajuan");
        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        
        btnSubmit.addActionListener(e -> {
            if(txtNamaVisit.getText().isEmpty() || txtInstansiVisit.getText().isEmpty() || txtTujuanVisit.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi semua data!"); return;
            }
            // Format Waktu Gabungan
            String waktuFix = cmbTanggal.getSelectedItem() + " " + cmbBulan.getSelectedItem() + " " + cmbTahun.getSelectedItem() + ", Pukul " + cmbJam.getSelectedItem();

            daftarAjuan.add(new StudiBanding(
                txtNamaVisit.getText(), 
                (String)cmbTimRiset.getSelectedItem(),
                txtAlamatVisit.getText(),
                txtTujuanVisit.getText(),
                txtInstansiVisit.getText(),
                waktuFix // Kirim data waktu ke Class Baru
            ));
            JOptionPane.showMessageDialog(this, "Ajuan Studi Banding Terkirim!");
            cardLayout.show(mainPanel, "Menu");
            // Reset sederhana
            txtNamaVisit.setText(""); txtInstansiVisit.setText(""); txtAlamatVisit.setText(""); txtTujuanVisit.setText("");
        });

        btnPanel.add(btnBack); btnPanel.add(btnSubmit);
        visitPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        visitPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    // --- 4. STATUS (Sama) ---
    private void initStatusPanel() {
        statusPanel = new JPanel(new BorderLayout());
        txtAreaStatus = new JTextArea();
        txtAreaStatus.setEditable(false);
        txtAreaStatus.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtAreaStatus.setMargin(new Insets(10,10,10,10));
        JButton btnBack = new JButton("Kembali ke Menu");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        statusPanel.add(new JScrollPane(txtAreaStatus), BorderLayout.CENTER);
        statusPanel.add(btnBack, BorderLayout.SOUTH);
    }

    private void updateStatusDisplay() {
        if (daftarAjuan.isEmpty()) txtAreaStatus.setText("Belum ada ajuan.");
        else {
            StringBuilder sb = new StringBuilder();
            for (Permintaan p : daftarAjuan) sb.append(p.getInfoLengkap()).append("\n");
            txtAreaStatus.setText(sb.toString());
        }
        txtAreaStatus.setCaretPosition(0);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(SistemRobotikaGUI::new);
    }
}