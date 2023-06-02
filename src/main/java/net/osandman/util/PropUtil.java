package net.osandman.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropUtil {
    private static Properties prop;
    private static final String PROP_FILE = "app.properties";

    private PropUtil() {
    }

    public static Properties getProperties() {
        if (prop == null) {
            try (InputStream resourceAsStream = PropUtil.class.getClassLoader().getResourceAsStream(PROP_FILE)) {
                prop = new Properties();
                prop.load(resourceAsStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return prop;
    }
}
