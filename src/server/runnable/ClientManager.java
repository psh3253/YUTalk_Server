package server.runnable;

import server.dao.MemberDao;
import server.model.Member;
import server.service.LoginService;
import server.service.RegisterService;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientManager implements Runnable{

    private Socket socket;
    ClientManager(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        ObjectOutputStream out;
        ObjectInputStream in;
        String[] receivedObject;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                try {
                    receivedObject = (String[]) in.readObject();
                    if(receivedObject[0].equals("disconnectRequest")) {
                        socket.close();
                        return;
                    } else if(receivedObject[0].equals("registerRequest")) {
                        int responseCode = RegisterService.getInstance().register(receivedObject[1], receivedObject[2], receivedObject[3]);
                        String[] responseObject = new String[2];
                        responseObject[0] = "registerResponse";
                        responseObject[1] = Integer.toString(responseCode);
                        out.writeObject(responseObject);
                        out.flush();
                    } else if(receivedObject[0].equals("loginRequest")) {
                        int responseCode = LoginService.getInstance().login(receivedObject[1], receivedObject[2]);
                        String [] responseObject = new String[2];
                        responseObject[0] = "loginResponse";
                        responseObject[1] = Integer.toString(responseCode);
                        out.writeObject(responseObject);
                        out.flush();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
