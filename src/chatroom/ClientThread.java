package chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class ClientThread implements Runnable{
    //用户名
    private String name;

    //服务器ip
    private String serverIP;

    //用户各个端口  端口1：用于聊天
    private String[] ports;

    //与服务端交互的socket
    private Socket socket;
    //交互的流；
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private MySocket mySocket;

    DatagramSocket datagramSocket;
    DatagramPacket packet;
    //在线好友的信息 //
    private Map<String, List<String>> userMap;

    //开启的好友聊天窗口
    private Map<String, MyFrame> jFrameMap;

    private boolean flag = true;

    public ClientThread(String name,String serverIP,String[] ports) throws IOException {
        this.name = name;
        this.serverIP = serverIP;
        this.ports = ports;
        this.socket = new Socket(this.serverIP,8990);
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printWriter = new PrintWriter(socket.getOutputStream(),true);
        this.mySocket = new MySocket(socket,bufferedReader,printWriter);

        this.datagramSocket = new DatagramSocket(Integer.parseInt(ports[0]));
        byte[] buf = new byte[1024*10];
        this.packet = new DatagramPacket(buf, buf.length);

        userMap = new HashMap<>();
        jFrameMap = new HashMap<>();
        printWriter.println(name+"&"+ports[0]);
        addUsers();

        new Thread(() -> {
            while(true){
                try {
                    String message = mySocket.getMessage();
                    String[] msgs = message.split(":",2);
                    if(msgs[0].equals("上线提醒")){
                        addUser(msgs[1]);
                    }else if(msgs[0].equals("下线提醒")){
                        System.out.println(msgs[1]+"下线了");
                        userMap.remove(msgs[1]);
                    }else if(msgs[0].equals("消息提醒")){
                        getFrame("群聊").getInputBox().append(msgs[1]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while(true) {
                try {
                    datagramSocket.receive(packet);
                    String msgs = new String(packet.getData(), 0, packet.getLength());
                    String getname = msgs.split(":", 2)[0];
                    getFrame(getname).getInputBox().append(msgs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public MyFrame getFrame(String username) throws SocketException {
        MyFrame myFrame = null;
        if(jFrameMap.containsKey(username)){
            myFrame = jFrameMap.get(username);
            myFrame.setAlwaysOnTop(true);
            myFrame.setAlwaysOnTop(false);
        }else {
            if(username.equals("群聊")){
                myFrame = new GroupChat(name, "群聊", mySocket, jFrameMap);
                jFrameMap.put("群聊",myFrame);
            }else{
                if(userMap.containsKey(username)){
                    List<String> userList= userMap.get(username);
                    myFrame = new ClientFrame(name, username, userList,userMap, jFrameMap);
                    jFrameMap.put(username,myFrame);
                }else{
                    System.out.println("对方不在线");
                }
            }
        }
        return myFrame;
    }

    private void addUser(String msg) {
        String[] ss = msg.split("&");
        List<String> userList = new ArrayList<>();
        for(int i = 1;i<ss.length;i++){
            userList.add(ss[i]);
        }
        System.out.println(ss[0]+"上线");
        userMap.put(ss[0],userList);
    }

    private void addUsers() throws IOException {
        String str = "";
        while(!(str = bufferedReader.readLine()).equals("end")){
            addUser(str);
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(flag){
            System.out.print("请输入聊天对象：");
            String username = scanner.nextLine();
            System.out.println(username);
            if(username.equals("quit")){
                flag = false;
                break;
            }
            try {
                getFrame(username);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
