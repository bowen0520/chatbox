package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Receive extends Thread{
	DatagramSocket socket;
	DatagramPacket packet;

	public Receive() throws SocketException {
		this.socket = new DatagramSocket(9090);
		byte[] buf = new byte[1024];
		this.packet = new DatagramPacket(buf, buf.length);
	}

	public void run() {
		while(true) {
			try {
				socket.receive(packet);

				System.out.println(packet.getAddress().getHostAddress() + "说" + new String(packet.getData(), 0, packet.getLength()));
			} catch (IOException e) {
				socket.close();
				e.printStackTrace();
			}
		}
	}
}
