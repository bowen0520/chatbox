package chatroom;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.List;

public class ClientFrame extends MyFrame {
    private String myname;
    private String username;
    private List<String> list;
    private Map<String,List<String>> userMap;
    private Map<String, MyFrame> jFrameMap;
    DatagramSocket send;

    public ClientFrame(String myname, String username, List<String> list, Map<String,List<String>> userMap, Map<String, MyFrame> jFrameMap) throws SocketException {
        super(username);
        this.myname = myname;
        this.username = username;
        this.list = list;
        this.userMap = userMap;
        this.jFrameMap = jFrameMap;
        this.send = new DatagramSocket();
    }

    @Override
    public void listen(JTextArea outputBox, JButton out) {
        out.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(userMap.containsKey(username)) {
                    String s = outputBox.getText();
                    try {
                        byte[] message = (myname + ":" + s).getBytes();
                        InetAddress address = InetAddress.getByName(list.get(0));
                        DatagramPacket packet = new DatagramPacket(message, message.length, address,Integer.parseInt(list.get(1)));
                        send.send(packet);
                        jFrameMap.get(username).getInputBox().append(myname + ":" + s + "\r\n");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    outputBox.setText("");
                }else {
                    jFrameMap.get(username).getInputBox().append(username+"不在线");
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

    @Override
    public JTextArea getInputBox() {
        return super.getInputBox();
    }
}
