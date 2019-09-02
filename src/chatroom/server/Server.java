package chatroom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    //用于接受用户信息并根据信息执行相应操作
    private ServerSocket serverSocket;

    //用于存储与其他用户交互的socket
    private static Map<String, MySocket> socketMap;

    //用于存储在线用户的信息  用户名：ip+ports
    private static Map<String, List<String>> userMap;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(8990);
        this.socketMap = new HashMap<>();
        this.userMap = new HashMap<>();
    }

    public void run()  {
        while(true){
            try {
                synchronized ("server") {
                    Socket accept = serverSocket.accept();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                    PrintWriter printWriter = new PrintWriter(accept.getOutputStream(),true);
                    String[] message = bufferedReader.readLine().split("&");
                    String username = message[0];
                    if(socketMap.containsKey(username)){
                        printWriter.println("用户存在");
                        accept.close();
                    }else{
                        printWriter.println("用户不存在");
                        List<String> list = new ArrayList<>();
                        String ip = accept.getInetAddress().getHostAddress();
                        list.add(ip);
                        for(int i = 1;i<message.length;i++){
                            list.add(message[i]);
                        }
                        MySocket mySocket = new MySocket(accept,bufferedReader,printWriter);
                        socketMap.put(username, mySocket);
                        userMap.put(username,list);
                        this.sendAll(username,lunchRemind(username,list));
                        new Thread(new ServerThread(username,mySocket,socketMap,userMap)).start();
                    }
                }
            }catch (IOException e){
                e.getStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().run();
    }

    public static void sendAll(String name ,String message) {
        Iterator<Map.Entry<String, MySocket>> iterator = socketMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, MySocket> next = iterator.next();
            if(!next.getKey().equals(name)){
                next.getValue().sendMessage(message);
            }
        }
    }

    public String lunchRemind(String name,List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("lunch"+":"+name);
        for(String s:list){
            stringBuilder.append("&"+s);
        }
        return stringBuilder.toString();
    }

    public static void quitRemind(String name) {
        String message = "quit"+":"+name;
        userMap.remove(name);
        socketMap.remove(name);
        sendAll(name,message);
    }
}
