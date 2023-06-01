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
}
