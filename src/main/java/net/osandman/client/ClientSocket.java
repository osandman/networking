package net.osandman.client;

import java.io.*;
import java.net.Socket;

public class ClientSocket {

    private Socket clientSocket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private final String host;
    private final int port;
    private String name;

    public ClientSocket(String host, int port, String name) {
        this.host = host;
        this.port = port;
        this.name = name;
    }

    public void setConnection() {
        try {
            clientSocket = new Socket(host, port);
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println(name + " подключился к серверу: " + clientSocket.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendMessage(String message) {
        StringBuilder resp = new StringBuilder();
        if (printWriter != null) {
            printWriter.println(message);
            try {
                while (bufferedReader.ready()) {
                    resp.append(bufferedReader.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resp.toString();
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void closeConnection() {
        try {
            bufferedReader.close();
            printWriter.close();
            clientSocket.close();
            System.out.println("Клиент закрыт");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}