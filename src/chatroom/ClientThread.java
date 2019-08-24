package chatroom;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class ClientThread implements Runnable{
    private String name;
    private String ip;
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private static Map<String,String> ipMap;
    private static Map<String, JFrame> jFrameMap;

    public ClientThread(String name,String ip) throws IOException {
        this.name = name;
        this.ip = ip;
        socket = new Socket(this.ip,8990);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(),true);
        printWriter.println(name);
        ipMap = new HashMap<>();
        jFrameMap = new HashMap<>();
        new Thread(() -> {
            while(true){
                try {
                    Map<String,String> temp = new HashMap<>();
                    String str = null;
                    while(!(str = bufferedReader.readLine()).equals("end")) {
                        String[] msg = str.split("::");
                        temp.put(msg[0], msg[1]);
                    }
                    printWriter.println("收到了"+"\r\n"+"end");
                    if(!checkUser(ipMap,temp)){
                        Iterator<Map.Entry<String, String>> iterator = ipMap.entrySet().iterator();
                        while(iterator.hasNext()){
                            Map.Entry<String, String> next = iterator.next();
                            System.out.println(next.getKey() + "::" + next.getValue());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        this.run();
    }

    @Override
    public void run() {
        while(socket.isConnected()){
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入聊天对象");
            String username = scanner.nextLine();
            if(ipMap.containsKey(username)){
                if(jFrameMap.containsKey(username)){
                    jFrameMap.get(username).setAlwaysOnTop(true);
                    jFrameMap.get(username).setAlwaysOnTop(false);
                }else{
                    try {
                        ClientFrame clientFrame = new ClientFrame(name, username, ipMap.get(username), ipMap, jFrameMap);
                        jFrameMap.put(username,clientFrame);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                System.out.println("对方不在线");
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUser(Map<String,String> ipMap,Map<String,String> temp){
        boolean flag = true;
        Iterator<Map.Entry<String, String>> iteratorIp = ipMap.entrySet().iterator();
        while(iteratorIp.hasNext()){
            Map.Entry<String, String> next = iteratorIp.next();
            if(!temp.containsKey(next.getKey())){
                System.out.println(next.getKey()+"下线了");
                flag = false;
            }
        }
        Iterator<Map.Entry<String, String>> iteratorTemp = temp.entrySet().iterator();
        while(iteratorTemp.hasNext()){
            Map.Entry<String, String> next = iteratorTemp.next();
            if(!ipMap.containsKey(next.getKey())){
                System.out.println(next.getKey()+"上线了");
                flag = false;
            }
        }
        ipMap.clear();
        ipMap.putAll(temp);
        return flag;
    }
}
