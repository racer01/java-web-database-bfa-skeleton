package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionUtil {

    public enum DatabaseName {

        TEST,
        BFA
    }

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_HOST = "localhost";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";
    private static final int DATABASE_PORT = 3306;

    static {
        try {
            Class.forName(DRIVER).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getUrl(DatabaseName databaseName) {
        return String.format(
            "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=%s&serverTimezone=%s",
            DATABASE_HOST,
            DATABASE_PORT,
            databaseName.name().toLowerCase(),
            "UTF-8",
            "Europe/Budapest");
    }

    public static Connection getConnection(DatabaseName databaseName) {
        try {
            return DriverManager.getConnection(getUrl(databaseName), DATABASE_USER, DATABASE_PASSWORD);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private ConnectionUtil() {
    }
}
