package server.service;

import server.dao.ChatRoomDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.ChatRoom;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadChatRoomService {

    private static LoadChatRoomService instance = null;

    public static LoadChatRoomService getInstance() {
        if (instance == null)
            instance = new LoadChatRoomService();
        return instance;
    }

    public HashMap<String, ArrayList<ChatRoom>> loadChatRoom(String userId) {
        Connection connection = null;
        HashMap<String, ArrayList<ChatRoom>> responseObject = new HashMap<>();
        ArrayList<ChatRoom> chatRoomData = new ArrayList<>();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);

            ArrayList<Integer> chatRoomList = ChatRoomDao.getInstance().selectChatRoomList(connection, userId);
            for (int i = 0; i < chatRoomList.size(); i++) {
                ChatRoom chatRoom = ChatRoomDao.getInstance().selectChatRoom(connection, chatRoomList.get(i), userId);
                chatRoomData.add(chatRoom);
            }
            responseObject.put("loadChatRoomResponse", chatRoomData);
            return responseObject;
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
