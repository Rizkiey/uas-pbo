package Projek;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class FormMahasiswa extends JFrame {
    private JTable table;
    private JTextField txtNama, txtNIM;
    private JButton btnTambah, btnHapus, btnRefresh;

    public FormMahasiswa() {
        setTitle("CRUD Data Mahasiswa");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(20, 20, 100, 25);
        add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(120, 20, 150, 25);
        add(txtNama);

        JLabel lblNIM = new JLabel("NIM:");
        lblNIM.setBounds(20, 60, 100, 25);
        add(lblNIM);

        txtNIM = new JTextField();
        txtNIM.setBounds(120, 60, 150, 25);
        add(txtNIM);

        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 100, 100, 25);
        add(btnTambah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(130, 100, 100, 25);
        add(btnHapus);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(240, 100, 100, 25);
        add(btnRefresh);

        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 140, 550, 200);
        add(sp);

        // Event Handlers
        btnTambah.addActionListener(e -> tambahData());
        btnHapus.addActionListener(e -> hapusData());
        btnRefresh.addActionListener(e -> loadData());

        loadData();
    }

    private void tambahData() {
        String nama = txtNama.getText();
        String nim = txtNIM.getText();

        if (!nama.isEmpty() && !nim.isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO mahasiswa (nama, nim) VALUES (?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nama);
                ps.setString(2, nim);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nama dan NIM tidak boleh kosong!");
        }
    }

    private void hapusData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int idmhs = (int) table.getValueAt(selectedRow, 0);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM mahasiswa WHERE idmhs = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, idmhs);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
        }
    }

    private void loadData() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nama", "NIM"}, 0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM mahasiswa")) {

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("idmhs"), rs.getString("nama"), rs.getString("nim")});
            }

            table.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormMahasiswa().setVisible(true));
    }
}

