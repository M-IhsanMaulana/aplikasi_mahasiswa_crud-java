package MataKuliah;
import javax.swing.*;

import utils.Koneksi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormMataKuliah extends JFrame {
    private JTextField fieldKode;
    private JTextField fieldMatakuliah;

    private String kodematkul;
    private Koneksi koneksi;

    public FormMataKuliah(String kodematkul) {
        koneksi = new Koneksi();
        this.kodematkul = kodematkul;
        initUI();
        if (kodematkul != null) {
            setTitle(".::Edit Data Form Mata Kuliah Universitas Budi Luhur - Kode : " + kodematkul + " ::.");
            loadMataKuliahData(kodematkul);
        } else {
            setTitle(".::Tambah Form Mata Kuliah Universitas Budi Luhur::.");
        }
    }

    private void initUI() {
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        add(panelForm);

        JLabel labelKode = new JLabel("Kode Matkul");
        panelForm.add(labelKode);
        fieldKode = new JTextField(10);
        panelForm.add(fieldKode);
        // set bounds
        labelKode.setBounds(20, 20, 100, 20);
        fieldKode.setBounds(120, 20, 200, 20);

        JLabel labelMatakuliah = new JLabel("Mata Kuliah");
        panelForm.add(labelMatakuliah);
        fieldMatakuliah = new JTextField(10);
        panelForm.add(fieldMatakuliah);

        // set bounds
        fieldMatakuliah.setBounds(120, 50, 200, 20);
        labelMatakuliah.setBounds(20, 50, 100, 20);

        JButton btnSimpan = new JButton("Simpan");
        panelForm.add(btnSimpan);
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (kodematkul == null) {
                    tambahMatakuliah();
                } else {
                    updateMatakuliah();
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

    private void tambahMatakuliah() {
        String kodematkul = fieldKode.getText();
        String matakuliah = fieldMatakuliah.getText();

        if (kodematkul.isEmpty() || matakuliah.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data harus diisi semua");
        } else if (matakuliahExist(kodematkul)) {
            JOptionPane.showMessageDialog(this, "Matakuliah sudah ada");
        } else {
            try (
                Connection connection = koneksi.connect();
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO mata_kuliah VALUES (?, ?)"
                )
            ) {
                statement.setString(1, kodematkul);
                statement.setString(2, matakuliah);

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

    private void loadMataKuliahData(String kodematkul) {
        try (
            Connection connection = koneksi.connect();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM mata_kuliah WHERE kode_matkul = ?"
            )
        ) {
            statement.setString(1, kodematkul);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                fieldKode.setText(rs.getString("kode_matkul"));
                fieldMatakuliah.setText(rs.getString("nama_matkul"));
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan");
        }
    }

    private void updateMatakuliah() {
        String kodematkul = fieldKode.getText();
        String matakuliah = fieldMatakuliah.getText();

        if (kodematkul.isEmpty() || matakuliah.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data harus diisi semua");
        } else {
            try (
                Connection connection = koneksi.connect();
                PreparedStatement statement = connection.prepareStatement(
                    "UPDATE mata_kuliah SET nama_matkul = ? WHERE kode_matkul = ?"
                )
            ) {
                statement.setString(1, matakuliah);
                statement.setString(2, kodematkul);

                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate");
                clearForm();
                dispose();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Data gagal diupdate");
            }
        }
    }

    private boolean matakuliahExist(String kodematkul) {
        try (
            Connection connection = koneksi.connect();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mata_kuliah WHERE kode_matkul = ?");
        ) {
            statement.setString(1, kodematkul);
            return statement.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private void clearForm() {
        fieldKode.setText("");
        fieldMatakuliah.setText("");
        fieldKode.requestFocusInWindow();
    }
}
