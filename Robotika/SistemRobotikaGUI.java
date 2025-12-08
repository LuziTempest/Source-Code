import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel; // Import Baru untuk Tabel Admin
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.File;
import java.util.Vector;

public class SistemRobotikaGUI extends JFrame {
    // Penyimpanan data
    private ArrayList<Permintaan> daftarAjuan = new ArrayList<>();

    // Komponen Utama
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Panel Halaman
    private JPanel menuPanel;
    private JPanel bookingPanel;
    private JPanel statusPanel;
    private JPanel visitPanel;
    private JPanel adminPanel; // BARU: Panel Admin

    // Komponen Booking
    private JComboBox<String> cmbHari;
    private JRadioButton rbMahasiswa, rbUmum;
    private JTextField txtNamaBooking, txtIdentitasBooking;
    private JTextArea txtKegiatanBooking;
    private JComboBox<String> cmbMetodeBayar;
    private JLabel lblHarga, lblPreviewKTM;
    private JButton btnSubmitBooking;
    private String pathFotoKTM = "-";
    private JPanel panelFormDinamis;

    // Komponen Studi Banding
    private JComboBox<String> cmbTimRiset;
    private JComboBox<String> cmbTanggal, cmbBulan, cmbTahun, cmbJam;
    private JTextField txtNamaVisit, txtInstansiVisit, txtAlamatVisit;
    private JTextArea txtTujuanVisit;

    // Komponen Status
    private JTextArea txtAreaStatus;

    // Komponen Admin (BARU)
    private JTable tabelAdmin;
    private DefaultTableModel modelAdmin;

    public SistemRobotikaGUI() {
        super("Sistem Manajemen Robotika");
        setSize(850, 650); // Sedikit diperlebar agar tabel muat
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inisialisasi Semua Halaman
        initMenuPanel();
        initBookingPanel();
        initStatusPanel();
        initVisitPanel();
        initAdminPanel(); // BARU

        // Tambah ke Main Panel
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(bookingPanel, "Booking");
        mainPanel.add(statusPanel, "Status");
        mainPanel.add(visitPanel, "Visit");
        mainPanel.add(adminPanel, "Admin"); // BARU

        add(mainPanel);
        setVisible(true);
    }

    // --- 1. MENU UTAMA (Updated: Tombol Admin di Kanan Bawah) ---
   // --- 1. MENU UTAMA (Updated: Layout Lebih Rapi & Tombol Admin Besar) ---
    private void initMenuPanel() {
        // Gunakan BorderLayout untuk memisahkan area Tengah (Menu) dan Bawah (Admin)
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(240, 248, 255));

        // --- A. PANEL TENGAH (Untuk Judul & 3 Tombol Utama) ---
        // Kita bungkus lagi pakai GridBagLayout supaya benar-benar di tengah layar
        JPanel centerStack = new JPanel(new GridBagLayout());
        centerStack.setOpaque(false); // Transparan agar warna background menuPanel terlihat

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.insets = new Insets(10, 0, 10, 0); // Jarak antar elemen vertikal
        gbc.fill = GridBagConstraints.HORIZONTAL; // Agar lebar tombol seragam

        // 1. Judul
        JLabel title = new JLabel("Sistem Manajemen Robotika");
        title.setFont(new Font("SansSerif", Font.BOLD, 28)); // Font diperbesar sedikit
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0; titleGbc.gridy = 0;
        titleGbc.insets = new Insets(0, 0, 40, 0); // Jarak dari judul ke tombol pertama
        centerStack.add(title, titleGbc);

        // 2. Tombol-Tombol Utama
        Dimension btnSize = new Dimension(280, 55); // Ukuran tombol utama

        JButton btnBooking = createStyledButton("Peminjaman Gedung", new Color(100, 149, 237), btnSize);
        JButton btnStatus = createStyledButton("Status Ajuan User", new Color(255, 215, 0), btnSize);
        JButton btnVisit = createStyledButton("Studi Banding", new Color(60, 179, 113), btnSize);

