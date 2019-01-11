package socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

//收发字符串
/*直接使用字节流收发字符串比较麻烦，使用数据流对字节流进行封装，这样收发字符串就容易了
1. 把输出流封装在DataOutputStream中
使用writeUTF发送字符串 "Legendary!"
2. 把输入流封装在DataInputStream
使用readUTF读取字符串,并打印*/

public class Client1 {

    public static void main(String[] args) {

        try {
            Socket s = new Socket("127.0.0.1", 8888);

            OutputStream os = s.getOutputStream();

            //把输出流封装在DataOutputStream中
            DataOutputStream dos = new DataOutputStream(os);
            //使用writeUTF发送字符串
            dos.writeUTF("Legendary!");
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
