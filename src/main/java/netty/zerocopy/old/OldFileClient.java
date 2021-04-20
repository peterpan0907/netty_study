package netty.zerocopy.old;


import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 传统文件io的文件拷贝客户端
 */
public class OldFileClient {


    public static void main(String[] args) {
        try {
            Socket socket = new Socket();
            // 绑定ip与端口
            socket.connect(new InetSocketAddress("127.0.0.1", 7001));

            // 文件名称
            String fileName = "1.tar.gz";
            //输入流
            FileInputStream in = new FileInputStream(fileName);
            //输出流
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // 一次读取的字节数
            byte[] bytes = new byte[4096];
            // 读取的数量
            long readCount;
            //总数量
            long totalCount = 0;
            // 读取文件的开始时间
            long startTime = System.currentTimeMillis();

            // 一直读取
            while ((readCount = in.read(bytes)) > 0) {
                totalCount += readCount;
                dataOutputStream.write(bytes);
            }
            // 读取的截至时间
            long endTime = System.currentTimeMillis();

            System.out.println("发送总字节数：" + totalCount + "字节， 用时" + (endTime - startTime));
            // 关闭流
            dataOutputStream.close();
            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
