package server.jdbc;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {

    private Connection connection = null;

    private static ConnectionProvider instance = null;

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
            JOptionPane.showMessageDialog(null, "NySQL 드라이버를 찾을 수 없습니다.", "실행 실패", JOptionPane.WARNING_MESSAGE);
            System.exit(-1);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
