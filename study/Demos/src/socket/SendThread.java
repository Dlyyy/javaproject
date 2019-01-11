package socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SendThread extends Thread{

    private Socket s;

    public SendThread(Socket s){
        this.s = s;
    }
    public void run(){
        try {
            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            while(true){
                Scanner sc = new Scanner(System.in);
                String str = sc.next();
                dos.writeUTF(str);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}