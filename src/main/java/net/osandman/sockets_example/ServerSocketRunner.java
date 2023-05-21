package net.osandman.sockets_example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ServerSocketRunner {
    public static void main(String[] args) throws IOException {
        int port = 1234;
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket clientSocket = serverSocket.accept();
             DataInputStream request = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream response = new DataOutputStream(clientSocket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {
            String req;
            while (!"exit".equals(req = request.readUTF())) {
                System.out.println("Client request: " + req);
                response.writeUTF(scanner.nextLine() + " : " + LocalDateTime.now());
            }
            response.writeUTF("exit");

        }
    }
}
