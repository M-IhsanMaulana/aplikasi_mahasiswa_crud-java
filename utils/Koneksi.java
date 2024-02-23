package utils;
import java.sql.*;
public class Koneksi {
    public Koneksi() {

    }
    
    private final String JDBC_URL = "com.mysql.cj.jdbc.Driver";
    private final String DB_NAME = "2311510487";
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String JDBC_URL_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME;
    Connection conn;

    public Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_URL);
        conn = DriverManager.getConnection(JDBC_URL_STRING, USERNAME, PASSWORD);
        System.out.println("Koneksi Berhasil");
        return conn;
    }
}
