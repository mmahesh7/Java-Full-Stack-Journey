package Day_27.library_management_system.src.main.java.com.library.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static Properties properties = new Properties();
    private static boolean loaded = false;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try(InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if(input != null) {
                properties.load(input);
                loaded = true;
                System.out.println("Configuration loaded successfully");
            } else {
                System.err.println("config.properties not found, using defaults");
                setDefaults();
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            setDefaults();
        }
    }

    private static void setDefaults() {
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/library_db");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "");
        properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("pool.min.size", "5");
        properties.setProperty("pool.max.size", "20");
        loaded = true;
    }

    public static String getDatabaseUrl() {
        return properties.getProperty("db.url");
    }

    public static String getDatabaseUsername() {
        return properties.getProperty("db.username");
    }

    public static String getDatabasePassword() {
        return properties.getProperty("db.password");
    }

    public static String getDatabaseDriver() {
        return properties.getProperty("db.driver");
    }

    public static int getMinPoolSize() {
        return Integer.parseInt(properties.getProperty("pool.min.size", "5"));
    }

    public static int getMaxPoolSize() {
        return Integer.parseInt(properties.getProperty("pool.max.size", "20"));
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static void printConfiguration() {
        System.out.println("=== Database Configuration ===");
        System.out.println("URL: " + getDatabaseUrl());
        System.out.println("Username: " + getDatabaseUsername());
        System.out.println("Password: " + (getDatabasePassword().isEmpty() ? "(empty)" : "***"));
        System.out.println("Pool Size: " + getMinPoolSize() + "-" + getMaxPoolSize());
        System.out.println("==============================");
    }

}
