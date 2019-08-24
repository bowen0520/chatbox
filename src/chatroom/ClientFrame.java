package chatroom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Map;

public class ClientFrame extends JFrame {
    private JTextArea inputBox;
    private JTextArea outputBox;
    private JButton out;
    private int high;
    private int wide;

    private String myname;
    private String username;
    private String ip;
    private Map<String,String> ipMap;
    private Map<String,JFrame> jFrameMap;

    DatagramSocket receive;
    DatagramPacket packet;
    DatagramSocket send;

    public ClientFrame(String myname,String username, String ip, Map<String,String> ipMap,Map<String,JFrame> jFrameMap) throws SocketException {
        this.high = 500;
        this.wide = 400;
        this.myname = myname;
        this.username = username;
        this.ip = ip;
        this.ipMap = ipMap;
        this.jFrameMap = jFrameMap;

        this.receive = new DatagramSocket(9090);
        byte[] buf = new byte[1024];
        this.packet = new DatagramPacket(buf, buf.length);

        this.send = new DatagramSocket();
        getFrame();
    }

    private void getFrame(){
        this.setName(username);
        this.setLayout(null);
        this.setBounds(300,200,wide,high);
        this.setSize(wide, high);
        this.setResizable(false);
        Container c = this.getContentPane();

        inputBox = new JTextArea();
        inputBox.setLineWrap(true);
        JScrollPane s1 = new JScrollPane(inputBox);
        s1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        s1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        s1.setBounds(4, 2, wide-12, high*5/10);

        outputBox = new JTextArea();
        outputBox.setLineWrap(true);
        JScrollPane s2 = new JScrollPane(outputBox);
        s2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        s2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        s2.setBounds(4, high*5/10+4, wide-12, high*3/10);

        out = new JButton("发送");
        out.setBounds(4, high*8/10+6, 100, high*1/10);

        c.add(s1);
        c.add(s2);
        c.add(out);

        new Thread(() -> {
            while(ipMap.containsKey(username)) {
                try {
                    receive.receive(packet);
                    String[] ss = new String(packet.getData(), 0, packet.getLength()).split(":");
                    if(ss[0].equals(username)){
                        inputBox.append(ss[0]+":"+ss[1]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            inputBox.append(username+"不在线");
        }).start();

        out.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(ipMap.containsKey(username)) {
                    String s = outputBox.getText();
                    try {
                        byte[] message = (myname + ":" + s).getBytes();
                        InetAddress address = InetAddress.getByName(ip);
                        DatagramPacket packet = new DatagramPacket(message, message.length, address, 9090);
                        send.send(packet);
                        inputBox.append(myname + ":" + s + "\r\n");

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    outputBox.setText("");
                }else {
                    inputBox.append(username+"不在线");
                }
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrameMap.remove(username);
                System.exit(0);
            }
        });
        this.setVisible(true);
    }
}
