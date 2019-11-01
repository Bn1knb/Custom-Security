package com.epam.kamisarau.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcMysqlUtil {
    private static final String HOSTNAME = "localhost";
    private static final String DB_NAME = "blog";
    private static final String USERNAME = "bn1knb";
    private static final String PASSWORD = "";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getMySQLConnection()
            throws ClassNotFoundException, SQLException {
        return getMySQLConnection(HOSTNAME, DB_NAME, USERNAME, PASSWORD, DB_DRIVER);
    }

    public static Connection getMySQLConnection(String hostName, String dbName,
                                                String userName, String password, String driver)
            throws SQLException, ClassNotFoundException {

        Class.forName(driver);
        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
        return DriverManager.getConnection(connectionURL, userName, password);
    }
}
