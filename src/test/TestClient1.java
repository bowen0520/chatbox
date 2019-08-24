package test;

import tcp.ClientBox;

import java.io.IOException;

public class TestClient1 {
    public static void main(String[] args) throws IOException {
        new ClientBox("张三","127.0.0.1");
    }
}
