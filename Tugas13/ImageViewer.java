import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageViewer extends JFrame {

    private JLabel imageLabel;
    private ImageIcon imageIcon;
    private double scale = 1.0;

    public ImageViewer() {

        setTitle("Simple Image Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane scrollPane = new JScrollPane(imageLabel);

        JButton openButton = new JButton("Open Image");
        JButton zoomInButton = new JButton("Zoom In");
        JButton zoomOutButton = new JButton("Zoom Out");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(zoomInButton);
        buttonPanel.add(zoomOutButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openImage();
            }
        });

        zoomInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeZoom(1.25); 
            }
        });

        zoomOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeZoom(0.80); 
            }
        });

        setVisible(true);
    }

    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif"
        );
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imageIcon = new ImageIcon(file.getAbsolutePath());
            scale = 1.0;
            imageLabel.setIcon(imageIcon);
        }
    }

    private void changeZoom(double factor) {
        if (imageIcon == null) return;

        scale *= factor;

        int newWidth = (int) (imageIcon.getIconWidth() * scale);
        int newHeight = (int) (imageIcon.getIconHeight() * scale);

        Image newImg = imageIcon.getImage().getScaledInstance(
                newWidth, newHeight, Image.SCALE_SMOOTH
        );

        imageLabel.setIcon(new ImageIcon(newImg));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageViewer());
    }
}
