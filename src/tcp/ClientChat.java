package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat{
	String name;
	Socket socket;
	Scanner scanner;
	BufferedReader bufferedReader;
	PrintWriter printWriter;

	public ClientChat(String name,Socket socket) throws IOException {
		this.name = name;
		this.socket = socket;
		scanner = new Scanner(System.in);
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printWriter = new PrintWriter(socket.getOutputStream(),true);
		printWriter.println(name);
	}
	boolean flag = true;
	public void run() throws IOException {
		new Thread(() -> {
			while(flag){
				try {
					String s = bufferedReader.readLine();
					if(s!=null&&(!s.equals(""))) {
						System.out.println(s);
					}
				} catch (IOException e) {
					flag = false;
				}
			}
		}).start();
		new Thread(() -> {
			while(flag){
				String s = scanner.nextLine();
				if(s!=null&&(!s.equals(""))) {
					if (s.startsWith("@")) {
						String[] strings = s.split(":", 2);
						printWriter.println(strings[0].substring(1) + ":" + strings[1]);
					} else {
						printWriter.println("所有人:" + s);
					}
				}
			}
		}).start();
		while(flag){}
		printWriter.close();
		bufferedReader.close();
		socket.close();
	}
}
