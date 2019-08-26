package chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MySocket {
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    public MySocket(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printWriter = new PrintWriter(socket.getOutputStream());
    }

    public MySocket(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter) {
        this.socket = socket;
        this.bufferedReader = bufferedReader;
        this.printWriter = printWriter;
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

    public void sendMessage(String message) throws IOException {
        printWriter.println(message+"\r\n"+"end");
    }

    public void close() throws IOException {
        if(bufferedReader!=null){
            bufferedReader.close();
        }
        if(printWriter!=null){
            printWriter.close();
        }
        socket.close();
    }

    public boolean isConnected(){
        return socket.isConnected();
    }
}
