package chatroom.client;

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

    private DatagramSocket datagramSocket;
    private DatagramPacket packet;

    //在线好友的信息 //
    private Map<String, List<String>> userMap;

    //开启的好友聊天窗口
    private Map<String,MyFrame> groupChatMap;
    private Map<String, MyFrame> privateChatMap;

    public ClientThread(String name,String serverIP,String[] ports) throws IOException {
        this.name = name;
        this.serverIP = serverIP;
        this.ports = ports;
        this.socket = new Socket(this.serverIP,8990);
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printWriter = new PrintWriter(socket.getOutputStream(),true);

        this.datagramSocket = new DatagramSocket(Integer.parseInt(ports[0]));
        byte[] buf = new byte[1024*10];
        this.packet = new DatagramPacket(buf, buf.length);

        this.userMap = new HashMap<>();
        this.groupChatMap = new HashMap<>();
        this.privateChatMap = new HashMap<>();
    }

    @Override
    public void run() {
        printWriter.println(name+"&"+ports[0]);
        String str = "";
        try {
            while (!(str = bufferedReader.readLine()).equals("end")) {
                UserUtil.addUser(str,userMap);
            }
        }catch (IOException e){
            System.out.println("获取好友异常");
            e.printStackTrace();
        }

        new Thread(new SocketInteractiveThread(name,userMap,groupChatMap,bufferedReader,printWriter)).start();
        new Thread(new DatagramSocketInteractiveThread(name,datagramSocket,packet,userMap,privateChatMap)).start();

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("请输入聊天对象：");
            String username = scanner.nextLine();
            if(username.equals("quit")){
                try {
                    datagramSocket.close();
                    socket.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(username.equals("群聊")){
                FrameUtil.getGroupFrame(name,printWriter,groupChatMap);
            }else{
                try {
                    FrameUtil.getPrivateFrame(name,username,userMap,privateChatMap);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
