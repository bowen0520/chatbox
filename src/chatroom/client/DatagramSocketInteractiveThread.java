package chatroom.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import java.util.Map;

public class DatagramSocketInteractiveThread implements Runnable {
    private String name;
    private DatagramSocket datagramSocket;
    private DatagramPacket packet;
    private Map<String, List<String>> userMap;
    private Map<String, MyFrame> privateChatMap;

    public DatagramSocketInteractiveThread(String name, DatagramSocket datagramSocket, DatagramPacket packet, Map<String, List<String>> userMap, Map<String, MyFrame> privateChatMap) {
        this.name = name;
        this.datagramSocket = datagramSocket;
        this.packet = packet;
        this.userMap = userMap;
        this.privateChatMap = privateChatMap;
    }

    @Override
    public void run() {
        boolean flag = true;
        while(flag) {
            try {
                datagramSocket.receive(packet);
                String msgs = new String(packet.getData(), 0, packet.getLength());
                String getname = msgs.split(":", 2)[0];
                FrameUtil.getPrivateFrame(name,getname,userMap,privateChatMap).getInputBox().append(msgs+"\r\n");
            } catch (IOException e) {
                System.out.println("客户端端口异常");
                flag = false;
                System.exit(0);
            }
        }
    }
}
