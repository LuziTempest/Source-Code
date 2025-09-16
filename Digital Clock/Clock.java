import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Clock {
    private JFrame frame;
    private JLabel timeLabel, dateLabel, tempLabel;
    private ClockDisplay clock;
    private boolean clockRunning = false;
    private TimerThread timerThread;

    public Clock() {
        makeFrame();
        clock = new ClockDisplay();
        updateInitialDisplay(); 
        
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        start();
    }
    
    private void updateInitialDisplay() {
        dateLabel.setText(clock.getDateString());
        timeLabel.setText(clock.getTimeString());
        tempLabel.setText(clock.getTemperatureString());
    }

    private void start() {
        if (!clockRunning) {
            clockRunning = true;
            timerThread = new TimerThread();
            timerThread.start();
        }
    }

    private void stop() {
        clockRunning = false;
    }

    private void step() {
        clock.timeTick();
        timeLabel.setText(clock.getTimeString());
        dateLabel.setText(clock.getDateString());
        tempLabel.setText(clock.getTemperatureString());
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(frame,
                "Digital Clock" +
                "Menampilkan Jam, Tanggal, dan Suhu.",
                "About Clock",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void quit() {
        System.exit(0);
    }

    private void makeFrame() {
        frame = new JFrame("Digital Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        makeMenuBar(frame);
        
        contentPane.setLayout(new BorderLayout(12, 12));
        
        timeLabel = new JLabel(" ", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 80)); // Font besar untuk waktu
        contentPane.add(timeLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        dateLabel = new JLabel(" ", SwingConstants.LEFT);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(dateLabel, BorderLayout.WEST);

        tempLabel = new JLabel(" ", SwingConstants.RIGHT);
        tempLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(tempLabel, BorderLayout.EAST);

        JPanel toolbar = new JPanel();
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> start());
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stop());
        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(e -> step());
                
        toolbar.add(startButton);
        toolbar.add(stopButton);
        toolbar.add(stepButton);
        bottomPanel.add(toolbar, BorderLayout.CENTER);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void makeMenuBar(JFrame frame) {
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        JMenu menu = new JMenu("File");
        menubar.add(menu);
        JMenuItem item = new JMenuItem("About Clock...");
        item.addActionListener(e -> showAbout());
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem("Quit");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        item.addActionListener(e -> quit());
        menu.add(item);
    }

    class TimerThread extends Thread {
        public void run() {
            while (clockRunning) {
                SwingUtilities.invokeLater(() -> step());
                pause();
            }
        }

        private void pause() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exc) {
                // should not happen
            }
        }
    }
}