import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// === PANEL LOGIN ===
class PanelLogin extends JPanel {
    private SistemRobotikaGUI gui;

    public PanelLogin(SistemRobotikaGUI gui) {
        this.gui = gui;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

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
        btnLogin.setForeground(Color.BLACK); // Warna Hitam
        btnLogin.setPreferredSize(new Dimension(150, 40));

        JButton btnRegister = new JButton("Belum punya akun? Daftar");
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(Color.BLUE);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; centerPanel.add(lblJudul, gbc);
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1; centerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridy = 2; centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; centerPanel.add(txtUser, gbc);
        gbc.gridy = 2; centerPanel.add(txtPass, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(btnLogin, gbc);
        gbc.gridy = 4; centerPanel.add(btnRegister, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Tombol Admin di Kanan Bawah
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(0, 0, 20, 20));
        JButton btnAdmin = new JButton("Login Admin");
        btnAdmin.setBackground(new Color(112, 128, 144));
        btnAdmin.setForeground(Color.BLACK); // Warna Hitam
        
        bottomPanel.add(btnAdmin);
        add(bottomPanel, BorderLayout.SOUTH);

        // Logic
        btnLogin.addActionListener(e -> {
            String u = txtUser.getText();
            String p = new String(txtPass.getPassword());
            if (gui.getDataUser().containsKey(u) && gui.getDataUser().get(u).equals(p)) {
                gui.setCurrentUser(u);
                txtUser.setText(""); txtPass.setText("");
                JOptionPane.showMessageDialog(this, "Selamat Datang, " + u + "!");
                gui.gantiHalaman("Menu");
            } else {
                JOptionPane.showMessageDialog(this, "Username/Password Salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegister.addActionListener(e -> gui.gantiHalaman("Register"));
        
        btnAdmin.addActionListener(e -> {
            String pass = JOptionPane.showInputDialog(this, "Masukkan Kode Admin:");
            if ("admin123".equals(pass)) gui.gantiHalaman("Admin");
            else if (pass != null) JOptionPane.showMessageDialog(this, "Kode Salah!");
        });
    }
}

// === PANEL REGISTER ===
class PanelRegister extends JPanel {
    public PanelRegister(SistemRobotikaGUI gui) {
        setLayout(new GridBagLayout());
        setBackground(new Color(255, 250, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField txtNewUser = new JTextField(20);
        JPasswordField txtNewPass = new JPasswordField(20);
        JPasswordField txtConfirm = new JPasswordField(20);
        JButton btnSubmit = new JButton("Daftar Sekarang");
        JButton btnBatal = new JButton("Batal");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; 
        JLabel title = new JLabel("Pendaftaran Akun Baru");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1; add(new JLabel("Username:"), gbc);
        gbc.gridy = 2; add(new JLabel("Password:"), gbc);
        gbc.gridy = 3; add(new JLabel("Konfirmasi:"), gbc);

        gbc.gridx = 1; 
        gbc.gridy = 1; add(txtNewUser, gbc);
        gbc.gridy = 2; add(txtNewPass, gbc);
        gbc.gridy = 3; add(txtConfirm, gbc);

        JPanel btnPanel = new JPanel(); btnPanel.setOpaque(false);
        btnPanel.add(btnBatal); btnPanel.add(btnSubmit);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(btnPanel, gbc);

        btnSubmit.addActionListener(e -> {
            String u = txtNewUser.getText();
            String p = new String(txtNewPass.getPassword());
            if (u.isEmpty() || p.isEmpty()) JOptionPane.showMessageDialog(this, "Isi semua data!");
            else if (gui.getDataUser().containsKey(u)) JOptionPane.showMessageDialog(this, "Username sudah dipakai!");
            else if (!p.equals(new String(txtConfirm.getPassword()))) JOptionPane.showMessageDialog(this, "Password tidak cocok!");
            else {
                gui.getDataUser().put(u, p);
                JOptionPane.showMessageDialog(this, "Akun dibuat!");
                txtNewUser.setText(""); txtNewPass.setText(""); txtConfirm.setText("");
                gui.gantiHalaman("Login");
            }
        });
        btnBatal.addActionListener(e -> gui.gantiHalaman("Login"));
    }
}