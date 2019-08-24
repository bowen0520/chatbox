package test;

import chatroom.ClientLunch;

import java.io.IOException;

public class TestChatRoom1 {
    public static void main(String[] args) throws IOException {
        new ClientLunch().getClientThread();
    }
}
