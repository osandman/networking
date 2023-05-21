package net.osandman.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public final class PropUtil {
    private static Properties prop;
    private static final String PROP_PATH = "D:\\Oleg\\Programming\\Java\\networking\\src\\main\\resources\\app.properties";

    private PropUtil() {

    }

    public static Properties getProperties() {
        if (prop == null) {
            prop = new Properties();
            try {
                prop.load(Files.newBufferedReader(new File(PROP_PATH).toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return prop;
    }
}
