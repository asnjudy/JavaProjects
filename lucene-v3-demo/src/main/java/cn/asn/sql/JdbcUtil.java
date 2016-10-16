package cn.asn.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {

    private static final String URL = "jdbc:mysql://127.0.0.1/project?autoReconnect=true&characterEncoding=utf8";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";
    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
