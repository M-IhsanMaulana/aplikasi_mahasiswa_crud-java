package MataKuliah;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import utils.Koneksi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.*;

public class DataMatkul extends JFrame {
    private JTable tabelMatakuliah;
    private DefaultTableModel model;
    private Koneksi koneksi;

    public DataMatkul () {
        koneksi = new Koneksi();
        initUI();
        loadMataKuliahData();
    }

    private void initUI() {
        // set title
        setTitle("Data Matkul");
        JScrollPane tablePanel = new JScrollPane();
        // Add panel to frame
        add(tablePanel); 

        tabelMatakuliah = new JTable();
        model = new DefaultTableModel(new Object[]{"Kode Matkul", "Nama Matkul"}, 0);
        tabelMatakuliah.setModel(model);
        tablePanel.setViewportView(tabelMatakuliah);
        
        // Panel Button
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // add/tambah
        JButton tambahButton = new JButton("TAMBAH");
        buttonPanel.add(tambahButton);
        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFormMatkul(null);
            }
        });

        // edit
        JButton ubahButton = new JButton("UBAH");
        buttonPanel.add(ubahButton);
        ubahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabelMatakuliah.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Pilih data yang ingin diubah");
                } else {
                    String kodematkul = tabelMatakuliah.getValueAt(selectedRow, 0).toString();
                    showFormMatkul(kodematkul);
                }
            }
        });

        // hapus
        JButton hapusButton = new JButton("HAPUS");
        buttonPanel.add(hapusButton);
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusMataKuliahData();
            }
        });
    }

    private void showFormMatkul(String kodematkul) {
        FormMataKuliah formMataKuliah = new FormMataKuliah(kodematkul);
        formMataKuliah.setVisible(true);
        formMataKuliah.setLocationRelativeTo(this);
        formMataKuliah.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formMataKuliah.setSize(800, 600);
        formMataKuliah.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadMataKuliahData();
            }
        });
    }

    private void loadMataKuliahData() {
        model.setRowCount(0);
        try (
            Connection connection = koneksi.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mata_kuliah")
        ) {
            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getString("kode_matkul"), resultSet.getString("nama_matkul")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hapusMataKuliahData() {
        int selectedRow = tabelMatakuliah.getSelectedRow(); // mendapatkan data yang dipilih dari JTable
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu");
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah anda yakin ingin menghapus data ini?");
            if (confirm == JOptionPane.YES_OPTION) {
                String kodeMatkul = tabelMatakuliah.getValueAt(selectedRow, 0).toString();
                try (
                    Connection connection = koneksi.connect();
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM mata_kuliah WHERE kode_matkul = ?")
                ) {
                    preparedStatement.setString(1, kodeMatkul);
                    preparedStatement.executeUpdate();
                    loadMataKuliahData();
                    JOptionPane.showMessageDialog(this, "Data Berhasil dihapus");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Data batal dihapus");
            }
        }
    }

    public static void main(String[] args) {
        DataMatkul frame = new DataMatkul();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        // set size
        frame.setSize(800, 600);
    }
}
