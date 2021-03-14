package server;

import server.jdbc.ConnectionProvider;
import server.runnable.ServerRunnable;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        UIManager.put("OptionPane.messageFont", new Font("맑은 고딕", Font.PLAIN, 15));
        UIManager.put("OptionPane.buttonFont", new Font("맑은 고딕", Font.PLAIN, 15));
        ConnectionProvider.getInstance().initConnection();
        waitClient();
    }

    public static void waitClient()
    {
        Thread thread = new Thread(new ServerRunnable());
        thread.start();
    }
}
