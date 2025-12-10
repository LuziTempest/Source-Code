import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.util.Vector;

public class SistemRobotikaGUI extends JFrame {
    private ArrayList<Permintaan> daftarAjuan = new ArrayList<>();
    private HashMap<String, String> dataUser = new HashMap<>();

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JPanel loginPanel;
    private JPanel registerPanel;
    private JPanel menuPanel;
    private JPanel bookingPanel;
    private JPanel statusPanel;
    private JPanel visitPanel;
    private JPanel adminPanel;

    private JComboBox<String> cmbHari;
    private JRadioButton rbMahasiswa, rbUmum;
    private JTextField txtNamaBooking, txtIdentitasBooking;
    private JTextArea txtKegiatanBooking;
    private JComboBox<String> cmbMetodeBayar;
    private JLabel lblHarga, lblPreviewKTM;
    private JButton btnSubmitBooking;
    private String pathFotoKTM = "-";
    private JPanel panelFormDinamis;

    private JComboBox<String> cmbTimRiset;
    private JComboBox<String> cmbTanggal, cmbBulan, cmbTahun, cmbJam;
    private JTextField txtNamaVisit, txtInstansiVisit, txtAlamatVisit;
    private JTextArea txtTujuanVisit;

    private JTextArea txtAreaStatus;

    private JTable tabelAdmin;
    private DefaultTableModel modelAdmin;

    private String currentUser = "";

    public SistemRobotikaGUI() {
        super("Sistem Manajemen Robotika");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        dataUser.put("user", "user123"); 
        dataUser.put("mahasiswa", "mhs123");

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initLoginPanel();
        initRegisterPanel();
        initMenuPanel();
        initBookingPanel();
        initStatusPanel();
        initVisitPanel();
        initAdminPanel();

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registerPanel, "Register");
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(bookingPanel, "Booking");
        mainPanel.add(statusPanel, "Status");
        mainPanel.add(visitPanel, "Visit");
        mainPanel.add(adminPanel, "Admin");

