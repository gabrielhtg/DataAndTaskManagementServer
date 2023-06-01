package gabrielhtg.service;
import java.net.*;
import java.util.Scanner;

import gabrielhtg.repository.TCPServerRepository;

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
        Scanner scan = new Scanner(System.in);
        TCPServerRepository repo = new TCPServerRepository();
        TCPServerServiceImpl service = new TCPServerServiceImpl();
        repo.buatKoneksi("jdbc:mysql://127.0.0.1:3306/userdata", "root", "agustus163");

        try{
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            String namaClient = service.decode(inFromClient.readLine());
            System.out.println("Client " + namaClient + " mencoba terhubung.");
            
            // memeriksa apakah user ada di database atau tidak
            if (repo.cekUsername(namaClient) == false) {
                System.out.printf("User %s tidak ditemukan\n", namaClient);
                System.out.flush();
                outToClient.writeBytes(service.encode("false"));
                String newPassword = service.decode(inFromClient.readLine()).trim();

                if (repo.inputUser(namaClient, newPassword)) {
                    outToClient.writeBytes(service.encode("true"));
                }
                
                else {
                    outToClient.writeBytes(service.encode("false"));
                }
            }

            else {
                outToClient.writeBytes(service.encode("true"));
                String passwordDatabase = repo.getPassword(namaClient);
                String passwordDariClient = "";
                // passwordDariClient = service.decode(inFromClient.readLine());
                // outToClient.writeBytes(service.encode("false"));
                
                while (true) {
                    passwordDariClient = service.decode(inFromClient.readLine());
                    if (passwordDatabase.equals(passwordDariClient)) {
                        outToClient.writeBytes(service.encode("true"));
                        break;
                    }
                    outToClient.writeBytes(service.encode("false"));
                }
            }

            while((clientSentence = inFromClient.readLine()) != null){
                if (clientSentence.equals("/notes")) {
                    outputSentence = String.format("Berikut ini adalah list note yang tersedia untuk %s\n  %s", namaClient, repo.showNote(namaClient).trim());
                }

                else if (clientSentence.equals("/exit")) {
                    outputSentence = String.format("Koneksi kamu dengan server selesai.");
                    System.out.printf("%s disconnected\n", namaClient);
                    outToClient.writeBytes(service.encode(outputSentence));
                    outToClient.flush();
                    repo.tutupKoneksi();
                    break;
                }

                else if (clientSentence.charAt(0) == '#') {
                    int panjang = repo.getNote(namaClient, clientSentence.split("#")[1]).length();
                    
                    if (panjang != 0) {
                        outputSentence = repo.getNote(namaClient, clientSentence.split("#")[1]).substring(0, panjang - 5);
                    }

                    else {
                        outputSentence = String.format("Note %s tidak ditemukan.", clientSentence.split("#")[1]);
                    }
                }

                else if (clientSentence.equals("/save")) {
                    String namaNote = service.decode(inFromClient.readLine());
                    String isiNote = service.decode(inFromClient.readLine());

                    if (repo.insertNote(namaNote, isiNote, namaClient)) {
                        outputSentence = "Berhasil menambahkan note " + namaNote;
                    }

                    else {
                        outputSentence = "Gagal menambah note. Note " + namaNote + " sudah ada.";
                    }
                }

                else if (clientSentence.equals("/remove")) {
                    String namaNote = service.decode(inFromClient.readLine());

                    if (repo.removeNote(namaClient, namaNote)) {
                        outputSentence = "Berhasil menghapus note " + namaNote;
                    }

                    else {
                        outputSentence = "Gagal menghapus note. Note " + namaNote + " tidak ditemukan.";
                    }
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

        scan.close();
    }
}


