package test;

import chatroom.client.ClientLunch;

import java.io.IOException;

public class TestChatRoom1 {
    public static void main(String[] args) throws IOException {
        new ClientLunch().getClientThread().start();
    }
}