        add(mainPanel);
        setVisible(true);
    }

    private void initLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(new Color(240, 248, 255));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel lblJudul = new JLabel("Login Sistem Robotika");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        
        JTextField txtUser = new JTextField(20);
        JPasswordField txtPass = new JPasswordField(20);
        
        JButton btnLogin = new JButton("LOGIN MASUK");
        btnLogin.setBackground(new Color(100, 149, 237));
        btnLogin.setForeground(Color.BLACK); 
        btnLogin.setPreferredSize(new Dimension(150, 40));

        JButton btnRegister = new JButton("Belum punya akun? Daftar");
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(Color.BLUE);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(lblJudul, gbc);
        
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1; centerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridy = 2; centerPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; 
        gbc.gridy = 1; centerPanel.add(txtUser, gbc);
        gbc.gridy = 2; centerPanel.add(txtPass, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(btnLogin, gbc);
        
        gbc.gridy = 4;
        centerPanel.add(btnRegister, gbc);

        loginPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(0, 0, 20, 20));

        JButton btnAdmin = new JButton("Login Admin");
        btnAdmin.setBackground(new Color(112, 128, 144));
        btnAdmin.setForeground(Color.BLACK);
        
        btnAdmin.addActionListener(e -> prosesLoginAdmin());
        
        bottomPanel.add(btnAdmin);
        loginPanel.add(bottomPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> {
            String u = txtUser.getText();
            String p = new String(txtPass.getPassword());
            
            if(dataUser.containsKey(u) && dataUser.get(u).equals(p)) {
                currentUser = u;
                txtUser.setText(""); txtPass.setText("");
                JOptionPane.showMessageDialog(this, "Selamat Datang, " + currentUser + "!");
                cardLayout.show(mainPanel, "Menu");
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password Salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegister.addActionListener(e -> cardLayout.show(mainPanel, "Register"));
    }

    private void initRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(new Color(255, 250, 240));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel lblReg = new JLabel("Pendaftaran Akun Baru");
        lblReg.setFont(new Font("SansSerif", Font.BOLD, 20));
        
        JTextField txtNewUser = new JTextField(20);
        JPasswordField txtNewPass = new JPasswordField(20);
        JPasswordField txtConfirmPass = new JPasswordField(20);
        
        JButton btnSubmitReg = new JButton("Daftar Sekarang");
        JButton btnCancel = new JButton("Batal");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        registerPanel.add(lblReg, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1; registerPanel.add(new JLabel("Username Baru:"), gbc);
        gbc.gridy = 2; registerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridy = 3; registerPanel.add(new JLabel("Konfirmasi Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1; registerPanel.add(txtNewUser, gbc);
        gbc.gridy = 2; registerPanel.add(txtNewPass, gbc);
        gbc.gridy = 3; registerPanel.add(txtConfirmPass, gbc);

        JPanel pnlBtn = new JPanel();
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnCancel); pnlBtn.add(btnSubmitReg);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(pnlBtn, gbc);

        btnSubmitReg.addActionListener(e -> {
            String u = txtNewUser.getText();
            String p = new String(txtNewPass.getPassword());
            String cp = new String(txtConfirmPass.getPassword());

            if(u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!");
            } else if (dataUser.containsKey(u)) {
                JOptionPane.showMessageDialog(this, "Username sudah terpakai!");
            } else if (!p.equals(cp)) {
                JOptionPane.showMessageDialog(this, "Password konfirmasi tidak cocok!");
            } else {
                dataUser.put(u, p);
                JOptionPane.showMessageDialog(this, "Akun berhasil dibuat! Silakan Login.");
                txtNewUser.setText(""); txtNewPass.setText(""); txtConfirmPass.setText("");
                cardLayout.show(mainPanel, "Login");
            }
        });

        btnCancel.addActionListener(e -> {
            txtNewUser.setText(""); txtNewPass.setText(""); txtConfirmPass.setText("");
            cardLayout.show(mainPanel, "Login");
        });
    }

    private void initMenuPanel() {
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(240, 248, 255));

        JPanel centerStack = new JPanel(new GridBagLayout());
        centerStack.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Menu Utama User");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0; titleGbc.gridy = 0;
        titleGbc.insets = new Insets(0, 0, 40, 0);
        centerStack.add(title, titleGbc);

        Dimension btnSize = new Dimension(280, 55);

        JButton btnBooking = createStyledButton("Peminjaman Gedung", new Color(100, 149, 237), btnSize);
        JButton btnStatus = createStyledButton("Status Ajuan Saya", new Color(255, 215, 0), btnSize);
        JButton btnVisit = createStyledButton("Studi Banding", new Color(60, 179, 113), btnSize);

        btnBooking.addActionListener(e -> {
            txtNamaBooking.setText(currentUser); 
            cardLayout.show(mainPanel, "Booking");
        });
        
        btnStatus.addActionListener(e -> {
            updateStatusDisplay();
            cardLayout.show(mainPanel, "Status");
        });

        btnVisit.addActionListener(e -> {
            txtNamaVisit.setText(currentUser);
            cardLayout.show(mainPanel, "Visit");
        });

        gbc.gridy = 1; centerStack.add(btnBooking, gbc);
        gbc.gridy = 2; centerStack.add(btnStatus, gbc);
        gbc.gridy = 3; centerStack.add(btnVisit, gbc);

        menuPanel.add(centerStack, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        
        JButton btnLogout = new JButton("Logout / Keluar");
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(Color.BLACK); 
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                currentUser = "";
                cardLayout.show(mainPanel, "Login");
            }
        });

        bottomPanel.add(btnLogout);
        menuPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void prosesLoginAdmin() {
        String password = JOptionPane.showInputDialog(this, "Masukkan Kode Admin:", "Login Admin", JOptionPane.QUESTION_MESSAGE);
        if (password != null && password.equals("admin123")) { 
            refreshTabelAdmin();
            cardLayout.show(mainPanel, "Admin");
        } else if (password != null) {
            JOptionPane.showMessageDialog(this, "Kode Salah!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initAdminPanel() {
        adminPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("Dashboard Admin - Persetujuan Ajuan");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setBorder(new EmptyBorder(15, 15, 15, 15));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        adminPanel.add(lblTitle, BorderLayout.NORTH);

        String[] kolom = {"No", "Pemohon", "Jenis Ajuan", "Status Saat Ini"};
        modelAdmin = new DefaultTableModel(kolom, 0);
        tabelAdmin = new JTable(modelAdmin);
        tabelAdmin.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tabelAdmin);
        adminPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        JButton btnSetuju = new JButton("SETUJUI");
        btnSetuju.setBackground(new Color(144, 238, 144));
        
        JButton btnTolak = new JButton("TOLAK");
        btnTolak.setBackground(new Color(255, 182, 193));
        
        JButton btnDetail = new JButton("Lihat Detail");
        
        JButton btnLogout = new JButton("Logout Admin");
        
        btnSetuju.addActionListener(e -> updateStatusAjuan("DISETUJUI"));
        btnTolak.addActionListener(e -> updateStatusAjuan("DITOLAK"));
        
        btnDetail.addActionListener(e -> {
            int row = tabelAdmin.getSelectedRow();
            if (row != -1) {
                Permintaan p = daftarAjuan.get(row);
                JOptionPane.showMessageDialog(this, p.getInfoLengkap(), "Detail Ajuan", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnLogout.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        actionPanel.add(btnDetail);
        actionPanel.add(btnTolak);
        actionPanel.add(btnSetuju);
        actionPanel.add(btnLogout);
        
        adminPanel.add(actionPanel, BorderLayout.SOUTH);
    }

    private void updateStatusAjuan(String statusBaru) {
        int row = tabelAdmin.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris ajuan terlebih dahulu!");
            return;
        }
        Permintaan p = daftarAjuan.get(row);
        p.setStatus(statusBaru);
        refreshTabelAdmin();
        JOptionPane.showMessageDialog(this, "Status berhasil diubah menjadi: " + statusBaru);
    }

    private void refreshTabelAdmin() {
        modelAdmin.setRowCount(0);
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

    private void initBookingPanel() {
        bookingPanel = new JPanel(new BorderLayout());
        JPanel formContentPanel = new JPanel();
        formContentPanel.setLayout(new BoxLayout(formContentPanel, BoxLayout.Y_AXIS));
        formContentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Form Peminjaman Gedung"),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JPanel pnlHari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] hari = {"Senin (Tersedia)", "Selasa (Penuh)", "Rabu (Tersedia)", "Kamis (Tersedia)", "Jumat (Penuh)", "Sabtu (Weekend)", "Minggu (Weekend)"};
        cmbHari = new JComboBox<>(hari);

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
                sb.append(p.getInfoLengkap()).append("\n"); 
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