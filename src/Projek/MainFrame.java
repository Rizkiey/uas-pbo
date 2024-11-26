package Projek;
import javax.swing.*;

public class MainFrame extends JFrame {
    private JButton btnMahasiswa, btnMatakuliah, btnKRS, btnExit;

    public MainFrame() {
        setTitle("Sistem Informasi Akademik");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblTitle = new JLabel("Menu Utama");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(100, 20, 200, 30);
        add(lblTitle);

        btnMahasiswa = new JButton("CRUD Mahasiswa");
        btnMahasiswa.setBounds(100, 70, 200, 30);
        add(btnMahasiswa);

        btnMatakuliah = new JButton("CRUD Mata Kuliah");
        btnMatakuliah.setBounds(100, 110, 200, 30);
        add(btnMatakuliah);

        btnKRS = new JButton("CRUD KRS");
        btnKRS.setBounds(100, 150, 200, 30);
        add(btnKRS);

        btnExit = new JButton("Keluar");
        btnExit.setBounds(100, 190, 200, 30);
        add(btnExit);

        // Event Handlers
        btnMahasiswa.addActionListener(e -> {
            new FormMahasiswa().setVisible(true); // Membuka Form Mahasiswa
            dispose(); // Menutup MainFrame
        });

        btnMatakuliah.addActionListener(e -> {
            new FormMatakuliah().setVisible(true); // Membuka Form Mata Kuliah
            dispose(); // Menutup MainFrame
        });

        btnKRS.addActionListener(e -> {
            new FormKRS().setVisible(true); // Membuka Form KRS
            dispose(); // Menutup MainFrame
        });

        btnExit.addActionListener(e -> System.exit(0)); // Keluar dari aplikasi
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
