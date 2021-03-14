package server.runnable;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunnable implements Runnable {
    @Override
    public void run() {
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(2121);
            while (true) {
                socket = serverSocket.accept();

                Thread thread = new Thread(new ClientManager(socket));
                thread.start();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "소켓 생성에 실패하였습니다.", "실행 실패", JOptionPane.WARNING_MESSAGE);
            System.exit(-1);
        }
    }
}
