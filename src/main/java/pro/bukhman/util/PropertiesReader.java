package pro.bukhman.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesReader {

    private static final Logger logger = LogManager.getLogger(PropertiesReader.class);
    private static final PropertiesReader INSTANCE = new PropertiesReader();

    private final Properties properties = new Properties();

    private PropertiesReader() {
        loadProperties();
    }

    public static PropertiesReader getInstance() {
        return INSTANCE;
    }

    private void loadProperties() {
        try (InputStream inputStream = PropertiesReader.class
                .getClassLoader()
                .getResourceAsStream("app.properties")) {

            if (inputStream == null) {
                logger.error("app.properties not found on classpath");
                throw new IllegalStateException("app.properties not found");
            }

            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load app.properties", e);
            throw new RuntimeException("Failed to load app.properties", e);
        }
    }

    public String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            logger.error("Missing property: {}", key);
            throw new IllegalStateException("Missing property: " + key);
        }
        return value;
    }
}