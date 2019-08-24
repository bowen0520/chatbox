package chatroom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ServerThread implements Runnable{
    private String name;
    private MySocket mySocket;
    private Map<String, MySocket> socketMap;
    private static Map<String,String> ipMap;
    private boolean flag = true;

    public ServerThread(String name, MySocket mySocket, Map<String, MySocket> socketMap,Map<String,String> ipMap) {
        this.name = name;
        this.mySocket = mySocket;
        this.socketMap = socketMap;
        this.ipMap = ipMap;
    }

    @Override
    public void run() {
        System.out.println(name+"上线了");
        while(mySocket.isConnected()&&flag){
            try {
                sendAll(getIPMessage());
                mySocket.getMessage();
                Thread.sleep(1000);
            } catch (InterruptedException | IOException e) {
                flag = false;
                System.out.println(name+"断开连接");
            }
        }
        ipMap.remove(name);
        socketMap.remove(name);
        try {
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIPMessage(){
        Iterator<Map.Entry<String, String>> iterator = ipMap.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while(iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            if(!next.getKey().equals(name)) {
                stringBuilder.append(next.getKey() + "::" + next.getValue());
                break;
            }
        }
        while(iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            if(!next.getKey().equals(name)) {
                stringBuilder.append("\r\n"+next.getKey() + "::" + next.getValue());
            }
        }
        String an = stringBuilder.toString();
        return an;
    }

    public void sendAll(String message) throws IOException {
        Iterator<Map.Entry<String, MySocket>> iterator = socketMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, MySocket> next = iterator.next();
            if(!next.getKey().equals(name)){
                next.getValue().sendMessage(message);
            }
        }
    }
}
