package server.service;

import server.dao.MemberDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class SetStatusMessageService {

    private static SetStatusMessageService instance = null;

    public static SetStatusMessageService getInstance() {
        if(instance == null)
            instance = new SetStatusMessageService();
        return instance;
    }

    public void setStatusMessage(String userId, String statusMessage) {
        Connection connection = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);
            MemberDao.getInstance().updateStatusMessage(connection, userId, statusMessage);
            connection.commit();
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
