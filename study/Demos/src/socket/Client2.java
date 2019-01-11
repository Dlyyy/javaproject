package socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/*
如果使用单线程开发Socket应用，那么同一时间，要么收消息，要么发消息，不能同时进行。
为了实现同时收发消息，就需要用到多线程

这是因为接受和发送都在主线程中，不能同时进行。 为了实现同时收发消息，基本设计思路是把收发分别放在不同的线程中进行

1. SendThread 发送消息线程
2. RecieveThread 接受消息线程
3. Server一旦接受到连接，就启动收发两个线程
4. Client 一旦建立了连接，就启动收发两个线程
*/


public class Client2 {

    public static void main(String[] args) {

        try {
            Socket s = new Socket("127.0.0.1", 8888);

            // 启动发送消息线程
            new SendThread(s).start();
            // 启动接受消息线程
            new RecieveThread(s).start();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
