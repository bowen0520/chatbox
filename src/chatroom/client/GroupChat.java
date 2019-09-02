package chatroom.client;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.util.Map;

public class GroupChat extends MyFrame{
    private String myname;
    private String username;
    private PrintWriter printWriter;
    Map<String, MyFrame> groupChatMap;



    public GroupChat(String myname,String username, PrintWriter printWriter,Map<String, MyFrame> groupChatMap) {
        super(username);
        this.myname = myname;
        this.username = username;
        this.printWriter = printWriter;
        this.groupChatMap = groupChatMap;
    }

    @Override
    public void setEvent(JTextArea outputBox,JButton out, JTextArea inputBox) {
        out.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = outputBox.getText();
                printWriter.println(myname+":"+message+"\r\n"+"end");
                inputBox.append(myname+":"+message+"\r\n");
                outputBox.setText("");
                outputBox.requestFocus();
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                groupChatMap.get(username).setVisible(false);
                groupChatMap.remove(username);
            }
        });
    }
}
