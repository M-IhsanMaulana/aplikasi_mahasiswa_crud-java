package KrsMahasiswa;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import utils.Koneksi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.*;

public class DataKrsMahasiswa extends JFrame {
    private JTable tabelKrsMahasiswa;
    private DefaultTableModel model;
    private Koneksi koneksi;

    public DataKrsMahasiswa() {
        koneksi = new Koneksi();
        initUI();
        loadKrsMahasiswaData();
    }

    private void initUI() {
        setTitle(".::Data KRS Mahasiswa Universitas Budi Luhur::.");
        JScrollPane tablePanel = new JScrollPane();
        // Add panel to frame
        add(tablePanel);

        tabelKrsMahasiswa = new JTable();
        model = new DefaultTableModel(new Object[] { "Kode KRS",  "NIM", "Nama", "Kode Matkul", "Nama Matkul", "Nilai Matkul" }, 0);
        tabelKrsMahasiswa.setModel(model);
        tablePanel.setViewportView(tabelKrsMahasiswa);

        // Panel Button
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // add/tambah
        JButton tambahButton = new JButton("TAMBAH");
        buttonPanel.add(tambahButton);
        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFormKrsMahasiswa(null);
            }
        });

        // ubah/edit
        JButton ubahButton = new JButton("UBAH");
        buttonPanel.add(ubahButton);
        ubahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabelKrsMahasiswa.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Pilih data yang akan diubah");
                } else {
                    String nim = tabelKrsMahasiswa.getValueAt(selectedRow, 0).toString();
                    showFormKrsMahasiswa(nim);
                }
            }
        });

        // hapus
        JButton hapusButton = new JButton("HAPUS");
        buttonPanel.add(hapusButton);
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusKrsMahasiswaData();
            }
        });
    }

    private void showFormKrsMahasiswa(String nim) {
        FormKrsMahasiswa formKrs = new FormKrsMahasiswa(nim);
        formKrs.setVisible(true);
        formKrs.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formKrs.setSize(800, 600);
        formKrs.setLocationRelativeTo(this);
        formKrs.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadKrsMahasiswaData();
            }
        });
    }

    private void loadKrsMahasiswaData() {
        model.setRowCount(0);
        String query = "SELECT krs.kode_krs AS 'Kode KRS', krs.nim AS 'NIM Mahasiswa', m.nama AS 'Nama Mahasiswa', "
                + "krs.kode_matkul AS 'Kode Mata Kuliah', mk.nama_matkul AS 'Nama Mata Kuliah', "
                + "krs.nilai_matkul AS 'Nilai' "
                + "FROM krs_mahasiswa krs "
                + "JOIN mahasiswa m ON krs.nim = m.nim "
                + "JOIN mata_kuliah mk ON krs.kode_matkul = mk.kode_matkul";
        try (
                Connection connection = koneksi.connect();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);) {
            while (resultSet.next()) {
                String kodeKrs = resultSet.getString("Kode KRS");
                String nim = resultSet.getString("NIM Mahasiswa");
                String nama = resultSet.getString("Nama Mahasiswa");
                String kodeMatkul = resultSet.getString("Kode Mata Kuliah");
                String matkul = resultSet.getString("Nama Mata Kuliah");
                String nilai = resultSet.getString("Nilai");
                model.addRow(new Object[] { kodeKrs, nim, nama, kodeMatkul, matkul, nilai });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hapusKrsMahasiswaData() {
        int selectedRow = tabelKrsMahasiswa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu");
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?");
            if (confirm == JOptionPane.YES_OPTION) {
                String kode_krs = tabelKrsMahasiswa.getValueAt(selectedRow, 0).toString();
                try (
                        Connection connection = koneksi.connect();
                        PreparedStatement preparedStatement = connection
                                .prepareStatement("DELETE FROM krs_mahasiswa WHERE kode_krs = ?")) {
                    preparedStatement.setString(1, kode_krs);
                    preparedStatement.executeUpdate();
                    loadKrsMahasiswaData();
                    JOptionPane.showMessageDialog(this, "Data Berhasil dihapus");
                } catch (Exception e) {

                }
            } else {
                JOptionPane.showMessageDialog(this, "Data batal dihapus");
            }
        }
    }

    public static void main(String[] args) {
        DataKrsMahasiswa frame = new DataKrsMahasiswa();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        // set size
        frame.setSize(800, 600);
    }
}
