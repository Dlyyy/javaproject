package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class RecieveThread extends Thread {

    private Socket s;

    public RecieveThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            InputStream is = s.getInputStream();

            DataInputStream dis = new DataInputStream(is);
            while (true) {
                String msg = dis.readUTF();
                System.out.println(msg);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
