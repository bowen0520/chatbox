package chatroom.client;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Container;

public abstract class MyFrame extends JFrame{

    private JTextArea inputBox;
    private JTextArea outputBox;
    private JButton out;
    private int high;
    private int wide;

    public JTextArea getInputBox() {
        return inputBox;
    }

    public JTextArea getOutputBox() {
        return outputBox;
    }

    public JButton getOut() {
        return out;
    }

    private String username;

    public MyFrame(String username) {
        this.high = 500;
        this.wide = 400;
        this.username = username;
        getFrame();
    }

    private void getFrame(){
        this.setTitle(username);
        this.setLayout(null);
        this.setBounds(300,200,wide,high);
        this.setSize(wide, high);
        this.setResizable(false);
        Container c = this.getContentPane();

        inputBox = new JTextArea();
        inputBox.setLineWrap(true);
        inputBox.setEditable(false);
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
        this.setVisible(true);
        setEvent(outputBox,out,inputBox);
    }
    public abstract void setEvent(JTextArea outputBox,JButton out,JTextArea inputBox);
}
