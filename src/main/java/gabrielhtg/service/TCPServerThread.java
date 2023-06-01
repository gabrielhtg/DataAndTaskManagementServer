package gabrielhtg.service;
import java.net.*;

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
        repo.buatKoneksi("jdbc:mysql://127.0.0.1:3306/userdata", "root", "agustus163");

        try{
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            String namaClient = service.decode(inFromClient.readLine());
            
            // memeriksa apakah user ada di database atau tidak
            if (repo.cekUsername(namaClient) == false) {
                System.out.println("User tidak ditemukan");
            }

            System.out.println("Client " + namaClient + " mencoba terhubung.");
            
            while((clientSentence = inFromClient.readLine()) != null){
                if (clientSentence.equals("/notes")) {
                    outputSentence = String.format("Berikut ini adalah list note yang tersedia untuk %s", namaClient);
                }

                else if (clientSentence.equals("/exit")) {
                    outputSentence = String.format("Koneksi kamu dengan server selesai.");
                    System.out.printf("%s disconnected\n", namaClient);
                    outToClient.writeBytes(service.encode(outputSentence));
                    outToClient.flush();
                    repo.tutupKoneksi();
                    break;
                }

                else if (clientSentence.equals("/help")) {
                    outputSentence = service.kirimHelp(namaClient);
                }

                else {
                    outputSentence = String.format("Input kamu tidak tepat. Ketikkan /help", service.kirimHelp(namaClient));
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


