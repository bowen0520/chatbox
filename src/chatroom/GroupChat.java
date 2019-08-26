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
import java.net.Socket;
import java.util.Map;

public class GroupChat extends MyFrame{
    private String myname;
    private String username;
    private MySocket mySocket;
    Map<String, MyFrame> jFrameMap;

    public GroupChat(String myname,String username, MySocket mySocket,Map<String, MyFrame> jFrameMap) {
        super(username);
        this.myname = myname;
        this.username = username;
        this.mySocket = mySocket;
        this.jFrameMap = jFrameMap;
    }

    @Override
    public void listen(JTextArea outputBox,JButton out) {
        out.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = outputBox.getText();
                try {
                    mySocket.sendMessage(myname+":"+message);
                    jFrameMap.get("群聊").getInputBox().append(myname+":"+message+"\r\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                outputBox.setText("");
                outputBox.setFocusable(true);
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrameMap.remove(username);
                System.exit(0);
            }
        });
    }

    @Override
    public JTextArea getInputBox() {
        return super.getInputBox();
    }
}
