package chatroom.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class SocketInteractiveThread implements Runnable{
    private String name;
    private Map<String, List<String>> userMap;
    private Map<String,MyFrame> groupChatMap;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public SocketInteractiveThread(String name, Map<String, List<String>> userMap, Map<String, MyFrame> groupChatMap, BufferedReader bufferedReader, PrintWriter printWriter) {
        this.name = name;
        this.userMap = userMap;
        this.groupChatMap = groupChatMap;
        this.bufferedReader = bufferedReader;
        this.printWriter = printWriter;
    }

    @Override
    public void run() {
        boolean flag = true;
        while(flag){
            try {
                String message = getMessage();
                String[] msgs = message.split(":",2);
                if(msgs[0].equals("lunch")){
                    UserUtil.addUser(msgs[1],userMap);
                    FrameUtil.getGroupFrame(name,printWriter,groupChatMap).getInputBox().append(msgs[1].split("&")[0]+"上线了\r\n");
                }else if(msgs[0].equals("quit")){
                    UserUtil.deleteUser(msgs[1],userMap);
                    FrameUtil.getGroupFrame(name,printWriter,groupChatMap).getInputBox().append(msgs[1]+"下线了\r\n");
                }else if(msgs[0].equals("message")){
                    FrameUtil.getGroupFrame(name,printWriter,groupChatMap).getInputBox().append(msgs[1]+"\r\n");
                }
            } catch (IOException e) {
                System.out.println("与服务器连接异常");
                flag = false;
                System.exit(0);
            }
        }
    }
    public String getMessage() throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        String str = bufferedReader.readLine();
        stringBuffer.append(str);
        while(!(str = bufferedReader.readLine()).equals("end")){
            stringBuffer.append("\r\n"+str);
        }
        return stringBuffer.toString();
    }
}
