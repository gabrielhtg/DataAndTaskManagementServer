package gabrielhtg;

import java.io.*;  
import java.net.*;
import java.util.Enumeration;

import gabrielhtg.service.TCPServerServiceImpl;
import gabrielhtg.service.TCPServerThread;

public class TCPServerMultiClient { 
    public static void main(String argv[]) throws Exception { 
        String ipAddress = "";
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while( networkInterfaceEnumeration.hasMoreElements()){
                for ( InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses()) {
                    if ( interfaceAddress.getAddress().isSiteLocalAddress()) {
                        ipAddress = interfaceAddress.getAddress().getHostAddress();
                    }
                }

                if (!ipAddress.equals("")) {
                    break;
                }
            }

            TCPServerServiceImpl.clearScreen();
            System.out.printf("IP Address : %s\n", ipAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try(ServerSocket welcomeSocket = new ServerSocket(5000)) { 
            while(true) { 
                new TCPServerThread(welcomeSocket.accept()).start();
            } 
        } catch (IOException e) { 
            System.err.println("Server tidak bisa menggunakan port " + 5000); 
            System.err.println("Port tersebut kemungkinan sudah digunakan!!"); 
            System.exit(-1); 
        } 
    } 
}
