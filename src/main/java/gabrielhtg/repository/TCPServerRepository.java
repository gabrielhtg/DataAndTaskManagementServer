package gabrielhtg.repository;


public interface TCPServerRepository {
    void buatKoneksi (String url, String username, String password);
    void tutupKoneksi ();
    boolean cekUsername(String username);
} 
