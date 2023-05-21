package net.osandman;

import net.osandman.client.ClientSocket;
import net.osandman.util.PropUtil;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;


public class RunClient {
    volatile private static boolean isStopped = false;
    volatile private static ClientSocket clientSocket;

    public static void main(String[] args) throws IOException {
        Properties prop = PropUtil.getProperties();
        clientSocket = new ClientSocket(prop.getProperty("host"),
                Integer.parseInt(prop.getProperty("port")), "Charly");
        clientSocket.setConnection();
        clientSocket.sendMessage(clientSocket.getName());
        String req;
        String resp;
        consoleHandler();
        while (!isStopped) {
            if (clientSocket.getBufferedReader() != null
                    && (resp = clientSocket.getBufferedReader().readLine()) != null) {
                System.out.println(resp);
            }
        }
        clientSocket.closeConnection();
    }

    private static void consoleHandler() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("q - для закрытия");
        new Thread(() -> {
            String input;
            while (scanner.hasNextLine()) {
                input = scanner.nextLine();
                clientSocket.getPrintWriter().println(input);
                if ("q".equals(input)) {
                    break;
                }
//                System.out.println("ввели " + input);
            }
            isStopped = true;
        }).start();
    }
}
