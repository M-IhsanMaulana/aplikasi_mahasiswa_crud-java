package KrsMahasiswa;
import javax.swing.*;

import utils.Koneksi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.UUID;

public class FormKrsMahasiswa extends JFrame {
    private JComboBox<String> comboMahasiswa;
    private JComboBox<String> comboMataKuliah;
    private JTextField fieldNilai;
    private String kode_krs;
    private Koneksi koneksi;

    public FormKrsMahasiswa(String kode_krs) {
        koneksi = new Koneksi();
        this.kode_krs = kode_krs;
        initUI();
        if (kode_krs != null) {
            setTitle(".::Edit Data Form Krs Mata Kuliah Mahasiswa Universitas Budi Luhur - Kode KRS : " + kode_krs + " ::.");
            loadKrsMahasiswaData(kode_krs);
        } else {
            setTitle(".::Tambah Form Krs Mata Kuliah Mahasiswa Universitas Budi Luhur::.");
        }
    }

    private void initUI() {
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        add(panelForm);

        JLabel labelMahasiswa = new JLabel("Mahasiswa");
        panelForm.add(labelMahasiswa);
        comboMahasiswa = new JComboBox<>();
        getMahasiswaData();
        panelForm.add(comboMahasiswa);
        // set bounds
        labelMahasiswa.setBounds(20, 20, 100, 20);
        comboMahasiswa.setBounds(120, 20, 200, 20);

        JLabel labelMataKuliah = new JLabel("Mata Kuliah");
        panelForm.add(labelMataKuliah);
        comboMataKuliah = new JComboBox<>();
        getMataKuliahData();
        panelForm.add(comboMataKuliah);

        // set bounds
        comboMataKuliah.setBounds(120, 50, 200, 20);
        labelMataKuliah.setBounds(20, 50, 100, 20);

        JLabel labelNilai = new JLabel("Nilai");
        panelForm.add(labelNilai);
        fieldNilai = new JTextField(10);
        panelForm.add(fieldNilai);

        // set bounds
        labelNilai.setBounds(20, 80, 100, 20);
        fieldNilai.setBounds(120, 80, 200, 20);


        JButton btnSimpan = new JButton("Simpan");
        panelForm.add(btnSimpan);
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (kode_krs == null) {
                    tambahKrsMahasiswa();
                } else {
                    updateKrsMahasiswa(kode_krs);
                }
            }
        });

        JButton buttonClear = new JButton("Clear");
        panelForm.add(buttonClear);
        // set bounds
        btnSimpan.setBounds(20, 100, 100, 20);
        buttonClear.setBounds(150, 100, 100, 20);
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
    }

    private void getMahasiswaData() {
        try (
            Connection connection = koneksi.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT nim, nama FROM mahasiswa"
            )
        ) {
            while (resultSet.next()) {
                comboMahasiswa.addItem(
                    resultSet.getString("nim") + " - " + resultSet.getString("nama")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMataKuliahData() {
        try (
            Connection connection = koneksi.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT kode_matkul, nama_matkul FROM mata_kuliah"
            )
        ) {
            while (resultSet.next()) {
                comboMataKuliah.addItem(
                    resultSet.getString("kode_matkul") + " - " + resultSet.getString("nama_matkul")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tambahKrsMahasiswa() {
        String nim = comboMahasiswa.getSelectedItem().toString().split(" - ")[0];
        String kodematkul = comboMataKuliah.getSelectedItem().toString().split(" - ")[0];
        int nilai = Integer.parseInt(fieldNilai.getText());

        if (nim.isEmpty() || kodematkul.isEmpty() || nilai < 0) {
            JOptionPane.showMessageDialog(this, "Data harus diisi semua");
        } else {
            try (
                Connection connection = koneksi.connect();
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO krs_mahasiswa (kode_krs, nim, kode_matkul, nilai_matkul) VALUES (?, ?, ?, ?)"
                )
            ) {
                // kode krs random characters
                String kode_krs = UUID.randomUUID().toString().substring(0, 8);
                statement.setString(1, kode_krs);
                statement.setString(2, nim);
                statement.setString(3, kodematkul);
                statement.setInt(4, nilai);


                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan");
                clearForm();
                dispose();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan");
            }
        }
    }

    private void loadKrsMahasiswaData(String kode_krs) {
        String query = "SELECT krs.kode_krs AS 'kode_krs', krs.nim AS 'nim', m.nama AS 'nama', "
                + "krs.kode_matkul AS 'kode_matkul', mk.nama_matkul AS 'matakuliah', "
                + "krs.nilai_matkul AS 'nilai' "
                + "FROM krs_mahasiswa krs "
                + "JOIN mahasiswa m ON krs.nim = m.nim "
                + "JOIN mata_kuliah mk ON krs.kode_matkul = mk.kode_matkul";
        try (
            Connection connection = koneksi.connect();
            
            // gunakan seperti contoh namun dengan kondisi where nim
            PreparedStatement statement = connection.prepareStatement(query + " WHERE krs.kode_krs = ?");
        ) {
            statement.setString(1, kode_krs);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                comboMahasiswa.setSelectedItem(
                    rs.getString("nim") + " - " + rs.getString("nama")
                );
                comboMataKuliah.setSelectedItem(
                    rs.getString("kode_matkul") + " - " + rs.getString("matakuliah")
                );
                fieldNilai.setText(rs.getString("nilai"));

            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateKrsMahasiswa(String kode_krs) {
        String nim = comboMahasiswa.getSelectedItem().toString().split(" - ")[0];
        String kodematkul = comboMataKuliah.getSelectedItem().toString().split(" - ")[0];
        int nilai = Integer.parseInt(fieldNilai.getText());

        if (nim.isEmpty() || kodematkul.isEmpty() || nilai < 0) {
            JOptionPane.showMessageDialog(this, "Data harus diisi semua");
        } else {
            try (
                Connection connection = koneksi.connect();
                PreparedStatement statement = connection.prepareStatement(
                    "UPDATE krs_mahasiswa SET nim = ?, kode_matkul = ?, nilai_matkul = ? WHERE kode_krs = ?"
                )
            ) {
                statement.setString(1, nim);
                statement.setString(2, kodematkul);
                statement.setInt(3, nilai);
                statement.setString(4, kode_krs);

                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate");
                dispose();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan");
            }
        }
    }

    private void clearForm() {
        comboMahasiswa.setSelectedIndex(0);
        comboMataKuliah.setSelectedIndex(0);
        fieldNilai.setText("");
        comboMahasiswa.requestFocusInWindow();
    }
}
