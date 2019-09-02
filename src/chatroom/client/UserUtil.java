package chatroom.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUtil {
    public static void addUser(String msg, Map<String, List<String>> userMap) {
        String[] ss = msg.split("&");
        List<String> userList = new ArrayList<>();
        for(int i = 1;i<ss.length;i++){
            userList.add(ss[i]);
        }
        System.out.println(ss[0]+"上线");
        userMap.put(ss[0],userList);
    }

    public static void deleteUser(String msg, Map<String, List<String>> userMap){
        System.out.println(msg+"下线了");
        if(userMap.containsKey(msg)){
            userMap.remove(msg);
        }else{
            System.out.println("用户原本 不在线");
        }
    }
}
