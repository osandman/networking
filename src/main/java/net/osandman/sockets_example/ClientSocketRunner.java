package net.osandman.sockets_example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ClientSocketRunner {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
//        String request = """
//                GET /music_bands/band?name=bodom HTTP/1.1
//                host: %s
//                user-agent: Mozilla/5.0
//                accept: application/json
//
//                """.formatted(hostName);
        int port = 1234;
        InetAddress[] address = Inet4Address.getAllByName(hostName);
        Arrays.stream(address).forEach(System.out::println);
        try (Socket clientSocket = new Socket(hostName, port);
             DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
             DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {
            String resp = "";
            while (scanner.hasNextLine()) {
                String req = scanner.nextLine();
                outputStream.writeUTF(req);
                if (!"exit".equals(resp = inputStream.readUTF())) {
                    System.out.println("Server response: " + resp);
                } else {
                    System.out.println("Server closed ...");
                    break;
                }

            }
//            Print.printResponse(inputStream);
        }
    }
}
