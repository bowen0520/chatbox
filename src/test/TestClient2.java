package test;

import tcp.ClientBox;
import tcp.ClientChat;

import java.io.IOException;
import java.net.Socket;

public class TestClient2 {
    public static void main(String[] args) throws IOException {
        new ClientBox("李四","127.0.0.1");
    }
}
