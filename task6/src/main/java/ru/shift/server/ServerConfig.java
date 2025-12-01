package ru.shift.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {
    private static final String FILE_NAME = "server.properties";
    public static final int DEFAULT_PORT = 8189;

    public static int getPort() {
        Properties props = new Properties();
        try (InputStream is = ServerConfig.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                return DEFAULT_PORT;
            }
            props.load(is);
            return Integer.parseInt(props.getProperty("server.port", "8189"));
        } catch (IOException e) {
            return DEFAULT_PORT;
        }
    }
}