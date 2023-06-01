package gabrielhtg.service;

public interface TCPServerService {
    String encode(String sentence);
    String decode(String sentence);
    String kirimHelp(String namaClient);
}
