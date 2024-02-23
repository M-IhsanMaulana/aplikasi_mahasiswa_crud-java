import javax.swing.*;

import KrsMahasiswa.DataKrsMahasiswa;
import Mahasiswa.DataMahasiswa;
import MataKuliah.DataMatkul;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Main extends JFrame {
    public Main() {
        initUI();
    }

    private void initUI() {
        // set title
        setTitle(".:: Aplikasi Universitas Budi Luhur ::.");

        // PANEL
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());

        // Panel Logo
        JPanel panelLogo = new JPanel();
        ImageIcon logo = new ImageIcon("assets/logo-budiluhur.png");
        Image logoScalled = logo.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledLogo = new ImageIcon(logoScalled);
        JLabel lblLogo = new JLabel(scaledLogo);
        panelLogo.add(lblLogo);

        // Panel tombol
        JPanel panelTombol = new JPanel();
        JButton btnMahasiswa = new JButton("Data Mahasiswa");

        btnMahasiswa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                DataMahasiswa dataMahasiswa = new DataMahasiswa();
                dataMahasiswa.setSize(800, 600);
                dataMahasiswa.setVisible(true);
                dataMahasiswa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                dataMahasiswa.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        setVisible(true);
                    }
                });
            }
        });

        JButton btnMatakuliah = new JButton("Data Matakuliah");
        btnMatakuliah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                DataMatkul dataMatkul = new DataMatkul();
                dataMatkul.setSize(800, 600);
                dataMatkul.setVisible(true);
                dataMatkul.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                dataMatkul.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        setVisible(true);
                    }
                });
            }
        });

        JButton btnKrsMahasiswa = new JButton("Data KRS Mahasiswa");
        btnKrsMahasiswa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                DataKrsMahasiswa dataKrsMahasiswa = new DataKrsMahasiswa();
                dataKrsMahasiswa.setSize(800, 600);
                dataKrsMahasiswa.setVisible(true);
                dataKrsMahasiswa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                dataKrsMahasiswa.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        setVisible(true);
                    }
                });
            }
        });

        panelTombol.add(btnMahasiswa);
        panelTombol.add(btnMatakuliah);
        panelTombol.add(btnKrsMahasiswa);

        panelMain.add(panelLogo, BorderLayout.NORTH);
        panelMain.add(panelTombol, BorderLayout.CENTER);


        // add panel
        add(panelMain);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setSize(800, 600);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }
}
