package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

public class Chat extends Thread{
    String name;
    MySocket mySocket;
    Map<String, MySocket> socketMap;

    public Chat(String name, MySocket mySocket, Map<String, MySocket> socketMap) {
        this.name = name;
        this.mySocket = mySocket;
        this.socketMap = socketMap;
    }

    @Override
    public void run() {
        String str = name+"上线了";
        System.out.println(str);
        try {
            sendAll(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                String s = mySocket.getMessage();
                if(s!=null&&(!s.equals(""))) {
                    String[] ss = s.split(":", 2);
                    if (ss[0].equals("所有人")) {
                        str = "[所有人]" +name+":\r\n"+ ss[1];
                        sendAll(str);
                    } else {
                        str = "[" + ss[0] + "]" +name+":\r\n"+ ss[1];
                        sendOne(ss[0], str);
                    }
                    System.out.println(str);
                }
            } catch (IOException e) {
                socketMap.remove(name);
                try {
                    sendAll("[所有人]"+name+"断开了连接");
                    System.out.println("[所有人]\r\n"+name+"断开了连接");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
    }

    public void sendAll(String message) throws IOException {
        Set<String> strings = socketMap.keySet();
        for(String o:strings){
            if(o!=name) {
                sendOne(o, message);
            }
        }
    }
    public void sendOne(String yourName,String message) throws IOException {
        if(!socketMap.containsKey(yourName)){
            mySocket.sendMessage("对象不在线");
        }else {
            MySocket youSocket = socketMap.get(yourName);
            if (youSocket.isConnected()) {
                youSocket.sendMessage(message);
            } else {
                socketMap.remove(yourName);
                mySocket.sendMessage("对象断开连接");
            }
        }
    }
}
