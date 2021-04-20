package netty.zerocopy.old;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * 传统的文件拷贝服务器端
 */
public class OldFileServer {

    public static void main(String[] args) {

        try {
            // 服务器端
            ServerSocket serverSocket = new ServerSocket(7001);

            while (true) {
                // 接收到的客户端请求
                Socket socket = serverSocket.accept();
                // 输入流
                DataInputStream in = new DataInputStream(socket.getInputStream());
                // 一次读取多少数据
                byte[] bytes = new byte[4096];
                // 读取，判断读取结束的标志
                long read;
                // 读取的总数量
                long totalCount = 0;
                while ((read = in.read(bytes)) > 0) {
                    totalCount += read;
                }
                System.out.println("服务器端收到了客户端发来的数据，共 " + totalCount + "个字节");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
