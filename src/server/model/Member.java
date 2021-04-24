package server.model;

import java.util.Date;

public class Member{

    private String userId;
    private String password;
    private String name;
    private String statusMessage;
    private Date registerTime;
    private Date lastLoginTime;

    public Member(String userId, String password, String name, String statusMessage, Date registerTime, Date lastLoginTime) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.statusMessage = statusMessage;
        this.registerTime = registerTime;
        this.lastLoginTime = lastLoginTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
