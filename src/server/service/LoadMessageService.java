package server.service;

import server.dao.MemberDao;
import server.dao.MessageDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.Message;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadMessageService {

    private static LoadMessageService instance = null;

    public static LoadMessageService getInstance() {
        if (instance == null)
            instance = new LoadMessageService();
        return instance;
    }

    public HashMap<String, HashMap<Integer, String[]>> loadMessage(int roomId) {
        Connection connection = null;
        HashMap<String, HashMap<Integer, String[]>> responseObject = new HashMap<>();
        HashMap<Integer, String[]> messageData = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);
            ArrayList<Integer> messageList = MessageDao.getInstance().selectMessageList(connection, roomId);
            for (Integer integer : messageList) {
                Message message = MessageDao.getInstance().selectMessage(connection, integer);
                if (!message.getMessageType().equals("info")) {
                    messageData.put(integer, new String[]{
                            Integer.toString(message.getMessageId()),
                            message.getUserId(),
                            MemberDao.getInstance().selectMember(connection, message.getUserId()).getName(),
                            message.getMessageType(),
                            message.getMessage(),
                            format.format(message.getSendTime())
                    });
                } else {
                    messageData.put(integer, new String[]{
                            Integer.toString(message.getMessageId()),
                            message.getUserId(),
                            "시스템",
                            message.getMessageType(),
                            message.getMessage(),
                            format.format(message.getSendTime())
                    });
                }
            }
            responseObject.put("loadMessageResponse", messageData);
            connection.commit();
            return responseObject;
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
