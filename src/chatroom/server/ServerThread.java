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
        boolean flag = true;
        while(flag){
            try {
                String message = mySocket.getMessage();
                Server.sendAll(name,"message"+":"+message);
                System.out.println(message);
            } catch (Exception e) {
                System.out.println(name+"断开连接");
                Server.quitRemind(name);
                try {
                    mySocket.close();
                } catch (IOException ex) {
                    System.out.println("关闭socket");
                }
                flag = false;
            }
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
        String msg = message.toString();
        return msg.equals("")?msg:msg.substring(0,msg.lastIndexOf("\r\n"));
    }
}
