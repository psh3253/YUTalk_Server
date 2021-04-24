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

    public String[] login(String userId, String password) {
        Connection connection = null;
        String[] response = new String[5];
        response[0] = "loginResponse";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);

            Member member = MemberDao.getInstance().selectMember(connection, userId);
            if (member == null) {
                JdbcUtil.getInstance().rollback(connection);
                response[1] = Integer.toString(NOT_EXIST_ID);
                return response;
            } else if (!password.equals(member.getPassword())) {
                JdbcUtil.getInstance().rollback(connection);
                response[1] = Integer.toString(NOT_MATCH_PASSWORD);
                return response;
            }
            MemberDao.getInstance().updateLogin(connection, userId);

            connection.commit();
            response[1] = Integer.toString(LOGIN_SUCCESS);
            response[2] = member.getName();
            response[3] = member.getStatusMessage();
            return response;
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
