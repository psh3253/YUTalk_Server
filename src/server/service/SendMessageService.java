package server.service;

import server.dao.MessageDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class SendMessageService {

    private static SendMessageService instance = null;

    public static SendMessageService getInstance() {
        if(instance == null)
            instance = new SendMessageService();
        return instance;
    }

    public void sendMessage(String userId, int roomId, String message) {
        Connection connection = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);
            MessageDao.getInstance().insertMessage(connection, userId, roomId, "text", message);
            connection.commit();
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
