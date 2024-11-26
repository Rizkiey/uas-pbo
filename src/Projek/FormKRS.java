/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Projek;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class FormKRS extends JFrame {
    private JTable table;
    private JComboBox<String> cbMahasiswa, cbMatakuliah;
    private JTextField txtSemester, txtTahunAjaran;
    private JButton btnTambah, btnHapus, btnRefresh;

    public FormKRS() {
        setTitle("CRUD Data KRS");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblMahasiswa = new JLabel("Mahasiswa:");
        lblMahasiswa.setBounds(20, 20, 100, 25);
        add(lblMahasiswa);

        cbMahasiswa = new JComboBox<>();
        cbMahasiswa.setBounds(120, 20, 200, 25);
        add(cbMahasiswa);

        JLabel lblMatakuliah = new JLabel("Mata Kuliah:");
        lblMatakuliah.setBounds(20, 60, 100, 25);
        add(lblMatakuliah);

        cbMatakuliah = new JComboBox<>();
        cbMatakuliah.setBounds(120, 60, 200, 25);
        add(cbMatakuliah);

        JLabel lblSemester = new JLabel("Semester:");
        lblSemester.setBounds(20, 100, 100, 25);
        add(lblSemester);

        txtSemester = new JTextField();
        txtSemester.setBounds(120, 100, 200, 25);
        add(txtSemester);

        JLabel lblTahunAjaran = new JLabel("Tahun Ajaran:");
        lblTahunAjaran.setBounds(20, 140, 100, 25);
        add(lblTahunAjaran);

        txtTahunAjaran = new JTextField();
        txtTahunAjaran.setBounds(120, 140, 200, 25);
        add(txtTahunAjaran);

        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 180, 100, 25);
        add(btnTambah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(130, 180, 100, 25);
        add(btnHapus);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(240, 180, 100, 25);
        add(btnRefresh);

        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 220, 750, 200);
        add(sp);

        btnTambah.addActionListener(e -> tambahData());
        btnHapus.addActionListener(e -> hapusData());
        btnRefresh.addActionListener(e -> loadData());

        loadData();
        loadDropdownData();
    }

    private void loadDropdownData() {
        cbMahasiswa.removeAllItems();
        cbMatakuliah.removeAllItems();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT idmhs, nama FROM mahasiswa");
            while (rs.next()) {
                cbMahasiswa.addItem(rs.getInt("idmhs") + " - " + rs.getString("nama"));
            }

            rs = stmt.executeQuery("SELECT idmk, nama FROM matakuliah");
            while (rs.next()) {
                cbMatakuliah.addItem(rs.getInt("idmk") + " - " + rs.getString("nama"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void tambahData() {
        String mahasiswa = (String) cbMahasiswa.getSelectedItem();
        String matakuliah = (String) cbMatakuliah.getSelectedItem();
        String semester = txtSemester.getText();
        String tahunAjaran = txtTahunAjaran.getText();

        if (mahasiswa != null && matakuliah != null && !semester.isEmpty() && !tahunAjaran.isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO krs (idmhs, idmk, semester, tahunajaran) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(mahasiswa.split(" - ")[0]));
                ps.setInt(2, Integer.parseInt(matakuliah.split(" - ")[0]));
                ps.setString(3, semester);
                ps.setString(4, tahunAjaran);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data KRS berhasil ditambahkan!");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
        }
    }

    private void hapusData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int idkrs = (int) table.getValueAt(selectedRow, 0);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM krs WHERE idkrs = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, idkrs);
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
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID KRS", "Mahasiswa", "Mata Kuliah", "Semester", "Tahun Ajaran"}, 0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT krs.idkrs, mahasiswa.nama AS mahasiswa, matakuliah.nama AS matakuliah, krs.semester, krs.tahunajaran " +
                     "FROM krs " +
                     "JOIN mahasiswa ON krs.idmhs = mahasiswa.idmhs " +
                     "JOIN matakuliah ON krs.idmk = matakuliah.idmk")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("idkrs"),
                        rs.getString("mahasiswa"),
                        rs.getString("matakuliah"),
                        rs.getString("semester"),
                        rs.getString("tahunajaran")
                });
            }

            table.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormKRS().setVisible(true));
    }
}
