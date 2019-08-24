package test;

import udp.Receive;
import udp.Send;

import java.net.SocketException;

public class TestUDP2 {
    public static void main(String[] args) throws SocketException {
        new Receive().start();
        new Send().start();
    }
}
