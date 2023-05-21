package net.osandman.server;

import net.osandman.util.PropUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class MultiServer {
    private ServerSocket serverSocket;
    private final List<Socket> clientSockets;
    private final int port;
    private boolean stopRequest = false;

    public MultiServer(int port) {
        this.port = port;
        clientSockets = new ArrayList<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Сервер стартовал: " + serverSocket + " в " + LocalDateTime.now());
            consoleHandler();
            while (!stopRequest) {
                System.out.println("Ожидание клиента ...");
                Socket clientSocket = serverSocket.accept();
                if (!clientSockets.contains(clientSocket)) {
                    clientSockets.add(clientSocket);
                }
                Thread thread = new ClientHandler(clientSocket);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void consoleHandler() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("q - для закрытия");
        new Thread(() -> {
            String input;
            while (scanner.hasNextLine() && !"q".equals(input = scanner.nextLine())) {
                switch (input) {
                    case "c":
                        System.out.println("клиенты: ");
                        clientSockets.forEach(el -> System.out.println(el.toString()));
                        break;
                    case "s":
                        System.out.println(serverSocket);
                        break;
                }
                System.out.println("ввели " + input);
            }
            stopRequest = true;
            stop();
//            System.exit(0);
        }).start();
    }

    public void stop() {
        try {
            for (Socket socket : clientSockets) {
                socket.close();
            }
            serverSocket.close();
            System.out.println("Сервер остановлен");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter printWriter;
        private BufferedReader bufferedReader;
        private String clientName;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientName = bufferedReader.readLine();
                System.out.println("Подключился клиент: " + clientName + ", " + clientSocket);
                String req;
                int bytesCount = 0;
                while ((req = bufferedReader.readLine()) != null && !"q".equals(req)) {
                    System.out.println("Принято от: " + clientName + " = " + req);
                    bytesCount += req.getBytes().length;
                    String resp = "сообщение от " + clientName + ": " + req;
                    for (Socket socket : clientSockets) {
                        if (socket != clientSocket) {
                            new PrintWriter(socket.getOutputStream(), true).println(resp);
                        }
                    }
//                    printWriter.println("сообщение отправлено: " + req);
                }
                clientSockets.remove(clientSocket);
                System.out.println("Клиент отключен: " + clientName + ", " + clientSocket);
                System.out.println("Передано байт: " + bytesCount);
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void close() {
            printWriter.close();
            try {
                bufferedReader.close();
                clientSocket.close();
                System.out.println("Client closed");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Properties prop = PropUtil.getProperties();
        MultiServer multiServer = new MultiServer(Integer.parseInt(prop.getProperty("port")));
        multiServer.start();
//        multiServer.stop();
    }
}
