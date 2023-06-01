package gabrielhtg.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TCPServerRepository{
    Connection koneksi;

    public void buatKoneksi(String url, String username, String password) {
        try {
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi Database Berhasil");
        } catch (SQLException e) {
            System.out.println("Gagal konek");
        }
    }

    public void tutupKoneksi() {
        try {
            koneksi.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean cekUsername(String username) {
        String sql = String.format("SELECT * FROM nama_tabel WHERE kolom LIKE %s;", username);
        try {
            PreparedStatement statement = koneksi.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            String data = null;
            while (resultSet.next()) {
                data = resultSet.getString("kolom");
                System.out.println(data);
            }

        } catch (SQLException e) {
            return false; // false jika username tidak ditemukan
        }
        return true; // true ketika username ditemukan

    }
    
    public boolean inputUser (String username, String password) {
        String sql =  String.format("user (username, password, privatepath) VALUES (%s, %s, %s)", username, password, username + "/");
        PreparedStatement statement;
        try {
            statement = koneksi.prepareStatement(sql);
        } catch (SQLException e) {
            return false;
        }

        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }
}
