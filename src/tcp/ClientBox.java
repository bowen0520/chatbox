package tcp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class ClientBox extends JFrame{
    private JTextArea inputBox;
    private JTextArea outputBox;
    private JButton out;
    private int high;
    private int wide;
    String name;
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    boolean flag = true;

    public ClientBox(String name,String ip) {
        this.high = 500;
        this.wide = 400;
        this.name = name;
        try {
            this.socket = new Socket(ip,8990);;
            printWriter = new PrintWriter(socket.getOutputStream(),true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter.println(name);
            getFrame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFrame(){
        this.setName(name);
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
            while(flag){
                try {
                    String str = bufferedReader.readLine();
                    inputBox.append(str+"\r\n");
                    while(true){
                        str = bufferedReader.readLine();
                        if(str.equals("end")){
                            break;
                        }
                        inputBox.append(str+"\r\n");
                    }
                } catch (IOException e) {
                    inputBox.append("断开连接");
                    flag = false;
                }
            }
        }).start();

        out.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String s = outputBox.getText();
                String message = null;
                if(s!=null&&(!s.equals(""))) {
                    if (s.startsWith("@")) {
                        String[] strings = s.split(":", 2);
                        message = strings[0].substring(1) + ":" + strings[1];
                    } else {
                        message = "所有人:" + s;
                    }
                    printWriter.println(message + "\r\n" + "end");
                    inputBox.append(message+"\r\n");
                }
                outputBox.setText("");
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                flag = false;
                System.exit(0);
            }
        });

        this.setVisible(true);
    }
}
