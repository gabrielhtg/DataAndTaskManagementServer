package gabrielhtg.repository;

import java.sql.Connection;
import java.sql.DriverManager;
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
    
}
