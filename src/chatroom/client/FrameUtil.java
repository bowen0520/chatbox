package chatroom.client;

import java.io.PrintWriter;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

public class FrameUtil {
    public static MyFrame getPrivateFrame(String myname,String username, Map<String, List<String>> userMap,Map<String, MyFrame> privateChatMap) throws SocketException {
        MyFrame myFrame = null;
        if(privateChatMap.containsKey(username)){
            myFrame = privateChatMap.get(username);
            myFrame.setAlwaysOnTop(true);
            myFrame.setAlwaysOnTop(false);
        }else {
            if(userMap.containsKey(username)){
                List<String> userList= userMap.get(username);
                myFrame = new ClientFrame(myname, username, userList,userMap, privateChatMap);
                privateChatMap.put(username,myFrame);
            }else{
                System.out.println("对方不在线");
            }
        }
        return myFrame;
    }


    public static MyFrame getGroupFrame(String myname, PrintWriter printWriter,Map<String, MyFrame> groupChatMap) {
        MyFrame myFrame = null;
        if(groupChatMap.containsKey("群聊")){
            myFrame = groupChatMap.get("群聊");
            myFrame.setAlwaysOnTop(true);
            myFrame.setAlwaysOnTop(false);
        }else {
            myFrame = new GroupChat(myname, "群聊", printWriter, groupChatMap);
            groupChatMap.put("群聊",myFrame);
        }
        return myFrame;
    }
}
