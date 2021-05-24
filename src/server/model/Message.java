package server.model;

import java.util.Date;

public class Message {

    private int messageId;
    private String userId;
    private int roomId;
    private String messageType;
    private String message;
    private Date sendTime;

    public Message(int messageId, String userId, int roomId, String messageType, String message, Date sendTime) {
        this.messageId = messageId;
        this.userId = userId;
        this.roomId = roomId;
        this.messageType = messageType;
        this.message = message;
        this.sendTime = sendTime;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
