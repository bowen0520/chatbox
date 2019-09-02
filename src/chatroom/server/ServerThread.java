package chatroom.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ServerThread implements Runnable{
    //连接的用户名
    private String name;

    //连接的用户的socket
    private MySocket mySocket;

    //所有连接的用户 用户名：：用户连接的socket
    private Map<String, MySocket> socketMap;

    //所有连接的用户的信息 用户名：ip：：ports
    private Map<String,List<String>> userMap;

    //判断是否在线
    private boolean flag = true;

    public ServerThread(String name, MySocket mySocket, Map<String, MySocket> socketMap,Map<String, List<String>> userMap) {
        this.name = name;
        this.mySocket = mySocket;
        this.socketMap = socketMap;
        this.userMap = userMap;
    }

    @Override
    public void run() {
        System.out.println(name+"上线了");
        mySocket.sendMessage(getUsersMsg(userMap));
        while(mySocket.isConnected()&&flag){
            try {
                String message = mySocket.getMessage();
                Server.sendAll(name,"message"+":"+message);
                System.out.println(message);
            } catch (IOException e) {
                flag = false;
                System.out.println(name+"断开连接");
            }
        }
        try {
            Server.quitRemind(name);
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsersMsg(Map<String,List<String>> userMap){
        StringBuilder message = new StringBuilder();
        Iterator<Map.Entry<String, List<String>>> iterator = userMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, List<String>> next = iterator.next();
            if(!next.getKey().equals(name)) {
                message.append(next.getKey());
                for (String s : next.getValue()) {
                    message.append("&" + s);
                }
                message.append("\r\n");
            }
        }
        return message.toString()+"end";
    }
}
