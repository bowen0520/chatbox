package chatroom;

import java.io.IOException;
import java.util.Scanner;

public class ClientLunch {
    public Thread getClientThread() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入名称：");
        String name = scanner.nextLine();
        System.out.print("请输入IP：");
        String IP = scanner.nextLine();
        return new Thread(new ClientThread(name,IP));
    }
}
