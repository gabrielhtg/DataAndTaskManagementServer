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
        String sql = String.format("INSERT INTO data (notename, notes, username) VALUES ('%s', '%s', '%s')", namaNote, isiNote, username);

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

    public boolean isEmpty (String username) {
        String query = String.format("SELECT notes FROM user WHERE username = '%s'", username);
        PreparedStatement statement;
        try {
            statement = koneksi.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String notes = resultSet.getString("notes");
                if (notes != null && !notes.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public String showNote (String username) {
        StringBuilder temp = new StringBuilder();
        String query = String.format("SELECT notename FROM data WHERE username = '%s'", username);
        PreparedStatement statement;
        try {
            statement = koneksi.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String notename = resultSet.getString("notename");
                temp.append(String.format("  - %s\n", notename));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return temp.toString();
    }

    public boolean removeNote (String username, String notename) {
        String query = String.format("DELETE FROM data WHERE username = '%s' AND notename = '%s'", username, notename);
        PreparedStatement statement;
        try {
            statement = koneksi.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    public String getNote (String username, String notename) {
        String query = String.format("select notes from data where username = '%s' and notename = '%s'", username, notename);
        String note = "";
        PreparedStatement statement;
        try {
            statement = koneksi.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                note = resultSet.getString("notes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return note;
    }
}
