package net.osandman.sockets_example;

import net.osandman.sockets_example.MyServerSocket;
import net.osandman.util.PropUtil;

import java.util.Properties;

public class RunServer {


    public static void main(String[] args) {
        Properties prop = PropUtil.getProperties();
        MyServerSocket myServerSocket = new MyServerSocket(Integer.parseInt(prop.getProperty("port")));
        myServerSocket.startServer();
        myServerSocket.close();
    }

}