        // Aksi Tombol
        btnBooking.addActionListener(e -> cardLayout.show(mainPanel, "Booking"));
        btnStatus.addActionListener(e -> {
            updateStatusDisplay();
            cardLayout.show(mainPanel, "Status");
        });
        btnVisit.addActionListener(e -> cardLayout.show(mainPanel, "Visit"));

        // Masukkan ke centerStack
        gbc.gridy = 1; centerStack.add(btnBooking, gbc);
        gbc.gridy = 2; centerStack.add(btnStatus, gbc);
        gbc.gridy = 3; centerStack.add(btnVisit, gbc);

        // Tambahkan centerStack ke menuPanel
        menuPanel.add(centerStack, BorderLayout.CENTER);


        // --- B. PANEL BAWAH (Khusus Tombol Admin) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Rata Kanan
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(0, 0, 20, 20)); // Margin dari pinggir layar (Bawah, Kanan)

        // Tombol Admin (Diperbesar)
        JButton btnAdmin = new JButton("Login Admin");
        btnAdmin.setPreferredSize(new Dimension(160, 45)); // UKURAN LEBIH BESAR
        btnAdmin.setBackground(new Color(112, 128, 144)); // Warna SlateGray (Lebih gelap/serius)
        btnAdmin.setForeground(Color.BLACK);
        btnAdmin.setFont(new Font("SansSerif", Font.BOLD, 14)); // Font tebal dan jelas
        btnAdmin.setFocusPainted(false);
        
        // Ikon gembok sederhana (opsional, pakai teks saja sudah cukup jelas dengan ukuran ini)
        btnAdmin.addActionListener(e -> prosesLoginAdmin());

        bottomPanel.add(btnAdmin);
        
        // Tambahkan bottomPanel ke menuPanel
        menuPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- LOGIKA LOGIN ADMIN ---
    private void prosesLoginAdmin() {
        String password = JOptionPane.showInputDialog(this, "Masukkan Kode Admin:", "Login Admin", JOptionPane.QUESTION_MESSAGE);
        if (password != null && password.equals("admin123")) { // PASSWORD HARDCODED
            refreshTabelAdmin(); // Load data terbaru ke tabel
            cardLayout.show(mainPanel, "Admin");
        } else if (password != null) {
            JOptionPane.showMessageDialog(this, "Kode Salah!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- 5. PANEL ADMIN (DASHBOARD) - BARU ---
    private void initAdminPanel() {
        adminPanel = new JPanel(new BorderLayout());
        
        // Header Admin
        JLabel lblTitle = new JLabel("Dashboard Admin - Persetujuan Ajuan");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setBorder(new EmptyBorder(15, 15, 15, 15));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        adminPanel.add(lblTitle, BorderLayout.NORTH);

        // Tabel Data
        String[] kolom = {"No", "Pemohon", "Jenis Ajuan", "Status Saat Ini"};
        modelAdmin = new DefaultTableModel(kolom, 0);
        tabelAdmin = new JTable(modelAdmin);
        tabelAdmin.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tabelAdmin);
        adminPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel Tombol Aksi
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        JButton btnSetuju = new JButton("SETUJUI");
        btnSetuju.setBackground(new Color(144, 238, 144)); // Hijau Muda
        
        JButton btnTolak = new JButton("TOLAK");
        btnTolak.setBackground(new Color(255, 182, 193)); // Merah Muda
        
        JButton btnDetail = new JButton("Lihat Detail"); // Opsional: Lihat isi lengkap
        
        JButton btnLogout = new JButton("Logout / Kembali");
        
        // Listener Aksi
        btnSetuju.addActionListener(e -> updateStatusAjuan("DISETUJUI"));
        btnTolak.addActionListener(e -> updateStatusAjuan("DITOLAK"));
        
        btnDetail.addActionListener(e -> {
            int row = tabelAdmin.getSelectedRow();
            if (row != -1) {
                Permintaan p = daftarAjuan.get(row);
                JOptionPane.showMessageDialog(this, p.getInfoLengkap(), "Detail Ajuan", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnLogout.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        actionPanel.add(btnDetail);
        actionPanel.add(btnTolak);
        actionPanel.add(btnSetuju);
        actionPanel.add(btnLogout);
        
        adminPanel.add(actionPanel, BorderLayout.SOUTH);
    }

    // --- Helper Admin: Update Status ---
    private void updateStatusAjuan(String statusBaru) {
        int row = tabelAdmin.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris ajuan terlebih dahulu!");
            return;
        }

        // Update Data di ArrayList
        Permintaan p = daftarAjuan.get(row);
        p.setStatus(statusBaru);

        // Update Tampilan Tabel
        refreshTabelAdmin();
        JOptionPane.showMessageDialog(this, "Status berhasil diubah menjadi: " + statusBaru);
    }

    // --- Helper Admin: Refresh Tabel ---
    private void refreshTabelAdmin() {
        modelAdmin.setRowCount(0); // Hapus data lama di tabel GUI
        int no = 1;
        for (Permintaan p : daftarAjuan) {
            modelAdmin.addRow(new Object[]{
                no++,
                p.getNama(),
                (p instanceof PeminjamanGedung) ? "Peminjaman Gedung" : "Studi Banding",
                p.getStatus()
            });
        }
    }

    private JButton createStyledButton(String text, Color bg, Dimension dim) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(dim);
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }

    // --- 2. PEMINJAMAN GEDUNG (Validasi Jadwal Tetap Ada) ---
    private void initBookingPanel() {
        bookingPanel = new JPanel(new BorderLayout());
        JPanel formContentPanel = new JPanel();
        formContentPanel.setLayout(new BoxLayout(formContentPanel, BoxLayout.Y_AXIS));
        formContentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Form Peminjaman Gedung"),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Header & Dropdown Hari
        JPanel pnlHari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] hari = {"Senin (Tersedia)", "Selasa (Penuh)", "Rabu (Tersedia)", "Kamis (Tersedia)", "Jumat (Penuh)", "Sabtu (Weekend)", "Minggu (Weekend)"};
        cmbHari = new JComboBox<>(hari);

        // Renderer Visual (Merah jika Penuh)
        cmbHari.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String text = (String) value;
                if (text != null && text.contains("(Penuh)")) {
                    setForeground(Color.RED);
                    setEnabled(false);
                } else {
                    setForeground(Color.BLACK);
                    setEnabled(true);
                }
                return c;
            }
        });

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

        txtNamaBooking = new JTextField(20);
        txtIdentitasBooking = new JTextField(20);
        txtKegiatanBooking = new JTextArea(4, 20); txtKegiatanBooking.setLineWrap(true);
        cmbMetodeBayar = new JComboBox<>(new String[]{"Tunai", "Transfer Bank", "QRIS"});
        lblHarga = new JLabel("Harga: Rp 0");
        lblPreviewKTM = new JLabel("Belum ada foto", SwingConstants.CENTER);
        lblPreviewKTM.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblPreviewKTM.setPreferredSize(new Dimension(120, 120));

        btnSubmitBooking = new JButton("Ajukan Peminjaman");

        ActionListener typeListener = e -> updateFormBooking();
        rbMahasiswa.addActionListener(typeListener); 
        rbUmum.addActionListener(typeListener);
        
        cmbHari.addActionListener(e -> {
            hitungHarga();
            cekKetersediaanHari();
        });

        rbMahasiswa.setSelected(true); 
        updateFormBooking();
        cekKetersediaanHari();

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> { resetFormBooking(); cardLayout.show(mainPanel, "Menu"); });
        
        btnSubmitBooking.addActionListener(e -> prosesSimpanBooking());
        
        btnPanel.add(btnBack); btnPanel.add(btnSubmitBooking);

        bookingPanel.add(new JScrollPane(formContentPanel), BorderLayout.CENTER);
        bookingPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void cekKetersediaanHari() {
        String hariPilih = (String) cmbHari.getSelectedItem();
        if (hariPilih != null && hariPilih.contains("(Penuh)")) {
            btnSubmitBooking.setEnabled(false);
            btnSubmitBooking.setText("Jadwal Penuh!");
        } else {
            btnSubmitBooking.setEnabled(true);
            btnSubmitBooking.setText("Ajukan Peminjaman");
        }
    }
    
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
        JOptionPane.showMessageDialog(this, "Peminjaman Berhasil! Status: Menunggu Konfirmasi.");
        resetFormBooking();
        cardLayout.show(mainPanel, "Menu");
    }

    private void resetFormBooking() {
        txtNamaBooking.setText(""); txtIdentitasBooking.setText(""); txtKegiatanBooking.setText("");
        lblPreviewKTM.setIcon(null); lblPreviewKTM.setText("Belum ada foto");
        pathFotoKTM = "-"; cmbHari.setSelectedIndex(0);
        rbMahasiswa.setSelected(true); updateFormBooking();
        cekKetersediaanHari();
    }

    // --- 3. STUDI BANDING ---
    private void initVisitPanel() {
        visitPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Form Studi Banding"),
                new EmptyBorder(10, 10, 10, 10)
        ));

        Vector<String> tgl = new Vector<>(); for(int i=1; i<=31; i++) tgl.add(String.valueOf(i));
        String[] bln = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        Vector<String> thn = new Vector<>(); for(int i=2024; i<=2030; i++) thn.add(String.valueOf(i));
        String[] jam = {"08:00 WIB", "10:00 WIB", "13:00 WIB", "15:00 WIB"};

        cmbTimRiset = new JComboBox<>(new String[]{"Tim Robot Soccer (ICHIRO)", "Tim Robot Terbang (BAYUCARAKA)", "Tim Robot Tari (VI-ROSE)", "Tim Robot Kapal (BARUNASTRA)"});
        
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

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSubmit = new JButton("Kirim Ajuan");
        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        
        btnSubmit.addActionListener(e -> {
            if(txtNamaVisit.getText().isEmpty() || txtInstansiVisit.getText().isEmpty() || txtTujuanVisit.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi semua data!"); return;
            }
            String waktuFix = cmbTanggal.getSelectedItem() + " " + cmbBulan.getSelectedItem() + " " + cmbTahun.getSelectedItem() + ", Pukul " + cmbJam.getSelectedItem();

            daftarAjuan.add(new StudiBanding(
                txtNamaVisit.getText(), 
                (String)cmbTimRiset.getSelectedItem(),
                txtAlamatVisit.getText(),
                txtTujuanVisit.getText(),
                txtInstansiVisit.getText(),
                waktuFix
            ));
            JOptionPane.showMessageDialog(this, "Ajuan Studi Banding Terkirim! Status: Menunggu.");
            cardLayout.show(mainPanel, "Menu");
            txtNamaVisit.setText(""); txtInstansiVisit.setText(""); txtAlamatVisit.setText(""); txtTujuanVisit.setText("");
        });

        btnPanel.add(btnBack); btnPanel.add(btnSubmit);
        visitPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        visitPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    // --- 4. STATUS AJUAN (User View) ---
    private void initStatusPanel() {
        statusPanel = new JPanel(new BorderLayout());
        
        JLabel lblHeader = new JLabel("Status Ajuan Saya");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblHeader.setBorder(new EmptyBorder(10,10,10,10));
        statusPanel.add(lblHeader, BorderLayout.NORTH);

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
            for (Permintaan p : daftarAjuan) {
                sb.append(p.getInfoLengkap()).append("\n"); // Info Lengkap sudah berisi Status
            }
            txtAreaStatus.setText(sb.toString());
        }
        txtAreaStatus.setCaretPosition(0);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(SistemRobotikaGUI::new);
    }
}