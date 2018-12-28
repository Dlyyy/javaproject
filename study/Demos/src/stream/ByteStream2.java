package stream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteStream2 {
    public static void main(String[] args) {
        /*以字节流的形式向文件写入数据
        OutputStream是字节输出流，同时也是抽象类，只提供方法声明，不提供方法的具体实现。
        FileOutputStream 是OutputStream子类，以FileOutputStream 为例向文件写出数据

        注: 如果文件d:/lol2.txt不存在，写出操作会自动创建该文件。
        但是如果是文件 d:/xyz/lol2.txt，而目录xyz又不存在，会抛出异常*/
        try {
            // 准备文件lol2.txt其中的内容是空的
            File f = new File("d:/lol2.txt");
            // 准备长度是2的字节数组，用88,89初始化，其对应的字符分别是X,Y
            byte data[] = { 88, 89 };

            // 创建基于文件的输出流
            FileOutputStream fos = new FileOutputStream(f);
            // 把数据写入到输出流
            fos.write(data);
            // 关闭输出流
            fos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
