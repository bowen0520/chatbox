package chatroom;

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
        try {
            lunchRemind();
            mySocket.sendMessage(getUsersMsg(userMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(mySocket.isConnected()&&flag){
            try {
                String message = mySocket.getMessage();
                sendAll("message"+":"+message);
                System.out.println(message);
            } catch (IOException e) {
                flag = false;
                System.out.println(name+"断开连接");
            }
        }
        userMap.remove(name);
        socketMap.remove(name);
        try {
            quitRemind();
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lunchRemind() throws IOException {
        List<String> list = userMap.get(name);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("lunch"+":"+name);
        for(String s:list){
            stringBuilder.append("&"+s);
        }
        sendAll(stringBuilder.toString());
    }

    public void quitRemind() throws IOException {
        String message = "quit"+":"+name;
        sendAll(message);
    }

    public void sendAll(String message) throws IOException {
        Iterator<Map.Entry<String, MySocket>> iterator = socketMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, MySocket> next = iterator.next();
            if(!next.getKey().equals(name)){
                next.getValue().sendMessage(message);
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
        return message.toString()+"end";
    }
}
