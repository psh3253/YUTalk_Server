package server.runnable;

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
                    switch (receivedObject[0]) {
                        case "disconnectRequest":
                            socket.close();
                            return;
                        case "registerRequest": {
                            int responseCode = RegisterService.getInstance().register(receivedObject[1], receivedObject[2], receivedObject[3]);
                            String[] responseObject = new String[2];
                            responseObject[0] = "registerResponse";
                            responseObject[1] = Integer.toString(responseCode);
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
                        case "loginRequest": {
                            String[] responseObject = LoginService.getInstance().login(receivedObject[1], receivedObject[2]);
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
                        case "addFriendRequest": {
                            String[] responseObject = AddFriendService.getInstance().addFriend(receivedObject[1], receivedObject[2]);
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
                        case "createChatRoomRequest": {
                            int roomId = CreateChatRoomService.getInstance().createChatRoom(receivedObject[1], receivedObject[2], receivedObject[3]);
                            String[] responseObject = new String[2];
                            responseObject[0] = "createChatRoomResponse";
                            responseObject[1] = String.valueOf(roomId);
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
                        case "leaveChatRoomRequest": {
                            LeaveChatRoomService.getInstance().leaveChatRoom(receivedObject[1], Integer.parseInt(receivedObject[2]));
                            String[] responseObject = new String[1];
                            responseObject[0] = "leaveChatRoomResponse";
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
                        case "setStatusMessageRequest": {
                            SetStatusMessageService.getInstance().setStatusMessage(receivedObject[1], receivedObject[2]);
                            String[] responseObject = new String[1];
                            responseObject[0] = "setStatusMessageResponse";
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
                        case "sendMessageRequest": {
                            SendMessageService.getInstance().sendMessage(receivedObject[1], Integer.parseInt(receivedObject[2]),  receivedObject[3]);
                            String[] responseObject = new String[1];
                            responseObject[0] = "sendMessageResponse";
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
                        case "loadFriendRequest": {
                            HashMap<String, HashMap<String, String[]>> responseObject = LoadFriendService.getInstance().loadFriend(receivedObject[1]);
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
                        case "loadChatRoomRequest": {
                            HashMap<String, HashMap<Integer, String[]>> responseObject = LoadChatRoomService.getInstance().loadChatRoom(receivedObject[1]);
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
                        case "loadMessageRequest": {
                            HashMap<String, HashMap<Integer, String[]>> responseObject = LoadMessageService.getInstance().loadMessage(Integer.parseInt(receivedObject[1]));
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
                        case "loadChatRoomMemberRequest": {
                            HashMap<String, ArrayList<String[]>> responseObject = LoadChatRoomMemberService.getInstance().loadChatRoomMember(Integer.parseInt(receivedObject[1]));
                            out.writeObject(responseObject);
                            out.flush();
                            break;
                        }
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
