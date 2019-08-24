package chatroom;

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
    private static Map<String,String> ipMap;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(8990);
        this.socketMap = new HashMap<>();
        this.ipMap = new HashMap<>();
    }

    public void run()  {
        while(true){
            try {
                synchronized ("server") {
                    Socket accept = serverSocket.accept();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                    PrintWriter printWriter = new PrintWriter(accept.getOutputStream(),true);
                    String name = bufferedReader.readLine();
                    String ip = accept.getInetAddress().getHostAddress();
                    MySocket mySocket = new MySocket(accept,bufferedReader,printWriter);
                    socketMap.put(name, mySocket);
                    ipMap.put(name,ip);
                    new Thread(new ServerThread(name,mySocket,socketMap,ipMap)).start();
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
