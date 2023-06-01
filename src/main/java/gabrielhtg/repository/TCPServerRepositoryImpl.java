package gabrielhtg.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TCPServerRepositoryImpl implements TCPServerRepository{
    Connection koneksi;

    @Override
    public void buatKoneksi(String url, String username, String password) {
        try {
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi Database Berhasil");
        } catch (SQLException e) {
            System.out.println("Gagal konek");
        }
    }

    @Override
    public void tutupKoneksi() {
        try {
            koneksi.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
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
    
}
