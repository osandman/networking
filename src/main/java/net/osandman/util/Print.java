package net.osandman.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Print {
    public static void printResponse(DataInputStream inputStream) {
        new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines().forEach(System.out::println);
    }
}
