package Mahasiswa;
import javax.swing.*;

import utils.Koneksi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormMahasiswa extends JFrame {
    private JTextField fieldNim;
    private JTextField fieldNama;
    private JComboBox<String> comboJK;
    private JComboBox<String> comboAgama;
    private JTextField Hobby1;
    private JTextField Hobby2;
    private JTextField Hobby3;
    private JTextField fieldAlamat;
    private JTextField fieldTelepon;
    private String nim;
    private Koneksi koneksi;

    public FormMahasiswa(String nim) {
        koneksi = new Koneksi();
        this.nim = nim;
        initUI();

        if (nim != null) {
            // set title edit
            setTitle(".::Edit Data Form Mahasiswa Universitas Budi Luhur - Nim : " + nim + " ::.");
            loadMahasiswaData(nim);
        } else {
            setTitle(".::Tambah Form Mahasiswa Universitas Budi Luhur::.");
        }
    }

    private void initUI() {

        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        add(panelForm);

        JLabel labelNim = new JLabel("NIM");
        panelForm.add(labelNim);
        fieldNim = new JTextField(10);
        panelForm.add(fieldNim);
        // set bounds
        labelNim.setBounds(20, 20, 100, 20);
        fieldNim.setBounds(120, 20, 200, 20);

        JLabel labelNama = new JLabel("Nama");
        panelForm.add(labelNama);
        fieldNama = new JTextField(10);
        panelForm.add(fieldNama);
        // set bounds
        labelNama.setBounds(20, 50, 100, 20);
        fieldNama.setBounds(120, 50, 200, 20);

        JLabel labelJK = new JLabel("Jenis Kelamin");
        panelForm.add(labelJK);
        String[] jenisKelamin = {"Laki-Laki", "Perempuan"};
        comboJK = new JComboBox<>(jenisKelamin);
        panelForm.add(comboJK);
        // set bounds
        comboJK.setBounds(120, 80, 200, 20);
        labelJK.setBounds(20, 80, 100, 20);

        JLabel labelAgama = new JLabel("Agama");
        panelForm.add(labelAgama);
        String[]agama = {"Islam", "Kristen", "Katolik", "Hindu", "Budha"};
        comboAgama = new JComboBox<>(agama);
        panelForm.add(comboAgama);
        // set bounds
        comboAgama.setBounds(120, 110, 200, 20);
        labelAgama.setBounds(20, 110, 100, 20);

        JLabel labelHobby1 = new JLabel("Hobby1");
        panelForm.add(labelHobby1);
        Hobby1 = new JTextField(10);
        panelForm.add(Hobby1);
        // set bounds
        labelHobby1.setBounds(20, 140, 100, 20);
        Hobby1.setBounds(120, 140, 200, 20);

        JLabel labelHobby2 = new JLabel("Hobby2");
        panelForm.add(labelHobby2);
        Hobby2 = new JTextField(10);
        panelForm.add(Hobby2);
        // set bounds
        labelHobby2.setBounds(20, 170, 100, 20);
        Hobby2.setBounds(120, 170, 200, 20);

        JLabel labelHobby3 = new JLabel("Hobby3");
        panelForm.add(labelHobby3);
        Hobby3 = new JTextField(10);
        panelForm.add(Hobby3);
        // set bounds
        labelHobby3.setBounds(20, 200, 100, 20);
        Hobby3.setBounds(120, 200, 200, 20);

        JLabel labelAlamat = new JLabel("Alamat");
        panelForm.add(labelAlamat);
        fieldAlamat = new JTextField(10);
        panelForm.add(fieldAlamat);
        // set bounds
        labelAlamat.setBounds(20, 230, 100, 20);
        fieldAlamat.setBounds(120, 230, 200, 20);

        JLabel labelTelepon = new JLabel("Telepon");
        panelForm.add(labelTelepon);
        fieldTelepon = new JTextField(10);
        panelForm.add(fieldTelepon);
        // set bounds
        labelTelepon.setBounds(20, 260, 100, 20);
        fieldTelepon.setBounds(120, 260, 200, 20);

        JButton buttonSimpan = new JButton("Simpan");
        panelForm.add(buttonSimpan);
        
        buttonSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nim == null) {
                    tambahDataMahasiswa();
                }  else {
                    editDataMahasiswa();
                }
            }
        });

        // Button clear
        JButton buttonClear = new JButton("Clear");
        panelForm.add(buttonClear);
        // set bounds button simpan dann clear agar sebelah an
        buttonClear.setBounds(250, 300, 100, 20);
        buttonSimpan.setBounds(150, 300, 100, 20);
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
    }

    private void tambahDataMahasiswa() {
        String nim = fieldNim.getText();
        String nama = fieldNama.getText();
        String alamat = fieldAlamat.getText();
        String telepon = fieldTelepon.getText();
        String jenisKelamin = comboJK.getSelectedItem().toString();
        String agama = comboAgama.getSelectedItem().toString();
        String hobby1 = Hobby1.getText();
        String hobby2 = Hobby2.getText();
        String hobby3 = Hobby3.getText();

        if (nim.isEmpty() || nama.isEmpty() || alamat.isEmpty() || telepon.isEmpty() || hobby1.isEmpty() || hobby2.isEmpty() || hobby3.isEmpty() || jenisKelamin.isEmpty() || agama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data harus diisi semua");
        } else if (nimExist(nim)) {
            JOptionPane.showMessageDialog(this, "NIM sudah ada");
        } else {
            try (
                Connection connection = koneksi.connect();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO mahasiswa VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")
            ) {
                preparedStatement.setString(1, nim);
                preparedStatement.setString(2, nama);
                preparedStatement.setString(3, alamat);
                preparedStatement.setString(4, telepon);
                preparedStatement.setString(5, jenisKelamin);
                preparedStatement.setString(6, agama);
                preparedStatement.setString(7, hobby1);
                preparedStatement.setString(8, hobby2);
                preparedStatement.setString(9, hobby3);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data Berhasil ditambahkan");
                clearForm();
                // close form
                dispose();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan");
            }
        }
    }

    private void loadMahasiswaData(String nim) {
        try (
            Connection connection = koneksi.connect();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mahasiswa WHERE nim = ?");
        ) {
            statement.setString(1, nim);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                fieldNim.setText(resultSet.getString("nim"));
                fieldNama.setText(resultSet.getString("nama"));
                fieldAlamat.setText(resultSet.getString("alamat"));
                fieldTelepon.setText(resultSet.getString("telepon"));
                comboJK.setSelectedItem(resultSet.getString("jenis_kelamin"));
                comboAgama.setSelectedItem(resultSet.getString("agama"));
                Hobby1.setText(resultSet.getString("hobby_1"));
                Hobby2.setText(resultSet.getString("hobby_2"));
                Hobby3.setText(resultSet.getString("hobby_3"));
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editDataMahasiswa() {
        String nim = fieldNim.getText();
        String nama = fieldNama.getText();
        String alamat = fieldAlamat.getText();
        String telepon = fieldTelepon.getText();
        String jenisKelamin = comboJK.getSelectedItem().toString();
        String agama = comboAgama.getSelectedItem().toString();
        String hobby1 = Hobby1.getText();
        String hobby2 = Hobby2.getText();
        String hobby3 = Hobby3.getText();

        if (nim.isEmpty() || nama.isEmpty() || alamat.isEmpty() || telepon.isEmpty() || hobby1.isEmpty() || hobby2.isEmpty() || hobby3.isEmpty() ||jenisKelamin.isEmpty() || agama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data harus diisi semua");
        } else {
            try (
                Connection connection = koneksi.connect();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE mahasiswa SET nama = ?, alamat = ?, telepon = ?, jenis_kelamin = ?, agama = ?, hobby_1 = ?, hobby_2 = ?, hobby_3 = ? WHERE nim = ?")
            ) {
                preparedStatement.setString(1, nama);
                preparedStatement.setString(2, alamat);
                preparedStatement.setString(3, telepon);
                preparedStatement.setString(4, jenisKelamin);
                preparedStatement.setString(5, agama);
                preparedStatement.setString(6, hobby1);
                preparedStatement.setString(7, hobby2);
                preparedStatement.setString(8, hobby3);
                preparedStatement.setString(9, nim);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data Berhasil diubah");
                clearForm();
                // close form
                dispose();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan");
            }
        }
    }


    private boolean nimExist(String nim) {
        try (
            Connection connection = koneksi.connect();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mahasiswa WHERE nim = ?");
        ) {
            statement.setString(1, nim);
            return statement.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void clearForm() {
        fieldNim.setText("");
        fieldNama.setText("");
        fieldTelepon.setText("");
        fieldAlamat.setText("");
        comboJK.setSelectedIndex(0);
        comboAgama.setSelectedIndex(0);
        Hobby1.setText("");
        Hobby2.setText("");
        Hobby3.setText("");
        fieldNim.requestFocusInWindow();
    }
}
