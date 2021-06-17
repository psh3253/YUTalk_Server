package server;

import server.jdbc.ConnectionProvider;
import server.runnable.ServerRunnable;

public class Main {
    public static void main(String[] args) {
        ConnectionProvider.getInstance().initConnection();
        waitClient();
    }

    public static void waitClient()
    {
        Thread thread = new Thread(new ServerRunnable());
        thread.start();
    }
}
