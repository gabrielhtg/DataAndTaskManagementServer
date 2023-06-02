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
        TCPServerService service = new TCPServerService();
        repo.buatKoneksi("jdbc:mysql://127.0.0.1:3306/userdata", "root", "agustus163");
        BufferedReader inFromClient = null;
        try {
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataOutputStream outToClient = null;
        try {
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String namaClient = null;
        try {
            namaClient = service.decode(inFromClient.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            System.out.println("Client " + namaClient + " mencoba terhubung.");
            
            // memeriksa apakah user ada di database atau tidak
            if (repo.cekUsername(namaClient) == false) {
                System.out.printf("User %s tidak ditemukan\n", namaClient);
                System.out.flush();
                outToClient.writeBytes(service.encode("false"));
                String newPassword = service.decode(inFromClient.readLine()).trim();

                // cek apakah username dan password itu tersedia
                if (repo.inputUser(namaClient, newPassword)) {
                    outToClient.writeBytes(service.encode("true"));
                }
                
                else {
                    outToClient.writeBytes(service.encode("false"));
                }
            }

            // situasi ketika username sudah pernah mendaftar sebelumnya
            else {
                outToClient.writeBytes(service.encode("true"));
                String passwordDatabase = repo.getPassword(namaClient);
                String passwordDariClient = "";
                
                while (true) {
                    passwordDariClient = service.decode(inFromClient.readLine());
                    if (passwordDatabase.equals(passwordDariClient)) {
                        outToClient.writeBytes(service.encode("true"));
                        break;
                    }
                    outToClient.writeBytes(service.encode("false"));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            while((clientSentence = inFromClient.readLine()) != null){
                System.out.println(clientSentence);
                if (clientSentence.equals("/notes")) {
                    outputSentence = String.format("Berikut ini adalah list note yang tersedia untuk %s\n  %s", namaClient, repo.showNote(namaClient).trim());
                }

                else if (clientSentence.equals("/exit")) {
                    outputSentence = String.format("Koneksi kamu dengan server selesai.");
                    System.out.printf("%s disconnected\n", namaClient);
                    try {
                        outToClient.writeBytes(service.encode(outputSentence));
                        outToClient.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    String namaNote = "";
                    try {
                        namaNote = service.decode(inFromClient.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String isiNote = "";
                    try {
                        isiNote = service.decode(inFromClient.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (repo.insertNote(namaNote, isiNote, namaClient)) {
                        outputSentence = "Berhasil menambahkan note " + namaNote;
                    }

                    else {
                        outputSentence = "Gagal menambah note. Note " + namaNote + " sudah ada.";
                    }
                }

                // // bagian ini ketika user mencoba untuk mengirimkan file
                // else if (clientSentence.equals("/send")) {
                //     String namaFile = "";
                //     try {
                //         namaFile = service.decode(inFromClient.readLine());
                //     } catch (IOException e) {
                //         e.printStackTrace();
                //     } 
                //     service.getFile(connectionSocket, namaFile);
                //     clientSentence = "";
                //     continue;
                //     // outputSentence = "true";
                // }

                else if (clientSentence.equals("/remove")) {
                    String namaNote = "";
                    try {
                        namaNote = service.decode(inFromClient.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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

                try {
                    outToClient.writeBytes(service.encode(outputSentence));
                } catch (SocketException e) {
                    continue;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scan.close();
    }
}


