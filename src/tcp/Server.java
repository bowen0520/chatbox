package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static ServerSocket serverSocket;
    private static Map<String, MySocket> socketMap;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(8990);
        this.socketMap = new HashMap<>();
    }

    public void run()  {
        while(true){
            try {
                synchronized ("server") {
                    Socket accept = serverSocket.accept();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                    PrintWriter printWriter = new PrintWriter(accept.getOutputStream(),true);
                    String name = bufferedReader.readLine();
                    MySocket mySocket = new MySocket(accept,bufferedReader,printWriter);
                    socketMap.put(name, mySocket);
                    System.out.println(name);
                    new Chat(name, mySocket, socketMap).start();
                    System.out.println("连接上了" + name);
                }
            }catch (IOException e){
                e.getStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().run();
    }
}