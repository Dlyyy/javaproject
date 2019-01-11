package socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 {

    public static void main(String[] args) {
        try {

            ServerSocket ss = new ServerSocket(8888);

            System.out.println("监听在端口号:8888");
            Socket s = ss.accept();

            InputStream is = s.getInputStream();

            //把输入流封装在DataInputStream
            DataInputStream dis = new DataInputStream(is);
            //使用readUTF读取字符串
            String msg = dis.readUTF();
            System.out.println(msg);
            dis.close();
            s.close();
            ss.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
