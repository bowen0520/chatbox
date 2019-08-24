package old;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Main {
	
	public static void Frame(){
		JFrame window = new JFrame("");
		window.setLayout(null);
		Container c = window.getContentPane();
		JTextField t = new JTextField(20);
		t.setBounds(50, 15, 300, 30);
		JButton b = new JButton("����Ϣ");
		b.setBounds(150, 55, 100, 55);
		c.add(t);
		c.add(b);
		window.setVisible(true);
		window.setSize(400, 165);
		window.setResizable(false);
		window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean flag = false;
				for(int i = 0; i< ServerChat.list.size(); i++){
					if(t.getText().equals(ServerChat.list.get(i).getName()))
					{
						flag = true;
						ServerChat.list.get(i).setAlwaysOnTop(!ServerChat.list.get(i).isAlwaysOnTop());
						ServerChat.list.get(i).setAlwaysOnTop(false);
						break;
					}
				}
				if(!flag){
					ChatFrame c = new ChatFrame(t.getText());
					ServerChat.list.add(c);
				}
			}
		});
	}
	
	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(8990);
			Frame();
			while(true){
				Socket socket = server.accept();
				ServerChat s = new ServerChat(socket);
				s.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
