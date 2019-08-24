package test;

import tcp.ClientChat;

import java.io.IOException;
import java.net.Socket;

public class TestClient3 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8990);
        new ClientChat("王五",socket).run();
    }
}
