package udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Send extends Thread{
	DatagramSocket socket;
	BufferedReader keyRead;

	public Send() throws SocketException {
		this.socket = new DatagramSocket();
		this.keyRead = new BufferedReader(new InputStreamReader(System.in));
	}

	public void run() {
		while(true){
			try {
				byte[] message = keyRead.readLine().getBytes();
				InetAddress address = InetAddress.getByName("169.254.255.255");
				DatagramPacket packet = new DatagramPacket(message, message.length, address, 9090);
				socket.send(packet);
			} catch (IOException e) {
				socket.close();
				e.printStackTrace();
			}
		}
	}
}
