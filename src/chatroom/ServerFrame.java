package chatroom;

import javax.swing.*;
import java.awt.*;

public class ServerFrame extends JFrame {
    private JList<Button> userList;
    private JList<JTextArea> messageBox;
    private JButton start;
    private JButton stop;
    private JTextField portText;

    public ServerFrame() throws HeadlessException {
        this.setTitle("服务器");
        this.setLayout(new BorderLayout(2,2));
        this.setLocationRelativeTo(null);
        this.setSize(500,400);
        this.setResizable(false);

        JPanel userListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT),true);

        JPanel messagePanel = new JPanel(new CardLayout(),true);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,4,4),true);
        start = new JButton("开启服务器");
        stop = new JButton("关闭服务器");



        this.add(userList,BorderLayout.WEST);
        this.add(messageBox,BorderLayout.CENTER);
        this.add(start,BorderLayout.EAST);
        this.add(stop,BorderLayout.EAST);
        this.add(portText,BorderLayout.NORTH);

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new ServerFrame();
    }
}
