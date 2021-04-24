package server.runnable;

import server.model.ChatRoom;
import server.service.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientManager implements Runnable {

    private Socket socket;

    ClientManager(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String[] receivedObject;
        ObjectOutputStream out;
        ObjectInputStream in;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                try {
                    receivedObject = (String[]) in.readObject();
                    if (receivedObject[0].equals("disconnectRequest")) {
                        socket.close();
                        return;
                    } else if (receivedObject[0].equals("registerRequest")) {
                        int responseCode = RegisterService.getInstance().register(receivedObject[1], receivedObject[2], receivedObject[3]);
                        String[] responseObject = new String[2];
                        responseObject[0] = "registerResponse";
                        responseObject[1] = Integer.toString(responseCode);
                        out.writeObject(responseObject);
                        out.flush();
                    } else if (receivedObject[0].equals("loginRequest")) {
                        String[] responseObject = LoginService.getInstance().login(receivedObject[1], receivedObject[2]);
                        out.writeObject(responseObject);
                        out.flush();
                    } else if (receivedObject[0].equals("addFriendRequest")) {
                        String[] responseObject = AddFriendService.getInstance().addFriend(receivedObject[1], receivedObject[2]);
                        out.writeObject(responseObject);
                        out.flush();
                    } else if(receivedObject[0].equals("loadFriendRequest")) {
                        HashMap<String, HashMap<String, String[]>> responseObject = LoadFriendService.getInstance().loadFriend(receivedObject[1]);
                        out.writeObject(responseObject);
                        out.flush();
                    } else if(receivedObject[0].equals("loadChatRoomRequest")) {
                        HashMap<String, ArrayList<ChatRoom>> responseObject = LoadChatRoomService.getInstance().loadChatRoom(receivedObject[1]);
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
