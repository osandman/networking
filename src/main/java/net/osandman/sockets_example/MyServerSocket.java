package net.osandman.sockets_example;

import net.osandman.util.PropUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class MyServerSocket {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public MyServerSocket(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started: " + serverSocket);
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String req;
            while (!"q".equals(req = bufferedReader.readLine())) {
                if (req == null) {
                    System.out.println("Client disconnected");
                    break;
                }
                String resp = "resp = " + req;
                printWriter.println(resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        printWriter.close();
        try {
            bufferedReader.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("Server closed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
