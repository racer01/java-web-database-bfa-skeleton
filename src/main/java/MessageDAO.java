import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

import java.sql.*;

public final class MessageDAO {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE = "bfa";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final int DATABASE_PORT = 13306;

    private static final DBConfigurationBuilder BUILDER;


    static {
        BUILDER = DBConfigurationBuilder
            .newBuilder()
            .setPort(DATABASE_PORT);
    }

    static {
        try {
            Class.forName(DRIVER).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static DB db;

    public static void startDatabase() {
        if (db == null) {
            try {
                db = DB.newEmbeddedDB(BUILDER.build());
                db.start();
                db.source("init.sql");
            } catch (ManagedProcessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static void stopDatabase() {
        try {
            db.stop();
        } catch (ManagedProcessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(BUILDER.getURL(DATABASE) + "?useUnicode=true&characterEncoding=UTF-8", USER, PASSWORD);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getMsg(String lang) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String sql = String.format("SELECT `msg` FROM `hi` WHERE `lang` = '%s'", lang);
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            String msg = resultSet.getString("msg");
            if (resultSet.next()) {
                throw new IllegalStateException();
            }
            return msg;
        }
        throw new IllegalStateException();
    }

    private MessageDAO() {
    }
}
