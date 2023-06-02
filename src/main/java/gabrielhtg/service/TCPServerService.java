package gabrielhtg.service;

import java.io.IOException;
import java.util.Base64;

public class TCPServerService{
    public String encode(String sentence) {
        byte[] stringInBytes = sentence.getBytes();
        return Base64.getEncoder().encodeToString(stringInBytes) + "\n";
    }

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

    public String kirimHelp(String namaClient) {
        return "/help untuk " + namaClient + "\n" +
                    "  - /notes --> menampilkan notes\n" + 
                    "  - /help --> menampilkan help\n" + 
                    "  - /save --> menyimpan note baru\n" + 
                    "  - /remove --> menghapus note\n" + 
                    "  - /exit --> exit\n" +
                    "  - untuk mendapatkan note ketikkan #notename" ;
    }

    public void buatGaris (int n) {
        for (int i = 0; i < n; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
        
    public void buatGaris () {
        System.out.println("-------------------------------------------");
    }

    // public void getFile (Socket socket, String namaFile) {
    //     String savePath = "public/" + namaFile;

    //     try {
    //         InputStream inputStream = socket.getInputStream();
    //         FileOutputStream fileOutputStream = new FileOutputStream(savePath);
    //         // Terima data file ke dalam byte array
    //         byte[] buffer = new byte[8192];
    //         int bytesRead;
    //         while ((bytesRead = inputStream.read(buffer)) != -1) {
    //             // Tulis byte array ke file
    //             fileOutputStream.write(buffer, 0, bytesRead);
    //         }
    //         fileOutputStream.flush();
    //         System.out.println("File berhasil diterima dan disimpan di: " + savePath);
    //         fileOutputStream.close();

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}
