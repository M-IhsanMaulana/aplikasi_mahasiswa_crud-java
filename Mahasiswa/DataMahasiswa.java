package Mahasiswa;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import utils.Koneksi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.*;

public class DataMahasiswa extends JFrame {
    private JTable tabelMahasiswa;
    private DefaultTableModel model;
    private Koneksi koneksi;

    public DataMahasiswa() {
        koneksi = new Koneksi();
        initUI();
        loadMahasiswaData();
    }

    private void initUI() {
        // Set Title
        setTitle(".::Data Mahasiswa Universitas Budi Luhur::.");
        JScrollPane tablePanel = new JScrollPane();
        // Add panel to frame
        add(tablePanel); 

        tabelMahasiswa = new JTable();
        model = new DefaultTableModel(new Object[]{"NIM", "Nama", "Jenis Kelamin", "Agama", "Hobby1", "Hobby2", "Hobby3", "Alamat", "Telepon"}, 0);
        tabelMahasiswa.setModel(model);
        tablePanel.setViewportView(tabelMahasiswa);


        // Panel Button
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // add/tambah
        JButton tambahButton = new JButton("TAMBAH");
        buttonPanel.add(tambahButton);
        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFormMahasiswa(null);
            }
        });

        // ubah/edit
        JButton ubahButton = new JButton("UBAH");
        buttonPanel.add(ubahButton);
        ubahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabelMahasiswa.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(DataMahasiswa.this, "Pilih data terlebih dahulu");
                } else {
                    String nim = tabelMahasiswa.getValueAt(selectedRow, 0).toString();
                    showFormMahasiswa(nim);
                }
            }
        });

        // hapus
        JButton hapusButton = new JButton("HAPUS");
        buttonPanel.add(hapusButton);
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusMahasiswa();
            }
        });
    }

    // method untuk memanggil formMahasiswa
    private void showFormMahasiswa(String nim) {
        FormMahasiswa formMahasiswa = new FormMahasiswa(nim);
        formMahasiswa.setVisible(true);
        formMahasiswa.setLocationRelativeTo(this);
        formMahasiswa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formMahasiswa.setSize(800, 600);
        formMahasiswa.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadMahasiswaData();
            }
        });
    }

    private void loadMahasiswaData() {
        model.setRowCount(0);
        try (
            Connection connection = koneksi.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mahasiswa")
        ) {
            while (resultSet.next()) {
                String nim = resultSet.getString("nim");
                String nama = resultSet.getString("nama");
                String alamat = resultSet.getString("alamat");
                String telepon = resultSet.getString("telepon");
                String jenisKelamin = resultSet.getString("jenis_kelamin");
                String agama = resultSet.getString("agama");
                String hobby1 = resultSet.getString("hobby_1");
                String hobby2 = resultSet.getString("hobby_2");
                String hobby3 = resultSet.getString("hobby_3");
                model.addRow(new Object[]{nim, nama, jenisKelamin, agama, hobby1, hobby2, hobby3, alamat, telepon});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hapusMahasiswa() {
        // Mendapatkan data yang dipilih dari JTable
        int selectedRow = tabelMahasiswa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu");
        } else {
            // confirm
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah anda yakin ingin menghapus data ini?");
            if (confirm == JOptionPane.YES_OPTION) {
                String nim = tabelMahasiswa.getValueAt(selectedRow, 0).toString();
                try (
                    Connection connection = koneksi.connect();
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM mahasiswa WHERE nim = ?")
                ) {
                    preparedStatement.setString(1, nim);
                    preparedStatement.executeUpdate();
                    loadMahasiswaData();
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
        DataMahasiswa dataMahasiswa = new DataMahasiswa();
        dataMahasiswa.setSize(800, 600);
        dataMahasiswa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dataMahasiswa.setVisible(true);
    }
}
