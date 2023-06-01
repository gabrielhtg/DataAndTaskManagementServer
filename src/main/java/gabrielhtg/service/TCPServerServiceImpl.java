package gabrielhtg.service;

import java.io.IOException;
import java.util.Base64;

public class TCPServerServiceImpl implements TCPServerService{
    @Override
    public String encode(String sentence) {
        byte[] stringInBytes = sentence.getBytes();
        return Base64.getEncoder().encodeToString(stringInBytes) + "\n";
    }

    @Override
    public String decode(String sentence) {
        byte[] stringInBytes = Base64.getDecoder().decode(sentence);
        return new String(stringInBytes);
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {}
    }

    @Override
    public String kirimHelp(String namaClient) {
        return "/help untuk " + namaClient + "\n" +
                    "  - /notes --> menampilkan notes\n" + 
                    "  - /help --> menampilkan help\n" + 
                    "  - /save --> menyimpan note baru\n" + 
                    "  - /remove --> menghapus note\n" + 
                    "  - /exit --> exit\n" +
                    "  - untuk mendapatkan note ketikkan #notename" ;
    }

    @Override
    public void buatGaris (int n) {
        for (int i = 0; i < n; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
        
    @Override
    public void buatGaris () {
        System.out.println("-------------------------------------------");
    }

    
}
