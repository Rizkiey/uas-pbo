/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Projek;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class FormMatakuliah extends JFrame {
    private JTable table;
    private JTextField txtNamaMK;
    private JButton btnTambah, btnHapus, btnRefresh;

    public FormMatakuliah() {
        setTitle("CRUD Data Mata Kuliah");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblNamaMK = new JLabel("Nama Mata Kuliah:");
        lblNamaMK.setBounds(20, 20, 150, 25);
        add(lblNamaMK);

        txtNamaMK = new JTextField();
        txtNamaMK.setBounds(180, 20, 150, 25);
        add(txtNamaMK);

        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 60, 100, 25);
        add(btnTambah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(130, 60, 100, 25);
        add(btnHapus);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(240, 60, 100, 25);
        add(btnRefresh);

        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 100, 550, 200);
        add(sp);

        // Event Handlers
        btnTambah.addActionListener(e -> tambahData());
        btnHapus.addActionListener(e -> hapusData());
        btnRefresh.addActionListener(e -> loadData());

        loadData();
    }

    private void tambahData() {
        String namaMK = txtNamaMK.getText();

        if (!namaMK.isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO matakuliah (nama) VALUES (?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, namaMK);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nama mata kuliah tidak boleh kosong!");
        }
    }

    private void hapusData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int idmk = (int) table.getValueAt(selectedRow, 0);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM matakuliah WHERE idmk = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, idmk);
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
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nama Mata Kuliah"}, 0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM matakuliah")) {

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("idmk"), rs.getString("nama")});
            }

            table.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormMatakuliah().setVisible(true));
    }
}
