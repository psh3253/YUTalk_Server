package server.service;

import server.dao.ChatRoomDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.ChatRoom;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class LoadChatRoomService {

    private static LoadChatRoomService instance = null;

    public static LoadChatRoomService getInstance() {
        if (instance == null)
            instance = new LoadChatRoomService();
        return instance;
    }

    public HashMap<String, HashMap<Integer, String[]>> loadChatRoom(String userId) {
        Connection connection = null;
        HashMap<String, HashMap<Integer, String[]>> responseObject = new HashMap<>();
        HashMap<Integer, String[]> chatRoomData = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);

            ArrayList<Integer> chatRoomList = ChatRoomDao.getInstance().selectChatRoomList(connection, userId);
            for (int i = 0; i < chatRoomList.size(); i++) {
                ChatRoom chatRoom = ChatRoomDao.getInstance().selectChatRoom(connection, chatRoomList.get(i));
                chatRoomData.put(chatRoomList.get(i), new String[]{
                        Integer.toString(chatRoom.getRoomId()),
                        chatRoom.getRoomType(),
                        chatRoom.getName(),
                        Integer.toString(chatRoom.getHeadcount()),
                        chatRoom.getLastMessage(),
                        format.format(chatRoom.getLastTime()),
                        Integer.toString(chatRoom.getUnreadMessageCount())
                });
            }
            responseObject.put("loadChatRoomResponse", chatRoomData);
            connection.commit();
            return responseObject;
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
