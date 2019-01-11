package socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
//在Client中，每次要发不同的数据都需要修改代码
//可以使用Scanner读取控制台的输入，并发送到服务端，这样每次都可以发送不同的数据了。

public class ClientScanner {
    public static void main(String[] args) {

        try {
            Socket s = new Socket("127.0.0.1", 8888);

            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            //使用Scanner读取控制台的输入，并发送到服务端
            Scanner sc = new Scanner(System.in);

            String str = sc.next();
            dos.writeUTF(str);

            dos.close();
            s.close();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
