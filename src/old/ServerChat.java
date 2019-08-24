package old;

import old.ChatFrame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerChat extends Thread{
	public static ArrayList<ChatFrame> list = new ArrayList<>();
	private  Socket socket;
	
	public void run() {
		while(true){
			try{
				BufferedReader socketRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				OutputStreamWriter socketOut = new OutputStreamWriter(socket.getOutputStream());
				
				String info = socketRead.readLine();
				String[] data = info.split("\n");
				String ipname = data[0];
				boolean flag = false;
				for(int i = 0;i<list.size();i++){
					if(ipname.equals(list.get(i).getName()))
					{
						flag = true;
						list.get(i).setAlwaysOnTop(!list.get(i).isAlwaysOnTop());
						list.get(i).setAlwaysOnTop(false);
						list.get(i).getOutputBox().append(data[0]+":\r\n");
						for(int j = 1;j<data.length;j++){
							list.get(i).getOutputBox().append(data[j]+"\r\n");
						}
						break;
					}
				}
				if(!flag){
					ChatFrame c = new ChatFrame(ipname);
					list.add(c);
					c.getOutputBox().append(data[0]+":\r\n");
					for(int j = 1;j<data.length;j++){
						c.getOutputBox().append(data[j]+"\r\n");
					}
				}
				socketOut.write("\r\n");
				socketOut.flush();
			}catch (Exception e) {
				e.getMessage();
			}
		}
	}
	
	public ServerChat(Socket socket) {
		super();
		this.socket = socket;
	}
}
