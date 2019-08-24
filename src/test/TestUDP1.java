package test;

import udp.Receive;
import udp.Send;

import java.net.SocketException;

public class TestUDP1 {
    public static void main(String[] args) throws SocketException {
        new Receive().start();
        new Send().start();
    }
}
