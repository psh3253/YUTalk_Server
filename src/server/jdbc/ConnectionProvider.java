package server.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {

    private static ConnectionProvider instance = null;

    private Connection connection = null;

    public static ConnectionProvider getInstance() {
        if (instance == null)
            instance = new ConnectionProvider();
        return instance;
    }

    public void initConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/yutalk?characterEncoding=utf8&serverTimezone=Asia/Seoul";
            String dbUsername = "yutalk";
            String dbPassword = "admin";
            connection = DriverManager.getConnection(url, dbUsername, dbPassword);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("MySQL 데이터베이스에 접근을 할 수 없습니다.");
            System.exit(-1);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
