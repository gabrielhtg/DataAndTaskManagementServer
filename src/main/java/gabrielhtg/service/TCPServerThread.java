package gabrielhtg.service;
import java.net.*;

import com.github.tomaslanger.chalk.Chalk;

import gabrielhtg.repository.TCPServerRepository;
import gabrielhtg.repository.TCPServerRepositoryImpl;

import java.io.*;

public class TCPServerThread extends Thread {
    private Socket connectionSocket = null;

    public TCPServerThread(Socket socket) {
        super("TCPServerThread");
        this.connectionSocket = socket;
    }

    public void run(){
        String clientSentence;
        String outputSentence;
        TCPServerRepository repo = new TCPServerRepositoryImpl();
        TCPServerServiceImpl service = new TCPServerServiceImpl();
        repo.buatKoneksi("jdbc:mysql://127.0.0.1:3306", "root", "agustus163");

        try{
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            String namaClient = service.decode(inFromClient.readLine());
            System.out.println("Client " + namaClient + " mencoba terhubung.");

            
            while((clientSentence = inFromClient.readLine()) != null){
                if (clientSentence.equals("/notes")) {
                    outputSentence = String.format("Berikut ini adalah list note yang tersedia untuk %s", namaClient);
                    outToClient.writeBytes(service.encode(outputSentence));
                }

                else if (clientSentence.equals("/exit")) {
                    outputSentence = String.format("Koneksi kamu dengan server selesai.");
                    outToClient.writeBytes(service.encode(outputSentence));
                    System.out.printf("%s disconnected\n", namaClient);
                    break;
                }

                else if (clientSentence.equals("/help")) {
                    outputSentence = String.format("/help untuk %s\n  - /notes --> untuk menampilkan notes\n  - /exit --> untuk exit", namaClient);

                }

                else {
                    outputSentence = String.format("Input kamu tidak tepat.\n/help untuk %s\n  - /notes --> untuk menampilkan notes\n  - /exit --> untuk exit", namaClient);
                }

                System.out.println("dari " + namaClient + " : " + clientSentence);
                outToClient.writeBytes(service.encode(outputSentence));
            }

            connectionSocket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}


