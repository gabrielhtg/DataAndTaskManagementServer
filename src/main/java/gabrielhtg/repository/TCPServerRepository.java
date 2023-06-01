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
        String sql = String.format("SELECT * FROM user WHERE username = '%s'", username);
        try {
            PreparedStatement statement = koneksi.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            String data = null;
            while (resultSet.next()) {
                data = resultSet.getString("username");
                System.out.println(data);
            }

            if (data == null) {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // false jika username tidak ditemukan
        }
        return true; // true ketika username ditemukan

    }
    
    public boolean inputUser (String username, String password) {
        String sql =  String.format("INSERT INTO user (username, password, privatepath) VALUES ('%s', '%s', '%s')", username, password, username + "/");
        PreparedStatement statement;
        try {
            statement = koneksi.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean insertNote (String namaNote, String isiNote, String username) {
        String sql =  String.format("UPDATE user SET notes = CONCAT(notes, '|%s'), notename = CONCAT(notename, '|%s') WHERE username = '%s'", namaNote, isiNote, username);
        PreparedStatement statement;
        try {
            statement = koneksi.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
