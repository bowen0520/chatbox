package old;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatFrame extends JFrame {
    private JTextArea inputBox;
    private JTextArea outputBox;
    private JButton out;
    private int high;
    private int wide;
    private Socket socket;

    public JTextArea getOutputBox() {
        return outputBox;
    }

    public ChatFrame(String name) {
        this.high = 500;
        this.wide = 400;
        try {
            socket = new Socket(name, 8990);
            OutputStreamWriter socketOut = new OutputStreamWriter(socket.getOutputStream());
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            getFrame(name,socketOut,socketIn);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChatFrame(int high,int wide,String name){
        this.high = high;
        this.wide = wide;
        try {
            socket = new Socket(name, 8990);
            OutputStreamWriter socketOut = new OutputStreamWriter(socket.getOutputStream());
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            getFrame(name,socketOut,socketIn);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFrame(String name,OutputStreamWriter socketOut,BufferedReader socketIn){
        this.setName(name);
        this.setLayout(null);
        Container c = this.getContentPane();
        outputBox = new JTextArea();
        //outputBox.setEditable(false);
        outputBox.setLineWrap(true);
        JScrollPane s1 = new JScrollPane(outputBox);
        s1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        s1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        s1.setBounds(2, 0, wide-22, high*5/10);
        inputBox = new JTextArea();
        inputBox.setLineWrap(true);
        JScrollPane s2 = new JScrollPane(inputBox);
        s2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        s2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        s2.setBounds(2, high*5/10, wide-22, high*3/10);
        out = new JButton("发送");
        out.setBounds(2, high*8/10, 100, high*1/10);
        c.add(s1);
        c.add(s2);
        c.add(out);
        this.setVisible(true);
        this.setSize(wide, high);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        out.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String data = inputBox.getText();
                if(data!=null&&data.trim().length()!=0){
                    try {
                        String[] each = data.split("\n");
                        System.out.println(each.length);
                        String message = InetAddress.getLocalHost().getHostAddress().toString();
                        for(int i = 0;i<each.length;i++){
                            message = message+"\n"+each[i];
                            System.out.println(each[i]);
                        }
                        message = message+"\r\n";
                        System.out.println(message);
                        socketOut.write(message);
                        socketOut.flush();
                        outputBox.append(InetAddress.getLocalHost().getHostAddress().toString()+":\r\n"+data);
                        outputBox.append(socketIn.readLine());
                        inputBox.setText("");

                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                for(int i = 0; i< ServerChat.list.size(); i++){
                    if(name.equals(ServerChat.list.get(i).getName())){
                        ServerChat.list.remove(i);
                    }
                }
                try {
                    socketOut.close();
                    socketIn.close();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
