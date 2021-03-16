package server.service;

import server.dao.MemberDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.Member;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginService {

    private static final int LOGIN_SUCCESS = 0;

    private static final int NOT_EXIST_ID = 1;

    private static final int NOT_MATCH_PASSWORD = 2;

    private static LoginService instance = null;

    public static LoginService getInstance() {
        if (instance == null)
            instance = new LoginService();
        return instance;
    }

    public int login(String userId, String password) {
        Connection connection = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);

            Member member = MemberDao.getInstance().selectByUserId(connection, userId);
            if (member == null) {
                JdbcUtil.getInstance().rollback(connection);
                return NOT_EXIST_ID;
            } else if (!password.equals(member.getPassword())) {
                JdbcUtil.getInstance().rollback(connection);
                return NOT_MATCH_PASSWORD;
            }

            MemberDao.getInstance().updateLogin(connection, userId);
            connection.commit();
            return LOGIN_SUCCESS;
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
