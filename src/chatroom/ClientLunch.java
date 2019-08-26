package chatroom;

import java.io.IOException;
import java.util.Scanner;

public class ClientLunch {
    public Thread getClientThread() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入名称：");
        String name = scanner.nextLine();
        System.out.print("请输入服务器ip：");
        String serverIP = scanner.nextLine();
        System.out.print("请输入ports：");
        String[] ports = scanner.nextLine().split(" ");
        return new Thread(new ClientThread(name,serverIP,ports));
    }
}
