package server.service;


import server.dao.MemberDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.Member;

import java.sql.Connection;
import java.sql.SQLException;

public class RegisterService {

    public static final int REGISTER_SUCCESS = 0;

    public static final int DUPLICATE_ID = 1;

    public static RegisterService instance = null;

    public static RegisterService getInstance() {
        if (instance == null)
            instance = new RegisterService();
        return instance;
    }

    public int register(String userId, String password, String name) {
        Connection connection = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);

            Member member = MemberDao.getInstance().selectByUserId(connection, userId);
            if(member != null) {
                JdbcUtil.getInstance().rollback(connection);
                return DUPLICATE_ID;
            }

            MemberDao.getInstance().insert(connection, userId, password, name);
            connection.commit();
            return REGISTER_SUCCESS;
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
